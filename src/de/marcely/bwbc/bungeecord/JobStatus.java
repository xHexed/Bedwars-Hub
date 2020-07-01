/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc.bungeecord;

public class JobStatus {
    private final Packet packet;
    private final Channel channel;

    public JobStatus(final Packet packet, final Channel channel) {
        this.packet = packet;
        this.channel = channel;
    }

    public Packet getPacket() {
        return packet;
    }

    public Channel getChannel() {
        return channel;
    }

    public void done() {
    }
}

