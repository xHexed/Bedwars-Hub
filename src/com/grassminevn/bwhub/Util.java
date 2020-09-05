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

import com.grassminevn.bwhub.inventory.arena.ArenaUpdateHandler;
import com.grassminevn.levels.LevelsAPI;
import me.MathiasMC.PvPLevels.PvPLevelsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Util {
    public static boolean config_beta;
    public static String config_subchannel = "lobby";
    static final Map<String, Arena> arenas = new ConcurrentHashMap<>();

    public static void checkMainDirs() {
        final File dir = BedwarsHub.plugin.getDataFolder();
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    static boolean isInteger(final char number) {
        return '0' <= number && number <= '9';
    }

    public static void removeArena(final String arena) {
        System.out.println("Trying to remove arena " + arena);
        ArenaUpdateHandler.updateView(arenas.remove(arena));
    }

    public static Arena addArena(final String name,
                                 final String madeBy,
                                 final String maxPlayers,
                                 final String arenaStatus,
                                 final Integer players) {
        if (arenas.containsKey(name)) {
            final Arena arena = arenas.get(name);
            arena.setMaxPlayers(Integer.parseInt(maxPlayers));
            if (arenaStatus != null && !arena.getStatus().name().equals(arenaStatus))
                arena.setStatus(Arena.ArenaStatus.valueOf(arenaStatus));
            if (players != null && arena.getPlayers() != players) {
                arena.setPlayers(players);
            }
            return arena;
        }
        System.out.println("Added arena " + name + "(" + maxPlayers + ")");
        final Arena arena = new Arena(name, madeBy, Integer.parseInt(maxPlayers));
        if (arenaStatus != null)
            arena.setStatus(Arena.ArenaStatus.valueOf(arenaStatus));
        arenas.put(name, arena);
        ArenaUpdateHandler.updateView(arena);
        return arena;
    }

    public static Arena getArena(final String name) {
        if (name == null) {
            return null;
        }
        return arenas.get(name);
    }

    static void autoJoin(final Player player, final String mode) {
        autoJoin(player, arenas.values(), mode);
    }

    public static void autoJoin(final Player player, final Iterable<Arena> arenas, final String fillter) {
        final ArrayList<Arena> goodArenas = new ArrayList<>();
        for (final Arena a : arenas) {
            if (a == null || !a.getName().startsWith(fillter) || !a.canAutoJoin()) continue;
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
            connect(player, goodArenas.get(ThreadLocalRandom.current().nextInt(goodArenas.size())));
        } else {
            player.sendMessage(Language.Arenas_Full.toString());
        }
    }

    public static void connect(final Player player, final Arena arena) {
        player.sendMessage(Language.JoinMessage_connecting.toString());
        Bukkit.getScheduler().runTaskAsynchronously(BedwarsHub.plugin, () -> {
            PvPLevelsAPI.api.syncSave(player.getUniqueId().toString());
            LevelsAPI.api.syncSave(player.getUniqueId().toString());
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
        });
    }

}

