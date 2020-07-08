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
        setInventoryIcon("§fPhòng: Solo ", "bw_solo", 8, 9);
        setInventoryIcon("§fPhòng: Duo ", "bw_duo", 16, 18);
        setInventoryIcon("§fPhòng: Squad ", "bw_squad", 16, 27);
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

    private void setInventoryIcon(final String iconName, final String arenaType, final int maxPlayers, final int slot) {
        ItemStack arenaIcon;
        ItemMeta meta;
        for (int i = 1; i <= 8; i++) {
            final Arena arena = Util.getArena(arenaType + i);
            if (arena == null) {
                arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                meta = arenaIcon.getItemMeta();
                meta.setLore(Arrays.asList("§fMap: ???", "§fTrạng thái: Dừng hoạt động", "§fNgười chơi: 0/16"));
            }
            else {
                if (arena.getStatus() == Arena.ArenaStatus.Running || arena.getStatus() == Arena.ArenaStatus.Reseting) {
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                    meta = arenaIcon.getItemMeta();
                }
                else {
                    arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                    meta = arenaIcon.getItemMeta();
                    if (arena.getPlayers() == 0) {
                        arenaIcon.setAmount(1);
                    }
                    else {
                        if (arena.getPlayers() > maxPlayers / 2)
                            arenaIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                        arenaIcon.setAmount(arena.getPlayers());
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                }
                meta.setLore(arena.getLore());
            }
            meta.setDisplayName(iconName + i);
            arenaIcon.setItemMeta(meta);
            inventory.setItem(i + slot, arenaIcon);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
