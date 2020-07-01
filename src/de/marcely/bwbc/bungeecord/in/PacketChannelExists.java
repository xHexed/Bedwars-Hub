/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc.bungeecord.in;

import de.marcely.bwbc.bungeecord.Packet;

public class PacketChannelExists
extends Packet {
    private final String channelName;

    private PacketChannelExists(final String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    @Override
    public String getPacketString() {
        return Packet.PacketType.IN_PacketChannelExists.getID() + "/" + channelName;
    }

    public static PacketChannelExists build(final String packetString) {
        final String[] strs = packetString.split("/");
        if (strs.length == 2) {
            return new PacketChannelExists(strs[1]);
        }
        return null;
    }
}

