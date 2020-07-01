/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package de.marcely.bwbc.bungeecord.in;

import de.marcely.bwbc.Arena;
import de.marcely.bwbc.Console;
import de.marcely.bwbc.Util;
import de.marcely.bwbc.bungeecord.Packet;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PacketArenaUpdateIcon
extends Packet {
    private static final Pattern SLASH = Pattern.compile("/", Pattern.LITERAL);
    private static final Pattern KEYSLASH = Pattern.compile("&sKEYslash;", Pattern.LITERAL);
    private final Arena arena;
    private final ItemStack to;

    private PacketArenaUpdateIcon(final Arena arena, final ItemStack to) {
        this.arena = arena;
        this.to = to;
    }

    public Arena getArena() {
        return arena;
    }

    public ItemStack getTo() {
        return to;
    }

    @Override
    public String getPacketString() {
        return PacketType.IN_PacketArenaUpdateMaxSlots.getID() + "/" + SLASH.matcher(arena.getName()).replaceAll(Matcher.quoteReplacement("&sKEYslash;")) + "/" + to.getType().name() + ":" + to.getDurability();
    }

    public static PacketArenaUpdateIcon build(final String str) {
        final String[] strs = str.split("/");
        if (strs.length == 3) {
            try {
                final Arena arena = Util.getArena(KEYSLASH.matcher(strs[1]).replaceAll(Matcher.quoteReplacement("/")));
                final String[] strs2 = strs[2].split(":");
                final int i = Integer.parseInt(strs2[1]);
                final ItemStack is = new ItemStack(Material.getMaterial(strs2[0]), 1, (short)i);
                if (arena != null) {
                    return new PacketArenaUpdateIcon(arena, is);
                }
            }
            catch (final Exception e) {
                Console.printBungeecordWarn("Wrong packet format:");
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}

