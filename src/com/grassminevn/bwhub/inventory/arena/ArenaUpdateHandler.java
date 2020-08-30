package com.grassminevn.bwhub.inventory.arena;

import com.grassminevn.bwhub.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class ArenaUpdateHandler implements InventoryHolder {
    private final List<HumanEntity> viewers = new ArrayList<>();
    protected final Map<Integer, Arena> arenas = new HashMap<>();
    protected final Inventory inventory = Bukkit.createInventory(this, 54, "Chọn phòng");

    public ArenaUpdateHandler() {
        final ItemMeta meta;
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        meta = glass.getItemMeta();
        meta.setDisplayName("");
        glass.setItemMeta(meta);
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, glass);
        }
    }

    abstract Inventory onArenaUpdate(final Arena arena);

    public List<HumanEntity> getViewers() {
        return viewers;
    }

    public void addViewer(final HumanEntity viewer) {
        viewers.add(viewer);
    }

    public void removeViewer(final HumanEntity viewer) {
        viewers.remove(viewer);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
