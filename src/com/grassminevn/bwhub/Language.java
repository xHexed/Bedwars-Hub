/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.grassminevn.bwhub;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.ChatColor;

public enum Language {
    All_Commands(ChatColor.DARK_AQUA + "All commands:"),
    List_Arenas(ChatColor.YELLOW + "All arenas: " + ChatColor.GOLD),
    List_Arenas_None("none"),
    Info_MadeBy("Made by"),
    Info_Website("Website"),
    Info_Version("NMSHandler"),
    Unkown_Argument(ChatColor.RED + "Unkown argument " + ChatColor.DARK_RED + "{arg}"),
    Error_Occured(ChatColor.RED + "A error occured!"),
    No_Permissions(ChatColor.RED + "You've got no permissions!"),
    Sign_Lobby(ChatColor.YELLOW + "Waiting..."),
    Sign_Running(ChatColor.GOLD + "Running"),
    Sign_Stopped(ChatColor.RED + "Stopped"),
    Sign_Reseting(ChatColor.GREEN + "Reseting"),
    Sign_Offline(ChatColor.RED + "Offline"),
    Added_Sign(ChatColor.DARK_GREEN + "Sign " + ChatColor.GREEN + "has been successfully added!"),
    Removed_Sign(ChatColor.GOLD + "Removed sign"),
    NotLooking_AtSign( ChatColor.RED + "You're not looking at an sign!"),
    Usage("Usage"),
    OnlyAs_Player( ChatColor.RED + "This works only as an player!"),
    NotFound_Arena(ChatColor.RED + "There's no arena with the name " + ChatColor.DARK_RED + "{arena}" + ChatColor.RED + "!"),
    JoinMessage_stopped(ChatColor.RED + "This arena is currently stopped!"),
    JoinMessage_reseting(ChatColor.RED + "This arena is currently reseting itself!"),
    JoinMessage_full(ChatColor.RED + "This arena is currently full!"),
    JoinMessage_running(ChatColor.RED + "This arena is already running!"),
    JoinMessage_connecting(ChatColor.YELLOW + "Connecting to the server..."),
    Only_BetaMember(ChatColor.RED + "You are now allowed to join while bedwars is in the beta!"),
    Number_NotOne(ChatColor.DARK_RED + "{number} " + ChatColor.RED + "is not a number!"),
    Spawned_LobbyVillager( ChatColor.DARK_GREEN + "lobbyvillager " + ChatColor.GREEN + "has been successfully spawned!"),
    Arenas_Full(ChatColor.RED + "All arenas are full!");
    
    private final String selected_message;
    private static final Map<Language, String> translations;

    static {
        translations = new EnumMap<>(Language.class);
    }

    Language(final String msg) {
        selected_message = msg;
    }

    public String getMessage() {
        if (translations.containsKey(this)) {
            return translations.get(this);
        }
        return selected_message;
    }

    public static String chatColorToString(String str) {
        for (final ChatColor c : ChatColor.values()) {
            str = str.replace(String.valueOf(c), "&" + c.getChar());
        }
        return str;
    }

    public static String stringToChatColor(String str) {
        for (final ChatColor c : ChatColor.values()) {
            str = str.replace("&" + c.getChar(), String.valueOf(c));
        }
        return str;
    }

    public static Language getLanguage(final String str) {
        for (final Language l : Language.values()) {
            if (!l.name().equalsIgnoreCase(str) && !l.getMessage().equalsIgnoreCase(str)) continue;
            return l;
        }
        return null;
    }

    public static void setTranslation(final Language language, final String message) {
        if (language == null || message == null) {
            new NullPointerException().printStackTrace();
            return;
        }
        translations.put(language, message);
    }
}

