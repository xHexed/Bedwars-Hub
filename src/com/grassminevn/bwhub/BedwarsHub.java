/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.Messenger
 */
package com.grassminevn.bwhub;

import com.grassminevn.bwhub.bungeecord.Communication;
import com.grassminevn.bwhub.config.Config;
import com.grassminevn.bwhub.config.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalTime;

public class BedwarsHub
extends JavaPlugin {
    public static Plugin plugin;
    private static ServerSocket socket;

    public void onEnable() {
        plugin = this;
        final CommandExecutor cmd = new Command();
        getCommand("bw").setExecutor(cmd);
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        LanguageConfig.load();
        Config.load();

        try {
            socket = new ServerSocket(2);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        new Thread("BedwarsSocketHandler") {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    try (final DataInputStream dis = new DataInputStream(socket.accept().getInputStream())) {
                        final String data = dis.readUTF();
                        Communication.onPacketReceived(data);
                    }
                    catch (final Exception e) {
                        if (!e.toString().contains("CancelledPacketHandleException"))
                            e.printStackTrace();
                        run();
                    }
                }
            }
        }.start();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            final World world = Bukkit.getWorld("world");
            if (world == null) return;
            final LocalTime date = LocalTime.now();
            int time = (int) ((date.getHour() + ((double) date.getMinute() / 60) - 6) * 1000);
            if (time < 0) time += 24000;
            world.setTime(time);
        }, 0, 20);
    }

    public void onDisable() {
        try {
            socket.close();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static String getVersion() {
        return plugin.getDescription().getVersion();
    }

}

