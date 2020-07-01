/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.bungeecord.in;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Packet;

import java.util.regex.Matcher;

public class PacketArenaUpdateMadeBy
extends Packet {
    private final Arena arena;
    private final String to;

    private PacketArenaUpdateMadeBy(final Arena arena, final String to) {
        this.arena = arena;
        this.to = to;
    }

    public Arena getArena() {
        return arena;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaUpdateMadeBy.getID() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + Util.SLASH.matcher(to).replaceAll(Matcher.quoteReplacement("&sKEYslash;"));
    }

    public static PacketArenaUpdateMadeBy build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 3) {
            final Arena arena = Util.getArena(Util.KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null) {
                return new PacketArenaUpdateMadeBy(arena, Util.KEYSLASH.matcher(strs[2]).replaceAll(Matcher.quoteReplacement("/")));
            }
            return null;
        }
        return null;
    }
}

