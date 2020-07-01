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

public class PacketArenaUpdateName
extends Packet {
    private final Arena arena;
    private final String to;

    private PacketArenaUpdateName(final Arena arena, final String to) {
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
        return PacketType.IN_PacketArenaUpdateName.getID() + "/" + SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + SLASH.matcher(to).replaceAll(Matcher.quoteReplacement("&sKEYslash;"));
    }

    public static PacketArenaUpdateName build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 3) {
            final Arena arena = Util.getArena(KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null) {
                return new PacketArenaUpdateName(arena, KEYSLASH.matcher(strs[2]).replaceAll(Matcher.quoteReplacement("/")));
            }
            return null;
        }
        return null;
    }
}

