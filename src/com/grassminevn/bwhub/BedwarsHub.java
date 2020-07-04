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

import com.grassminevn.bwhub.bungeecord.JobManager;
import com.grassminevn.bwhub.config.Config;
import com.grassminevn.bwhub.config.LanguageConfig;
import com.grassminevn.bwhub.library.Vault;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BedwarsHub
extends JavaPlugin {
    public static Plugin plugin;

    public void onEnable() {
        plugin = this;
        final CommandExecutor cmd = new Command();
        getCommand("bw").setExecutor(cmd);
        getCommand("bedwars").setExecutor(cmd);
        getCommand("mbedwars").setExecutor(cmd);
        getCommand("bwbc").setExecutor(cmd);
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Vault.onEnable();
        LanguageConfig.load();
        Config.load();
        JobManager.onEnable();
    }

    public void onDisable() {
        JobManager.onDisable();
    }

    public static String getVersion() {
        return plugin.getDescription().getVersion();
    }

}

