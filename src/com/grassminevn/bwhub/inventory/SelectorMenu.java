package com.grassminevn.bwhub.inventory;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class SelectorMenu implements InventoryHolder {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Chọn phòng");

    public SelectorMenu() {
        final ItemStack solo = new ItemStack(Material.WOOD_SWORD);
        ItemMeta meta = solo.getItemMeta();
        for (int i = 1; i <= 8; i++) {
            meta.setDisplayName("§fPhòng: Solo " + i);
            final Arena arena = Util.getArena("sl" + i);
            if (arena == null) {
                meta.setLore(Arrays.asList("§fMap: Road", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/8"));
            }
            else {
                meta.setLore(arena.getLore());
            }
            solo.setAmount(i);
            solo.setItemMeta(meta);
            inventory.setItem(i + 9, solo);
        }
        final ItemStack duo = new ItemStack(Material.IRON_SWORD);
        meta = duo.getItemMeta();
        for (int i = 1; i <= 8; i++) {
            meta.setDisplayName("§fPhòng: Duo " + i);
            final Arena arena = Util.getArena("du" + i);
            if (arena == null) {
                meta.setLore(Arrays.asList("§fMap: Road", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/16"));
            }
            else {
                meta.setLore(arena.getLore());
            }
            duo.setAmount(i);
            duo.setItemMeta(meta);
            inventory.setItem(i + 18, duo);
        }
        final ItemStack squad = new ItemStack(Material.GOLD_SWORD);
        meta = squad.getItemMeta();
        for (int i = 1; i <= 8; i++) {
            meta.setDisplayName("§fPhòng: Squad " + i);
            final Arena arena = Util.getArena("sq" + i);
            if (arena == null) {
                meta.setLore(Arrays.asList("§fMap: Road", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/16"));
            }
            else {
                meta.setLore(arena.getLore());
            }
            squad.setAmount(i);
            squad.setItemMeta(meta);
            inventory.setItem(i + 27, squad);
        }
        final ItemStack special = new ItemStack(Material.DIAMOND_SWORD);
        meta = special.getItemMeta();
        meta.setDisplayName("§fPhòng đặc biệt (Comming soon...)");
        meta.setLore(Collections.singletonList("§fClick để xem các phòng đặc biệt."));
        special.setItemMeta(meta);
        inventory.setItem(39, special);
        final ItemStack auto = new ItemStack(Material.NETHER_STAR);
        meta = auto.getItemMeta();
        meta.setDisplayName("§fTự động tìm phòng");
        meta.setLore(Arrays.asList("§fClick để tự động tìm phòng", "§fngẫu nhiên."));
        auto.setItemMeta(meta);
        inventory.setItem(41, auto);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
