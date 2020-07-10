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
                Util.addArena(new Arena(data[1], data[2], Integer.parseInt(data[3])));
                return;
            case "disable":
                Util.removeArena(data[1]);
                return;
            case "join": {
                Arena arena = Util.getArena(data[1]);
                if (arena == null) {
                    arena = new Arena(data[1], data[3], Integer.parseInt(data[4]));
                    Util.addArena(arena);
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
                    arena = new Arena(data[1], data[4], Integer.parseInt(data[5]));
                    Util.addArena(arena);
                    arena.setPlayers(Integer.parseInt(data[3]));
                    return;
                }
                else {
                    arena.setPlayers(arena.getPlayers() - 1);
                }
                return;
            }
            case "update": {
                Arena arena = Util.getArena(data[1]);
                if (arena == null) {
                    arena = new Arena(data[1], data[4], Integer.parseInt(data[5]));
                    Util.addArena(arena);
                }
                arena.setStatus(Arena.ArenaStatus.valueOf(data[2]));
            }
        }
    }
}

