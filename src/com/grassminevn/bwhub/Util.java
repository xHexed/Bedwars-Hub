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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Util {
    private static final Random random = ThreadLocalRandom.current();
    public static boolean config_beta;
    public static String config_lobbyVillagerPrefix = ChatColor.GOLD + "Bedwars " + ChatColor.YELLOW;
    public static final boolean config_signAntispam = true;
    public static final double config_antispamDelay = 1.0;
    public static String config_subchannel = "lobby";
    public static final Map<String, Arena> arenas = new ConcurrentHashMap<>();

    public static void checkMainDirs() {
        final File dir = BedwarsHub.plugin.getDataFolder();
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void removeArena(final String arena) {
        System.out.println("Trying to remove arena " + arena);
        arenas.remove(arena);
        Events.updateView();
    }

    public static void addArena(final Arena arena) {
        if (arenas.containsKey(arena.getName())) return;
        System.out.println("Added arena " + arena.getName());
        arenas.put(arena.getName(), arena);
        Events.updateView();
    }

    public static Arena getArena(final String name) {
        if (name == null) {
            return null;
        }
        return arenas.get(name);
    }

    public static void autoJoin(final Player player, final String mode) {
        final ArrayList<Arena> goodArenas = new ArrayList<>();
        for (final Arena a : arenas.values()) {
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

}

