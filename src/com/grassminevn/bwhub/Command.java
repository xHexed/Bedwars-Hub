/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 */
package com.grassminevn.bwhub;

import com.grassminevn.bwhub.config.Config;
import com.grassminevn.bwhub.config.LanguageConfig;
import com.grassminevn.bwhub.inventory.arena.ArenaMenuHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command
implements CommandExecutor {
    private static final Pattern ARENA = Pattern.compile("\\{arena}");

    public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
        if (cmd(sender, label, args)) return true;
        final Collection<String> cmds = new ArrayList<>();
        addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " join <arena>");
        addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " auto [solo/duo/squad]");
        addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " menu <solo/duo/squad>");
        addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " list");
        addIfHasPermission(cmds, sender, Permission.Command_Info, "/" + label + " info <arena>");
        addIfHasPermission(cmds, sender, Permission.Reload, "/" + label + " reload");
        if (cmds.size() >= 1) {
            sender.sendMessage(Language.All_Commands.toString());
            for (final String usage : cmds) {
                sender.sendMessage(ChatColor.AQUA + usage);
            }
        } else {
            sender.sendMessage(Language.No_Permissions.toString());
        }
        return true;
    }

    private boolean cmd(final CommandSender sender, final String label, final String[] args) {
        if (args.length < 1) return false;
        switch (args[0].toLowerCase()) {
            case "join": {
                if (args.length >= 2) {
                    if (sender instanceof Player) {
                        final Player player = (Player)sender;
                        final Arena arena = Util.getArena(args[1]);
                        if (arena != null) {
                            Util.connect(player, arena);
                        } else {
                            player.sendMessage(ARENA.matcher(Language.NotFound_Arena.toString()).replaceAll(Matcher.quoteReplacement(args[1])));
                        }
                    } else {
                        sender.sendMessage(Language.OnlyAs_Player.toString());
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + Language.Usage.toString() + ": " + ChatColor.YELLOW + "/" + label + " join <arena name>");
                }
                return true;
            }
            case "auto": {
                if (sender instanceof Player) {
                    if (args.length >= 2)
                        Util.autoJoin((Player) sender, args[1]);
                    else
                        Util.autoJoin((Player) sender, Util.arenas.values(), "");
                } else {
                    sender.sendMessage(ChatColor.GOLD + Language.Usage.toString() + ": " + ChatColor.YELLOW + "/" + label + " join <arena name>");
                }
                return true;
            }
            case "list": {
                final StringBuilder str = new StringBuilder();
                int i = 1;
                for (final Arena a : Util.arenas.values()) {
                    if (i != Util.arenas.size()) {
                        str.append(a.getName()).append(", ");
                        continue;
                    }
                    str.append(a.getName());
                    ++i;
                }
                if (str.length() == 0) {
                    sender.sendMessage(Language.List_Arenas + Language.List_Arenas_None.toString());
                } else {
                    sender.sendMessage(Language.List_Arenas.toString() + str);
                }
                return true;
            }
            case "menu":
                if (!(sender instanceof Player))
                    return false;
                if (args.length >= 2) {
                    switch (args[1].toLowerCase()) {
                        case "solo":
                            ((HumanEntity) sender).openInventory(ArenaMenuHandler.getSoloArenaMenu().getInventory());
                            break;
                        case "duo":
                            ((HumanEntity) sender).openInventory(ArenaMenuHandler.getDuoArenaMenu().getInventory());
                            break;
                        case "squad":
                            ((HumanEntity) sender).openInventory(ArenaMenuHandler.getSquadArenaMenu().getInventory());
                            break;
                    }
                    return true;
                }
                return false;
            case "reload": {
                LanguageConfig.load();
                Config.load();
                sender.sendMessage("Reload");
                return true;
            }
            case "info": {
                if (args.length < 2) return false;
                final Arena arena = Util.getArena(args[1].toLowerCase());
                if (arena == null) return false;
                sender.sendMessage(arena.getName() + ": " + arena.getStatus().name() + " " + arena.getPlayers() + "/" + arena.getMaxPlayers());
                return true;
            }
        }
        return false;
    }

    private void addIfHasPermission(final Collection<String> cmds, final Permissible sender, final Permission perm, final String usage) {
        if (sender.hasPermission(perm.getPermission())) {
            cmds.add(usage);
        }
    }
}

