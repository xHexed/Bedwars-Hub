/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.bungeecord.in;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Packet;

import java.util.regex.Matcher;

public class PacketArenaUpdateSlots
extends Packet {
    private final Arena arena;
    private final int to;

    private PacketArenaUpdateSlots(final Arena arena, final int to) {
        this.arena = arena;
        this.to = to;
    }

    public Arena getArena() {
        return arena;
    }

    public int getTo() {
        return to;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaUpdateSlots.getID() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + to;
    }

    public static PacketArenaUpdateSlots build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 3) {
            final Arena arena = Util.getArena(Util.KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null && Util.isInteger(strs[2])) {
                return new PacketArenaUpdateSlots(arena, Integer.parseInt(strs[2]));
            }
            return null;
        }
        return null;
    }
}

