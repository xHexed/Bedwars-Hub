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
package de.marcely.bwbc;

import de.marcely.bwbc.bungeecord.JobManager;
import de.marcely.bwbc.config.Config;
import de.marcely.bwbc.config.LanguageConfig;
import de.marcely.bwbc.library.Vault;
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

    public static String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    public static String getWebsite() {
        return plugin.getDescription().getWebsite();
    }

    public static String getVersion() {
        return plugin.getDescription().getVersion();
    }

}

