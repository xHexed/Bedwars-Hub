/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package de.marcely.bwbc;

import org.bukkit.Bukkit;

public class Console {
    public static void printBungeecordWarn(final String info) {
        Bukkit.getLogger().warning("[MBedwars-BungeeCord] " + info);
    }

    public static void printBungeecordInfo(final String info) {
        Bukkit.getLogger().info("[MBedwars-BungeeCord] " + info);
    }
}

