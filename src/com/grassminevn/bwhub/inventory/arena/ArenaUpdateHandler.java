package com.grassminevn.bwhub.inventory.arena;

import com.grassminevn.bwhub.Arena;
import org.bukkit.inventory.Inventory;

public interface ArenaUpdateHandler {
    Inventory onUpdate(final Arena arena);
}
