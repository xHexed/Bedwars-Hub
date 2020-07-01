/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc.bungeecord.out;

import de.marcely.bwbc.Arena;
import de.marcely.bwbc.bungeecord.Packet;
import java.util.UUID;
import java.util.regex.Matcher;

import static de.marcely.bwbc.Util.SLASH;

public class PacketConnectPlayer
extends Packet {
    private final UUID uuid;
    private final Arena arena;

    public PacketConnectPlayer(final UUID uuid, final Arena arena) {
        this.uuid = uuid;
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public String getPacketString() {
        return PacketType.OUT_PacketConnectPlayer.getID() + "/" + uuid.toString() + "/" + SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;"));
    }
}

