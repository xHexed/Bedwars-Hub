package com.grassminevn.bwhub.inventory;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class SelectorMenu implements InventoryHolder {
    private static final Inventory inventory = Bukkit.createInventory(new SelectorMenu(), 54, "Chọn phòng");

    static {
        setInventoryIcons("§fPhòng: Solo ", "bw_solo", 8, 9);
        setInventoryIcons("§fPhòng: Duo ", "bw_duo", 16, 18);
        setInventoryIcons("§fPhòng: Squad ", "bw_squad", 16, 27);
        ItemMeta meta;
        final ItemStack special = new ItemStack(Material.DIAMOND_SWORD);
        meta = special.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("§fPhòng đặc biệt (Comming soon...)");
        meta.setLore(Collections.singletonList("§fClick để xem các phòng đặc biệt."));
        special.setItemMeta(meta);
        inventory.setItem(39, special);

        final ItemStack auto = new ItemStack(Material.NETHER_STAR);
        meta = auto.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("§fTự động tìm phòng");
        meta.setLore(Arrays.asList("§fClick để tự động tìm phòng", "§fngẫu nhiên."));
        auto.setItemMeta(meta);
        inventory.setItem(41, auto);

        meta.setDisplayName("§fTự động tìm phòng solo");
        meta.setLore(Arrays.asList("§fClick để tự động tìm phòng", "§fsolo ngẫu nhiên."));
        auto.setItemMeta(meta);
        inventory.setItem(9, auto);

        meta.setDisplayName("§fTự động tìm phòng duo");
        meta.setLore(Arrays.asList("§fClick để tự động tìm phòng", "§fduo ngẫu nhiên."));
        auto.setItemMeta(meta);
        auto.setAmount(2);
        inventory.setItem(18, auto);

        meta.setDisplayName("§fTự động tìm phòng squad");
        meta.setLore(Arrays.asList("§fClick để tự động tìm phòng", "§fsquad ngẫu nhiên."));
        auto.setItemMeta(meta);
        auto.setAmount(4);
        inventory.setItem(27, auto);
    }

    public static void updateInventoryIcon(final Arena arena) {
        switch (arena.getArenaType()) {
            case SOLO:
                setInventoryIcon("§fPhòng: Solo " + arena.getArenaNumber(), arena.getName(), arena.getMaxPlayers(), 9 + arena.getArenaNumber());
                break;
            case DUO:
                setInventoryIcon("§fPhòng: Duo " + arena.getArenaNumber(), arena.getName(), arena.getMaxPlayers(), 18 + arena.getArenaNumber());
                break;
            case SQUAD:
                setInventoryIcon("§fPhòng: Squad " + arena.getArenaNumber(), arena.getName(), arena.getMaxPlayers(), 27 + arena.getArenaNumber());
                break;
        }
    }

    private static void setInventoryIcon(final String iconName, final String arenaName, final int maxPlayers, final int slot) {
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
        inventory.setItem(slot, arenaIcon);
    }

    private static void setInventoryIcons(final String iconName, final String arenaType, final int maxPlayers, final int slot) {
        for (int i = 1; i <= 8; i++) {
            setInventoryIcon(iconName + i, arenaType + i, maxPlayers, slot + i);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
