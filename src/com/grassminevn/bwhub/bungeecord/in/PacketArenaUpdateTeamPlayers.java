/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.bungeecord.in;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Packet;

import java.util.regex.Matcher;

public class PacketArenaUpdateTeamPlayers
extends Packet {
    private final Arena arena;
    private final int teams;
    private final int inTeamPlayers;

    private PacketArenaUpdateTeamPlayers(final Arena arena, final int teams, final int inTeamPlayers) {
        this.arena = arena;
        this.teams = teams;
        this.inTeamPlayers = inTeamPlayers;
    }

    public Arena getArena() {
        return arena;
    }

    public int getTeams() {
        return teams;
    }

    public int getInTeamPlayers() {
        return inTeamPlayers;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaUpdateTeamPlayers.getID() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + teams;
    }

    public static PacketArenaUpdateTeamPlayers build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 4) {
            final Arena arena = Util.getArena(Util.KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null && Util.isInteger(strs[2]) && Util.isInteger(strs[3])) {
                return new PacketArenaUpdateTeamPlayers(arena, Integer.parseInt(strs[2]), Integer.parseInt(strs[3]));
            }
            return null;
        }
        return null;
    }
}

