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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Events
implements Listener {
    private static final Set<UUID> antispam = new HashSet<>();
    private static final Map<UUID, Inventory> viewingArenas = new HashMap<>();

    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent event) {
        viewingArenas.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        if (!viewingArenas.containsKey(event.getWhoClicked().getUniqueId())) return;
        final Arena arena;
        event.setCancelled(true);
        final Player player = (Player)event.getWhoClicked();
        if (!antispam.contains(player.getUniqueId()) && event.getClickedInventory() != null && event.getClickedInventory().getTitle() != null && event.getClickedInventory().getTitle().startsWith(Util.config_lobbyVillagerPrefix) && event.getCurrentItem() != null && event.getCurrentItem().getType() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName() != null && (arena = Util.getArena(event.getCurrentItem().getItemMeta().getDisplayName().replace(String.valueOf(ChatColor.UNDERLINE), ""))) != null) {
            if (!Util.config_beta || Util.hasPermission(player, Permission.BetaUser)) { arena.getChannel().Connect(player, arena);
                } else {
                player.sendMessage(Language.Only_BetaMember.getMessage());
            }
            if (Util.config_signAntispam) {
                antispam.add(player.getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(BedwarsHub.plugin, () -> antispam.remove(player.getUniqueId()), (int)(Util.config_antispamDelay * 20.0));
            }
        }
    }

    private static void updateViewArena(final Inventory inv, final List<Arena> arenas, final UUID player) {
        if (inv != null && inv.getType() != null && inv.getType() == InventoryType.CHEST) {
            int i;
            for (i = 0; i < inv.getSize(); ++i) {
                inv.setItem(i, null);
            }
            if (arenas != null) {
                if (arenas.size() == 1 && inv.getSize() > 13) {
                    inv.setItem(13, Events.getViewArenaItem(arenas.get(0)));
                } else if (arenas.size() > 1) {
                    i = 0;
                    final int max = inv.getSize();
                    for (final Arena arena : arenas) {
                        if (i < max) {
                            inv.setItem(i, Events.getViewArenaItem(arena));
                        }
                        ++i;
                    }
                }
            }
        } else {
            viewingArenas.remove(player);
        }
    }

    private static void updateViewArena(final Inventory inv) {
        if (inv.getItem(0) == null && inv.getItem(13) != null) {
            final String name = inv.getItem(13).getItemMeta().getDisplayName().replace(String.valueOf(ChatColor.UNDERLINE), "");
            final Arena arena = Util.getArena(name);
            if (arena != null) {
                inv.setItem(13, Events.getViewArenaItem(arena));
            }
        } else {
            for (int i = 0; i < inv.getSize(); ++i) {
                if (inv.getItem(i) == null) continue;
                final String name = inv.getItem(i).getItemMeta().getDisplayName().replace(String.valueOf(ChatColor.UNDERLINE), "");
                final Arena arena = Util.getArena(name);
                if (arena != null) {
                    inv.setItem(i, Events.getViewArenaItem(arena));
                    continue;
                }
                inv.setItem(i, null);
            }
        }
    }

    private static ItemStack getViewArenaItem(final Arena arena) {
        final ItemStack is = arena.getIcon().clone();
        final ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.UNDERLINE + arena.getName());
        im.setLore(arena.getLore());
        is.setItemMeta(im);
        return is;
    }

    public static void ArenaUpdate() {
        Events.ArenaUpdate(false);
    }

    public static void ArenaUpdate(final boolean everything) {
        if (!everything) {
            for (final Map.Entry<UUID, Inventory> entry : viewingArenas.entrySet()) {
                Events.updateViewArena(entry.getValue());
            }
        } else {
            for (final Map.Entry<UUID, Inventory> entry : viewingArenas.entrySet()) {
                Events.updateViewArena(entry.getValue(), Events.getArenas(entry.getValue().getTitle().replace(Util.config_lobbyVillagerPrefix, "")), entry.getKey());
            }
        }
    }

    private static List<Arena> getArenas(final String str) {
        final List<Arena> list = new ArrayList<>();
        final String[] strs = str.split("x");
        if (strs.length == 1 || strs.length >= 1 && !Util.isInteger(strs[0])) {
            final Arena arena = Util.getArena(strs[0]);
            if (arena != null) {
                list.add(arena);
            }
        } else if (strs.length == 2) {
            final int teams = Integer.parseInt(strs[0]);
            final int playersInTeam = Integer.parseInt(strs[1]);
            for (final Arena arena : Util.arenas) {
                if (arena.getTeams() != teams || arena.getInTeamPlayers() != playersInTeam) continue;
                list.add(arena);
            }
        }
        return list;
    }

}

