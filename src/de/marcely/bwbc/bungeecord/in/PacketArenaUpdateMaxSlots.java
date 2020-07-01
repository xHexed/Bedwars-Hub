/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc.bungeecord.in;

import de.marcely.bwbc.Arena;
import de.marcely.bwbc.Util;
import de.marcely.bwbc.bungeecord.Packet;

import java.util.regex.Matcher;

import static de.marcely.bwbc.Util.KEYSLASH;
import static de.marcely.bwbc.Util.SLASH;

public class PacketArenaUpdateMaxSlots
extends Packet {
    private final Arena arena;
    private final int to;

    private PacketArenaUpdateMaxSlots(final Arena arena, final int to) {
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
        return PacketType.IN_PacketArenaUpdateMaxSlots.getID() + "/" + SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + to;
    }

    public static PacketArenaUpdateMaxSlots build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 3) {
            final Arena arena = Util.getArena(KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null && Util.isInteger(strs[2])) {
                return new PacketArenaUpdateMaxSlots(arena, Integer.parseInt(strs[2]));
            }
            return null;
        }
        return null;
    }
}

