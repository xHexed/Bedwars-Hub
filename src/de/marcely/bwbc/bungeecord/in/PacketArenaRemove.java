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
        return PacketType.IN_PacketArenaRemove.getID() + "/" + SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;"));
    }

    public static PacketArenaRemove build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 2) {
            final Arena arena = Util.getArena(KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null) {
                return new PacketArenaRemove(arena);
            }
            return null;
        }
        return null;
    }
}

