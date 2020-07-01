/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package com.grassminevn.bwhub.config;

import com.grassminevn.bwhub.BedwarsHub;
import com.grassminevn.bwhub.Language;

import java.util.Map;

public class LanguageConfig {
    private static final ConfigManager cm = new ConfigManager(BedwarsHub.plugin.getName(), "messages.yml", false);

    public static void load() {
        if (cm.exists()) {
            cm.load();
            for (final Map.Entry entry : cm.getInside(0).entrySet()) {
                final String key = (String)entry.getKey();
                final String value = (String)entry.getValue();
                final Language l = Language.getLanguage(key);
                if (l == null) continue;
                Language.setTranslation(l, Language.stringToChatColor(value));
            }
        }
        LanguageConfig.save();
    }

    private static void save() {
        cm.clear();
        for (final Language l : Language.values()) {
            cm.addConfig(l.name(), Language.chatColorToString(l.getMessage()));
        }
        cm.save();
    }
}

