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
        return PacketType.IN_PacketArenaUpdateTeamPlayers.getID() + "/" + SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + teams;
    }

    public static PacketArenaUpdateTeamPlayers build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 4) {
            final Arena arena = Util.getArena(KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
            if (arena != null && Util.isInteger(strs[2]) && Util.isInteger(strs[3])) {
                return new PacketArenaUpdateTeamPlayers(arena, Integer.parseInt(strs[2]), Integer.parseInt(strs[3]));
            }
            return null;
        }
        return null;
    }
}

