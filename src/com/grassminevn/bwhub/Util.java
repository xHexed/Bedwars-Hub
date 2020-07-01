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

import com.grassminevn.bwhub.library.Vault;
import com.grassminevn.bwhub.bungeecord.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.InetAddress;
import java.util.*;
import java.util.regex.Pattern;

public class Util {
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
        Events.ArenaUpdate(true);
    }

    public static void addArena(final Arena arena) {
        arenas.add(arena);
        Events.ArenaUpdate(true);
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

    public static boolean hasPermission(final CommandSender sender, final Permission permission) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            final Boolean bl = Vault.hasPermission(player, permission.getPermission());
            if (bl != null) {
                return bl;
            }
            return player.hasPermission(permission.getPermission());
        }
        return true;
    }

}

