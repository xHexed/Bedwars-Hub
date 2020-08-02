/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package com.grassminevn.bwhub;

import com.grassminevn.bwhub.inventory.SelectorMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Events implements Listener {
    private static final Set<UUID> cooldown = new HashSet<>();
    private static final Collection<UUID> viewers = ConcurrentHashMap.newKeySet();

    @EventHandler
    public void onInventoryOpenEvent(final InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof SelectorMenu)) return;
        viewers.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent event) {
        viewers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();
        if (inventory != null && !(inventory.getHolder() instanceof SelectorMenu)) return;
        event.setCancelled(true);
        final Player player = (Player) event.getWhoClicked();
        final int slot = event.getSlot();
        if (slot == 41) {
            Util.autoJoin(player, "");
        }
        if (!isArenaClicked(slot)) return;
        if (cooldown.contains(player.getUniqueId()))
            return;
        if (!Util.config_beta || player.hasPermission(Permission.BetaUser.getPermission())) {
            if (isAutoJoin(slot)) {
                Util.autoJoin(player, getMode(slot));
            } else {
                final Arena arena = Util.getArena(getMode(slot) + getArenaNumber(slot));
                if (arena == null || !arena.isJoinable()) return;
                Util.connect(player, arena);
            }
        } else {
            player.sendMessage(Language.Only_BetaMember.getMessage());
        }
        cooldown.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(BedwarsHub.plugin, () -> cooldown.remove(player.getUniqueId()), 20);
    }

    private boolean isArenaClicked(final int slot) {
        return slot >= 9 && slot <= 35;
    }

    private int getArenaNumber(final int slot) {
        if (slot >= 10 && slot <= 17)
            return slot - 9;
        if (slot >= 19 && slot <= 26)
            return slot - 18;
        if (slot >= 28 && slot <= 35)
            return slot - 27;
        else
            return 0;
    }

    private String getMode(final int slot) {
        if (slot >= 9 && slot <= 17)
            return "bw_solo";
        if (slot >= 18 && slot <= 26)
            return "bw_duo";
        else
            return "bw_squad";
    }

    private boolean isAutoJoin(final int slot) {
        return slot == 9 || slot == 18 || slot == 27;
    }

    public static void updateView(final Arena arena) {
        if (arena == null) return;
        final int slot;
        switch (arena.getArenaType()) {
            case SOLO:
                slot = 9 + arena.getArenaNumber();
                break;
            case DUO:
                slot = 18 + arena.getArenaNumber();
                break;
            case SQUAD:
                slot = 27 + arena.getArenaNumber();
                break;
            default:
                return;
        }
        SelectorMenu.updateInventoryIcon(arena);
        Bukkit.getScheduler().runTask(BedwarsHub.plugin, () -> {
            for (final UUID uuid : viewers) {
                Bukkit.getPlayer(uuid).getOpenInventory().getTopInventory().setItem(slot, new SelectorMenu().getInventory().getItem(slot));
            }
        });
    }
}

