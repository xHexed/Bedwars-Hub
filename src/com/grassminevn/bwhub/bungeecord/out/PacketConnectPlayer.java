/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub.bungeecord.out;

import com.grassminevn.bwhub.Arena;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.Packet;
import java.util.UUID;
import java.util.regex.Matcher;

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
        return PacketType.OUT_PacketConnectPlayer.getID() + "/" + uuid.toString() + "/" + Util.SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;"));
    }
}

