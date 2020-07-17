/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package com.grassminevn.bwhub.bungeecord;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;

public class Communication {
    public static void onPacketReceived(final String msg) {
        final String[] data = msg.split(":");
        switch (data[0].toLowerCase()) {
            case "enable":
                Util.addArena(data[1], data[2], data[3], data[4], Integer.parseInt(data[5]));
                return;
            case "disable":
                Util.removeArena(data[1]);
                return;
            case "join": {
                Arena arena = Util.getArena(data[1]);
                if (arena == null) {
                    arena = Util.addArena(data[1], data[3], data[4], null, null);
                    arena.setPlayers(1);
                    return;
                }
                else {
                    arena.setPlayers(arena.getPlayers() + 1);
                }
                return;
            }
            case "quit": {
                Arena arena = Util.getArena(data[1]);
                if (arena == null) {
                    arena = Util.addArena(data[1], data[4], data[5], null, null);
                    arena.setPlayers(Integer.parseInt(data[3]));
                    return;
                }
                else {
                    arena.setPlayers(Integer.parseInt(data[3]));
                }
                return;
            }
            case "update": {
                Arena arena = Util.getArena(data[1]);
                if (arena == null) {
                    arena = Util.addArena(data[1], data[4], data[5], data[2], null);
                }
                arena.setStatus(Arena.ArenaStatus.valueOf(data[2]));
            }
        }
    }
}

