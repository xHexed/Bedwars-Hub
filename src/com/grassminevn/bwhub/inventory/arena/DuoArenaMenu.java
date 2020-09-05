package com.grassminevn.bwhub.inventory.arena;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.inventory.InventoryHandler;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DuoArenaMenu extends ArenaUpdateHandler implements InventoryHandler {

    public DuoArenaMenu() {
        super();

        /*
        final ItemStack returnItem = new ItemStack(Material.DIAMOND_SWORD);
        meta = returnItem.getItemMeta();
        meta.setDisplayName("§f");
        meta.setLore(Collections.singletonList("§fClick để quay lại."));
        returnItem.setItemMeta(meta);
        inventory.setItem(54, returnItem);
        */

        final ItemStack auto = new ItemStack(Material.NETHER_STAR);
        final ItemMeta meta = auto.getItemMeta();
        meta.setDisplayName("§fTự động tìm phòng duo");
        meta.setLore(Arrays.asList("§fClick để tự động tìm phòng", "§fduo ngẫu nhiên."));
        auto.setItemMeta(meta);
        inventory.setItem(49, auto);

        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, ArenaMenuHandler.getInventoryIcon("§fPhòng: Duo " + (i + 1), "bw_duo" + i, 16));
        }
    }

    @Override
    public void onClick(final InventoryClickEvent event) {
        ArenaMenuHandler.handleArenaClick(event, arenas.values(), "bw_duo");
    }

    @Override
    public Inventory onArenaUpdate(final Arena arena) {
        arenas.put(arena.getArenaNumber(), arena);
        inventory.setItem(arena.getArenaNumber() - 1, ArenaMenuHandler.getInventoryIcon("§fPhòng: Duo " + arena.getArenaNumber(), arena.getName(), arena.getMaxPlayers()));
        return inventory;
    }
}