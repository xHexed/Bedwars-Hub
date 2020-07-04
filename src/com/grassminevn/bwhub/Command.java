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

import com.grassminevn.bwhub.inventory.SelectorMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command
implements CommandExecutor {
    private static final Pattern ARENA = Pattern.compile("\\{arena}", Pattern.LITERAL);

    public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
        if (!cmd(sender, label, args)) {
            final Collection<String> cmds = new ArrayList<>();
            addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " join <arena name>");
            addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " auto");
            addIfHasPermission(cmds, sender, Permission.Command_List, "/" + label + " list");
            addIfHasPermission(cmds, sender, Permission.Command_Info, "/" + label + " info");
            if (cmds.size() >= 1) {
                sender.sendMessage(Language.All_Commands.getMessage());
                for (final String usage : cmds) {
                    sender.sendMessage(ChatColor.AQUA + usage);
                }
            } else {
                sender.sendMessage(Language.No_Permissions.getMessage());
            }
        }
        return true;
    }

    private boolean cmd(final CommandSender sender, final String label, final String[] args) {
        if (args.length >= 1) {
            final String cmd = args[0];
            switch (cmd.toLowerCase()) {
                case "join": {
                    if (args.length >= 2) {
                        if (sender instanceof Player) {
                            final Player player = (Player)sender;
                            final Arena arena = Util.getArena(args[1]);
                            if (arena != null) {
                                arena.getChannel().connect(player, arena);
                            } else {
                                player.sendMessage(ARENA.matcher(Language.NotFound_Arena.getMessage()).replaceAll(Matcher.quoteReplacement(args[1])));
                            }
                        } else {
                            sender.sendMessage(Language.OnlyAs_Player.getMessage());
                        }
                    } else {
                        sender.sendMessage(ChatColor.GOLD + Language.Usage.getMessage() + ": " + ChatColor.YELLOW + "/" + label + " join <arena name>");
                    }
                    return true;
                }
                case "autojoin": {
                    if (sender instanceof Player) {
                        Util.autoJoin((Player) sender, "");
                    } else {
                        sender.sendMessage(ChatColor.GOLD + Language.Usage.getMessage() + ": " + ChatColor.YELLOW + "/" + label + " join <arena name>");
                    }
                    return true;
                }
                case "list": {
                    final StringBuilder str = new StringBuilder();
                    int i = 1;
                    for (final Arena a : Util.arenas) {
                        if (i != Util.arenas.size()) {
                            str.append(a.getName()).append(", ");
                            continue;
                        }
                        str.append(a.getName());
                        ++i;
                    }
                    if (str.length() == 0) {
                        sender.sendMessage(Language.List_Arenas.getMessage() + Language.List_Arenas_None.getMessage());
                    } else {
                        sender.sendMessage(Language.List_Arenas.getMessage() + str);
                    }
                    return true;
                }
                case "menu": {
                    if (!(sender instanceof Player)) return false;
                    ((HumanEntity) sender).openInventory(new SelectorMenu().getInventory());
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void addIfHasPermission(final Collection<String> cmds, final CommandSender sender, final Permission perm, final String usage) {
        if (Util.hasPermission(sender, perm)) {
            cmds.add(usage);
        }
    }
}

