/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  com.google.common.io.ByteArrayDataOutput
 *  com.google.common.io.ByteStreams
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.messaging.Messenger
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.bukkit.plugin.messaging.PluginMessageListenerRegistration
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package de.marcely.bwbc.bungeecord;

import de.marcely.bwbc.Console;
import de.marcely.bwbc.Util;
import de.marcely.bwbc.*;
import de.marcely.bwbc.bungeecord.out.PacketConnectPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Channel
implements PluginMessageListener {
    private final String name;
    private String ip;
    private int port;
    private InetAddress address;

    public Channel(final String name, final InetAddress address, final int port) {
        this.name = name;
        ip        = address.getHostAddress();
        this.port = port;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public boolean isRegistred() {
        return ip != null;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return address;
    }

    public void onPluginMessageReceived(final String channel, final Player player, final byte[] bytes) {
        if (channel.equals("BungeeCord")) {
            final DataInput in = new DataInputStream(new ByteArrayInputStream(bytes));
            String type = "";
            String subchannel = "";
            try {
                type = in.readUTF();
                subchannel = in.readUTF();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            if (type.equals("ServerIP") && subchannel.equals(name)) {
                try {
                    ip   = in.readUTF();
                    port = in.readShort();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
                try {
                    address = InetAddress.getByName(ip);
                }
                catch (final UnknownHostException e) {
                    Console.printBungeecordWarn("Failed to register channel InetAddress:");
                    e.printStackTrace();
                }
                Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(BedwarsHub.plugin, "BungeeCord", this);
            }
        }
    }

    public void sendPacket(final Packet packet) {
        JobManager.addJob(new JobStatus(packet, this));
    }

    public void Connect(final Player player, final Arena arena) {
        if (arena.getStatus() == Arena.ArenaStatus.Lobby) {
            if (arena.getPlayers() < arena.getMaxPlayers()) {
                connect(player, arena);
            } else {
                player.sendMessage(Language.JoinMessage_full.getMessage());
            }
        } else if (arena.getStatus() == Arena.ArenaStatus.Running) {
            if (Util.config_spectator) {
                connect(player, arena);
            } else {
                player.sendMessage(Language.JoinMessage_running.getMessage());
            }
        } else if (arena.getStatus() == Arena.ArenaStatus.Reseting) {
            player.sendMessage(Language.JoinMessage_reseting.getMessage());
        } else if (arena.getStatus() == Arena.ArenaStatus.Stopped) {
            player.sendMessage(Language.JoinMessage_stopped.getMessage());
        }
    }

    private void connect(final Player player, final Arena arena) {
        player.sendMessage(Language.JoinMessage_connecting.getMessage());
        sendPacket(new PacketConnectPlayer(player.getUniqueId(), arena));
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bytes);
        try {
            out.writeUTF("Connect");
            out.writeUTF(name);
            player.sendPluginMessage(BedwarsHub.plugin, "BungeeCord", bytes.toByteArray());
            out.flush();
            bytes.flush();
            out.close();
            bytes.close();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

}

