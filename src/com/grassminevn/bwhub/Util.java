/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Sign
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.grassminevn.bwhub;

import com.grassminevn.bwhub.bungeecord.Channel;
import com.grassminevn.bwhub.bungeecord.JobManager;
import com.grassminevn.bwhub.bungeecord.JobStatus;
import com.grassminevn.bwhub.bungeecord.Packet;
import com.grassminevn.bwhub.bungeecord.out.PacketConnectPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Util {
    private static final Random random = ThreadLocalRandom.current();
    public static final Pattern SLASH = Pattern.compile("/", Pattern.LITERAL);
    public static final Pattern KEYSLASH = Pattern.compile("&sKEYslash;", Pattern.LITERAL);
    public static boolean config_beta;
    public static String config_lobbyVillagerPrefix = ChatColor.GOLD + "Bedwars " + ChatColor.YELLOW;
    public static final boolean config_spectator = true;
    public static final boolean config_signAntispam = true;
    public static final double config_antispamDelay = 1.0;
    public static String config_subchannel = "lobby";
    public static final Collection<Channel> channels = new ArrayList<>();
    public static final Collection<Arena> arenas = new ArrayList<>();

    public static void checkMainDirs() {
        final File dir = BedwarsHub.plugin.getDataFolder();
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void removeArena(final Arena arena) {
        arenas.remove(arena);
        Events.updateView();
    }

    public static void addArena(final Arena arena) {
        arenas.add(arena);
        Events.updateView();
    }

    public static Arena getArena(final String name) {
        if (name == null) {
            return null;
        }
        for (final Arena arena : arenas) {
            if (!arena.getName().equalsIgnoreCase(name)) continue;
            return arena;
        }
        return null;
    }

    public static Channel getChannel(final String name) {
        for (final Channel channel : channels) {
            if (!channel.getName().equalsIgnoreCase(name)) continue;
            return channel;
        }
        return null;
    }

    public static Channel getChannel(final InetAddress address, final int port) {
        for (final Channel channel : channels) {
            if (!channel.getInetAddress().getHostAddress().equals(address.getHostAddress()) || channel.getPort() != port) continue;
            return channel;
        }
        return null;
    }

    public static boolean isInteger(final String str) {
        try {
            Integer.valueOf(str);
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }

    public static boolean hasPermission(final Permissible sender, final Permission permission) {
        if (sender instanceof Player) {
            return hasPermission(sender, permission.getPermission());
        }
        return true;
    }

    public static void autoJoin(final Player player, final String mode) {
        final ArrayList<Arena> goodArenas = new ArrayList<>();
        for (final Arena a : Util.arenas) {
            if (a.hideFromAutoSign()) continue;
            if (!a.getName().startsWith(mode)) continue;
            if (goodArenas.isEmpty()) {
                goodArenas.add(a);
                continue;
            }
            if (a.getPlayers() > goodArenas.get(0).getPlayers()) {
                goodArenas.clear();
                goodArenas.add(a);
                continue;
            }
            if (a.getPlayers() != goodArenas.get(0).getPlayers()) continue;
            goodArenas.add(a);
        }
        if (!goodArenas.isEmpty()) {
            connect(player, goodArenas.get(random.nextInt(goodArenas.size())));
        } else {
            player.sendMessage(Language.Arenas_Full.getMessage());
        }
    }

    public static void connect(final Player player, final Arena arena) {
        player.sendMessage(Language.JoinMessage_connecting.getMessage());
        sendPacket(new PacketConnectPlayer(player.getUniqueId(), arena), arena.getChannel());
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bytes);
        try {
            out.writeUTF("Connect");
            out.writeUTF(arena.getName());
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

    public static void sendPacket(final Packet packet, final Channel channel) {
        JobManager.addJob(new JobStatus(packet, channel));
    }

    private static Boolean hasPermission(final Permissible player, final String perm) {
        return player.hasPermission(perm);
    }
}

