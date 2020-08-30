package com.grassminevn.bwhub.inventory.arena;

import com.grassminevn.bwhub.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ArenaMenuHandler {
    private static final SoloArenaMenu soloArenaMenu = new SoloArenaMenu();
    private static final DuoArenaMenu duoArenaMenu = new DuoArenaMenu();
    private static final SquadArenaMenu squadArenaMenu = new SquadArenaMenu();

    public static SoloArenaMenu getSoloArenaMenu() {
        return soloArenaMenu;
    }

    public static DuoArenaMenu getDuoArenaMenu() {
        return duoArenaMenu;
    }

    public static SquadArenaMenu getSquadArenaMenu() {
        return squadArenaMenu;
    }

    public static void updateView(final Arena arena) {
        if (arena == null) return;
        final ArenaUpdateHandler arenaMenu;
        switch (arena.getArenaType()) {
            case SOLO:
                arenaMenu = soloArenaMenu;
                break;
            case DUO:
                arenaMenu = duoArenaMenu;
                break;
            case SQUAD:
                arenaMenu = squadArenaMenu;
                break;
            default:
                return;
        }
        final Inventory updatedInventory = arenaMenu.onArenaUpdate(arena);
        Bukkit.getScheduler().runTask(BedwarsHub.plugin, () -> {
            final Optional<HumanEntity> viewer = arenaMenu.getViewers().parallelStream().findAny();
            if (!viewer.isPresent()) return;

            final Inventory inventory = viewer.get().getOpenInventory().getTopInventory();
            final ItemStack[] currentContents = viewer.get().getOpenInventory().getTopInventory().getContents();
            final ItemStack[] updatedContents = updatedInventory.getContents();
            final Map<Integer, ItemStack> itemUpdateList = new HashMap<>();
            for (int i = 0; i < inventory.getSize(); i++) {
                if (currentContents[i] == null) continue;
                if (currentContents[i].isSimilar(updatedContents[i])) {
                    itemUpdateList.put(i, updatedContents[i]);
                }
            }
            for (final HumanEntity player : arenaMenu.getViewers()) {
                itemUpdateList.forEach(player.getOpenInventory().getTopInventory()::setItem);
            }
        });
    }

    public static void addViewer(final InventoryOpenEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof ArenaUpdateHandler) {
            ((ArenaUpdateHandler) holder).addViewer(event.getPlayer());
        }
    }

    public static void removeViewer(final InventoryCloseEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof ArenaUpdateHandler) {
            ((ArenaUpdateHandler) holder).removeViewer(event.getPlayer());
        }
    }

    public static ItemStack getInventoryIcon(final String iconName, final String arenaName, final int maxPlayers) {
        ItemStack arenaIcon;
        final ItemMeta meta;
        final Arena arena = Util.getArena(arenaName);
        if (arena == null) {
            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            meta = arenaIcon.getItemMeta();
            meta.setLore(Arrays.asList("§fMap: ???", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/" + maxPlayers));
        }
        else {
            switch (arena.getStatus()) {
                case Stopped:
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                    meta = arenaIcon.getItemMeta();
                    meta.setLore(Arrays.asList("§fMap: ???", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/" + maxPlayers));
                    break;
                case Running:
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                    meta = arenaIcon.getItemMeta();
                    break;
                case EndLobby:
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
                    meta = arenaIcon.getItemMeta();
                    break;
                case Reseting:
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
                    meta = arenaIcon.getItemMeta();
                    break;
                default:
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                    meta = arenaIcon.getItemMeta();
                    if (arena.getPlayers() <= 0) {
                        arenaIcon.setAmount(1);
                    }
                    else {
                        if (arena.getPlayers() > maxPlayers / 2)
                            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                        if (arena.getPlayers() == maxPlayers)
                            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                        arenaIcon.setAmount(arena.getPlayers());
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    break;
            }
            meta.setLore(arena.getLore());
        }
        meta.setDisplayName(iconName);
        arenaIcon.setItemMeta(meta);
        return arenaIcon;
    }

    static void handleArenaClick(final InventoryClickEvent event, final Iterable<Arena> arenas, final String arenaName) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        if (inventory != null && !(inventory.getHolder() instanceof SoloArenaMenu)) return;
        event.setCancelled(true);
        final int slot = event.getSlot();
        if (slot == 49) {
            Util.autoJoin(player, arenas, "");
        }
        if (!isArenaClicked(slot)) return;
        if (!Util.config_beta || player.hasPermission(Permission.BetaUser.getPermission())) {
            final Arena arena = Util.getArena(arenaName + getArenaNumber(slot));
            if (arena == null || !arena.isJoinable()) return;
            Util.connect(player, arena);
        } else {
            player.sendMessage(Language.Only_BetaMember.toString());
        }
    }

    private static boolean isArenaClicked(final int slot) {
        return slot < 45;
    }

    private static int getArenaNumber(final int slot) {
        return slot + 1;
    }
}
