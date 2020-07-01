/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.bungeecord.in;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Packet;

import java.util.regex.Matcher;

public class PacketArenaRemove
extends Packet {
    private final Arena arena;

    private PacketArenaRemove(final Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaRemove.getID() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;"));
    }

    public static PacketArenaRemove build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 2) {
            final Arena arena = Util.getArena(Util.KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null) {
                return new PacketArenaRemove(arena);
            }
            return null;
        }
        return null;
    }
}

