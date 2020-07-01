/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.bungeecord.in;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Packet;

import java.util.regex.Matcher;

public class PacketArenaUpdateStatus
extends Packet {
    private final Arena arena;
    private final Arena.ArenaStatus to;

    private PacketArenaUpdateStatus(final Arena arena, final Arena.ArenaStatus to) {
        this.arena = arena;
        this.to = to;
    }

    public Arena getArena() {
        return arena;
    }

    public Arena.ArenaStatus getTo() {
        return to;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaUpdateStatus.getID() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + to.getID();
    }

    public static PacketArenaUpdateStatus build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 3) {
            final Arena arena = Util.getArena(Util.KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null && Util.isInteger(strs[2])) {
                final Arena.ArenaStatus status = Arena.ArenaStatus.fromInt(strs[2]);
                return new PacketArenaUpdateStatus(arena, status);
            }
            return null;
        }
        return null;
    }
}

