/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.plugin.Plugin
 */
package de.marcely.bwbc.config;

import de.marcely.bwbc.BedwarsHub;
import de.marcely.bwbc.Util;
import org.bukkit.ChatColor;

public class Config {
    private static final ConfigManager cm = new ConfigManager(BedwarsHub.plugin.getName(), "/config.yml");

    public static void load() {
        Util.checkMainDirs();
        cm.load();
        final String config_version = cm.getConfigString("config-NMSHandler");
        final Boolean config_beta = cm.getConfigBoolean("beta");
        final String config_lobbyvillagerprefix = cm.getConfigString("lobbyvillager-prefix");
        final String config_bungeecordsubchannel = cm.getConfigString("bungeecord-subchannel");
        if (config_beta != null) {
            Util.config_beta = config_beta;
        }
        if (config_lobbyvillagerprefix != null) {
            Util.config_lobbyVillagerPrefix = Config.replaceStringChatColor(config_lobbyvillagerprefix);
        }
        if (config_bungeecordsubchannel != null) {
            Util.config_subchannel = config_bungeecordsubchannel;
        }
        if (config_version == null || !config_version.equals(BedwarsHub.getVersion())) {
            Config.save();
        }
    }

    private static void save() {
        Util.checkMainDirs();
        cm.clear();
        cm.addComment("Don't change this!");
        cm.addConfig("config-NMSHandler", BedwarsHub.getVersion());
        cm.addEmptyLine();
        cm.addComment("If this config is enabled, only people with the permission 'mbedwars.beta' can join");
        cm.addConfig("beta", Util.config_beta);
        cm.addEmptyLine();
        cm.addComment("The prefix by the lobby-villager you can spawn with /bwbc summon lobbyvillager");
        cm.addConfig("lobbyvillager_prefix", Config.replaceChatColorString(Util.config_lobbyVillagerPrefix));
        cm.addEmptyLine();
        cm.addComment("The subchannel name of this server");
        cm.addConfig("bungeecord-subchannel", Util.config_subchannel);
        cm.save();
    }

    private static String replaceStringChatColor(String str) {
        for (final ChatColor color : ChatColor.values()) {
            str = str.replace("&" + color.getChar(), (CharSequence) color);
        }
        return str;
    }

    private static String replaceChatColorString(String str) {
        for (final ChatColor color : ChatColor.values()) {
            str = str.replace((CharSequence) color, "&" + color.getChar());
        }
        return str;
    }
}

