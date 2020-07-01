/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package com.grassminevn.bwhub.bungeecord;

import com.grassminevn.bwhub.Console;
import com.grassminevn.bwhub.Util;
import com.grassminevn.bwhub.bungeecord.in.*;
import com.grassminevn.bwhub.bungeecord.out.PacketChannelEnabled;

import java.net.DatagramPacket;
import java.util.Objects;

public class Communication {
    public static void onPacketReceived(final DatagramPacket bigPacket) {
        String msg = new String(bigPacket.getData());
        if ((msg = msg.substring(0, bigPacket.getLength())).length() >= 1) {
            final String[] strs = msg.split("/");
            if (Util.isInteger(strs[0])) {
                final PacketChannelExists packet;
                final PacketArenaUpdateTeamPlayers packet2;
                final Packet.PacketType type = Packet.PacketType.fromInt(strs[0]);
                if (type == Packet.PacketType.IN_PacketChannelExists && (packet = PacketChannelExists.build(msg)) != null) {
                    Channel channel;
                    if (Util.getChannel(packet.getChannelName()) != null) {
                        channel = Util.getChannel(packet.getChannelName());
                        if (bigPacket.getAddress().getHostAddress().equals(Objects.requireNonNull(channel).getInetAddress().getHostAddress())) {
                            Util.channels.remove(channel);
                        } else {
                            return;
                        }
                    }
                    channel = new Channel(packet.getChannelName(), bigPacket.getAddress(), bigPacket.getPort());
                    Util.channels.add(channel);
                    channel.sendPacket(new PacketChannelEnabled(Util.config_subchannel));
                    return;
                }
                final Channel channel = Util.getChannel(bigPacket.getAddress(), bigPacket.getPort());
                if (channel == null) {
                    return;
                }
                if (type == Packet.PacketType.IN_PacketArenaCreate) {
                    final PacketArenaCreate packet3 = PacketArenaCreate.build(msg, channel);
                    if (packet3 != null && Util.getArena(packet3.getArena().getName()) == null) {
                        Util.addArena(packet3.getArena());
                        Console.printBungeecordInfo("Registred arena '" + packet3.getArena().getName() + "' by channel '" + channel.getName() + "'");
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaRemove) {
                    final PacketArenaRemove packet4 = PacketArenaRemove.build(msg);
                    if (packet4 != null) {
                        Util.removeArena(packet4.getArena());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateSlots) {
                    final PacketArenaUpdateSlots packet5 = PacketArenaUpdateSlots.build(msg);
                    if (packet5 != null) {
                        packet5.getArena().setPlayers(packet5.getTo());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateMaxSlots) {
                    final PacketArenaUpdateMaxSlots packet6 = PacketArenaUpdateMaxSlots.build(msg);
                    if (packet6 != null) {
                        packet6.getArena().setMaxPlayers(packet6.getTo());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateIcon) {
                    final PacketArenaUpdateIcon packet7 = PacketArenaUpdateIcon.build(msg);
                    if (packet7 != null) {
                        packet7.getArena().setIcon(packet7.getTo());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateStatus) {
                    final PacketArenaUpdateStatus packet8 = PacketArenaUpdateStatus.build(msg);
                    if (packet8 != null) {
                        packet8.getArena().setStatus(packet8.getTo());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateName) {
                    final PacketArenaUpdateName packet9 = PacketArenaUpdateName.build(msg);
                    if (packet9 != null) {
                        packet9.getArena().setName(packet9.getTo());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateMadeBy) {
                    final PacketArenaUpdateMadeBy packet10 = PacketArenaUpdateMadeBy.build(msg);
                    if (packet10 != null) {
                        packet10.getArena().setMadeBy(packet10.getTo());
                    }
                } else if (type == Packet.PacketType.IN_PacketArenaUpdateTeamPlayers && (packet2 = PacketArenaUpdateTeamPlayers.build(msg)) != null) {
                    packet2.getArena().setPlayers(packet2.getTeams());
                    packet2.getArena().setInTeamPlayers(packet2.getInTeamPlayers());
                }
            } else {
                Console.printBungeecordWarn("Failed to open packet: Received a non-numeric packet");
            }
        }
    }
}

