/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package com.grassminevn.bwhub.bungeecord.in;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Console;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Channel;
import com.grassminevn.bwhub.bungeecord.Packet;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;

public class PacketArenaCreate
extends Packet {
    private final Arena arena;

    private PacketArenaCreate(final Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaCreate.getID() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + arena.getStatus().getID() + "/" + arena.getPlayers() + "/" + arena.getMaxPlayers() + "/" + arena.getIcon().getType().name() + ":" + arena.getIcon().getDurability() + "/" + Util.SLASH.matcher(arena.getMadeBy()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + arena.getTeams() + "/" + arena.getInTeamPlayers();
    }

    public static PacketArenaCreate build(final String str, final Channel channel) {
        final String[] strs = str.split("/");
        if (strs.length == 9) {
            try {
                final String[] strs2 = strs[5].split(":");
                final int i = Integer.parseInt(strs2[1]);
                final Arena arena = new Arena(channel, Util.KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")), Util.KEYSLASH.matcher(strs[6]).replaceAll(Matcher.quoteReplacement("/")), Integer.parseInt(strs[3]), Integer.parseInt(strs[4]), new ItemStack(Material.getMaterial(strs2[0]), 1, (short)i), Arena.ArenaStatus.fromInt(strs[2]), Integer.parseInt(strs[7]), Integer.parseInt(strs[8]));
                return new PacketArenaCreate(arena);
            }
            catch (final Exception e) {
                Console.printBungeecordWarn("Wrong packet format:");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}

