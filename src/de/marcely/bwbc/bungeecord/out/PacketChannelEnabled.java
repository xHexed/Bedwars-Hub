/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc.bungeecord.out;

import de.marcely.bwbc.bungeecord.Packet;

public class PacketChannelEnabled
extends Packet {
    private final String channelName;

    public PacketChannelEnabled(final String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getPacketString() {
        return Packet.PacketType.OUT_PacketChannelEnabled.getID() + "/" + channelName;
    }

    public static PacketChannelEnabled build(final String packetString) {
        final String[] strs = packetString.split("/");
        if (strs.length == 2) {
            return new PacketChannelEnabled(strs[1]);
        }
        return null;
    }
}

