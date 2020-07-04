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
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Chọn phòng");

    public SelectorMenu() {
        ItemStack arenaIcon;
        ItemMeta meta;
        for (int i = 1; i <= 8; i++) {
            final Arena arena = Util.getArena("bw_solo" + i);
            if (arena == null || arena.getStatus() == Arena.ArenaStatus.Stopped) {
                arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                meta = arenaIcon.getItemMeta();
                meta.setLore(Arrays.asList("§fMap: ???", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/8"));
            }
            else {
                if (arena.getStatus() == Arena.ArenaStatus.Running || arena.getStatus() == Arena.ArenaStatus.Reseting)
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                else {
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                    if (arena.getPlayers() == 0) {
                        arenaIcon.setAmount(1);
                    }
                    else {
                        if (arena.getPlayers() > 4)
                            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                        arenaIcon.setAmount(arena.getPlayers());
                        arenaIcon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                    }
                }
                meta = arenaIcon.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setLore(arena.getLore());
            }
            meta.setDisplayName("§fPhòng: Solo " + i);
            arenaIcon.setItemMeta(meta);
            inventory.setItem(i + 9, arenaIcon);
        }
        for (int i = 1; i <= 8; i++) {
            final Arena arena = Util.getArena("bw_duo" + i);
            if (arena == null) {
                arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                meta = arenaIcon.getItemMeta();
                meta.setLore(Arrays.asList("§fMap: ???", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/16"));
            }
            else {
                if (arena.getStatus() == Arena.ArenaStatus.Running || arena.getStatus() == Arena.ArenaStatus.Reseting)
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                else {
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                    if (arena.getPlayers() == 0) {
                        arenaIcon.setAmount(1);
                    }
                    else {
                        if (arena.getPlayers() > 4)
                            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                        arenaIcon.setAmount(arena.getPlayers());
                        arenaIcon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                    }
                }
                meta = arenaIcon.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setLore(arena.getLore());
            }
            meta.setDisplayName("§fPhòng: Duo " + i);
            arenaIcon.setItemMeta(meta);
            inventory.setItem(i + 18, arenaIcon);
        }
        for (int i = 1; i <= 8; i++) {
            final Arena arena = Util.getArena("bw_squad" + i);
            if (arena == null) {
                arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                meta = arenaIcon.getItemMeta();
                meta.setLore(Arrays.asList("§fMap: ???", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/16"));
            }
            else {
                if (arena.getStatus() == Arena.ArenaStatus.Running || arena.getStatus() == Arena.ArenaStatus.Reseting)
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                else {
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                    if (arena.getPlayers() == 0) {
                        arenaIcon.setAmount(1);
                    }
                    else {
                        if (arena.getPlayers() > 4)
                            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                        arenaIcon.setAmount(arena.getPlayers());
                        arenaIcon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                    }
                }
                meta = arenaIcon.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setLore(arena.getLore());
            }
            meta.setDisplayName("§fPhòng: Squad " + i);
            arenaIcon.setItemMeta(meta);
            inventory.setItem(i + 27, arenaIcon);
        }
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
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
