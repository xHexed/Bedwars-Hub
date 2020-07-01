/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.chat.Chat
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicesManager
 */
package com.grassminevn.bwhub.library;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Vault {
    private static Permission permission;

    public static void onEnable() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            if (Bukkit.getServer().getServicesManager().getRegistration(Permission.class) != null) {
                permission = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
            }
        }
    }

    public static Boolean hasPermission(final Player player, final String perm) {
        if (permission != null && permission.isEnabled()) {
            return permission.has(player, perm);
        }
        return null;
    }
}

