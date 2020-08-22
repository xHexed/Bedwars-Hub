package com.grassminevn.bwhub.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryHandler {
    default void onClick(final InventoryClickEvent event) { }
}