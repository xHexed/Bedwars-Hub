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

import com.grassminevn.bwhub.inventory.InventoryHandler;
import com.grassminevn.bwhub.inventory.SelectorMenu;
import com.grassminevn.bwhub.inventory.arena.ArenaUpdateHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Events implements Listener {
    private static final Set<UUID> cooldown = new HashSet<>();
    private static final Collection<HumanEntity> viewers = ConcurrentHashMap.newKeySet();

    @EventHandler
    public void onInventoryOpenEvent(final InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof SelectorMenu)) return;
        viewers.add(event.getPlayer());
    }

    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent event) {
        viewers.remove(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        if (item == null || !item.getType().isItem()) return;
        final Entity player = event.getWhoClicked();
        final Inventory inventory = event.getView().getTopInventory();
        final InventoryHolder topInventory = inventory.getHolder();
        if (topInventory instanceof InventoryHandler) {
            if (cooldown.contains(player.getUniqueId()))
                return;
            final InventoryHandler holder = ((InventoryHandler) topInventory);
            if (event.getInventory().equals(inventory))
                holder.onClick(event);
        }
        cooldown.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(BedwarsHub.plugin, () -> cooldown.remove(player.getUniqueId()), 20);
    }

    public static void updateView(final Arena arena) {
        if (arena == null) return;
        Bukkit.getScheduler().runTask(BedwarsHub.plugin, () -> {
            final Optional<HumanEntity> viewer = viewers.parallelStream().findAny();
            if (!viewer.isPresent()) return;
            final Inventory inventory = viewer.get().getOpenInventory().getTopInventory();
            final Inventory currentInventory = viewer.get().getOpenInventory().getTopInventory();
            final Inventory updatedInventory = ((ArenaUpdateHandler) inventory.getHolder()).onUpdate(arena);
            final ItemStack[] currentContents = currentInventory.getContents();
            final ItemStack[] updatedContents = updatedInventory.getContents();
            final Map<Integer, ItemStack> itemUpdateList = new HashMap<>();
            for (int i = 0; i < inventory.getSize(); i++) {
                if (currentContents[i].isSimilar(updatedContents[i])) {
                    itemUpdateList.put(i, updatedContents[i]);
                }
            }
            for (final HumanEntity player : viewers) {
                final Inventory playerInventory = player.getOpenInventory().getTopInventory();
                itemUpdateList.forEach(playerInventory::setItem);
            }
        });
    }
}

