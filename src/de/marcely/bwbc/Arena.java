/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package de.marcely.bwbc;

import de.marcely.bwbc.bungeecord.Channel;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arena {
    private static final Pattern INGAME = Pattern.compile("\\{ingame}", Pattern.LITERAL);
    private static final Pattern MAX = Pattern.compile("\\{max}", Pattern.LITERAL);
    private final Channel channel;
    private String name;
    private String madeBy;
    private int players;
    private int maxPlayers;
    private final int teams;
    private int inTeamPlayers;
    private ItemStack icon;
    private ArenaStatus status;

    public Arena(final Channel channel, final String name, final String madeBy, final int players, final int maxPlayers, final ItemStack icon, final ArenaStatus status, final int teams, final int inTeamPlayers) {
        this.channel = channel;
        this.name = name;
        this.madeBy = madeBy;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.icon = icon;
        this.status = status;
        this.teams = teams;
        this.inTeamPlayers = inTeamPlayers;
    }

    public void setName(final String name) {
        this.name = name;
        Events.ArenaUpdate();
    }

    public void setMadeBy(final String madeBy) {
        this.madeBy = madeBy;
        Events.ArenaUpdate();
    }

    public void setPlayers(final int players) {
        this.players = players;
        Events.ArenaUpdate();
    }

    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
        Events.ArenaUpdate();
    }

    public void setIcon(final ItemStack icon) {
        this.icon = icon;
        Events.ArenaUpdate();
    }

    public void setStatus(final ArenaStatus status) {
        this.status = status;
        Events.ArenaUpdate();
    }

    public void setInTeamPlayers(final int inTeamPlayers) {
        this.inTeamPlayers = inTeamPlayers;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getName() {
        return name;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public int getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public int getTeams() {
        return teams;
    }

    public int getInTeamPlayers() {
        return inTeamPlayers;
    }

    public List<String> getLore() {
        final List<String> list = new ArrayList<>();
        int i = 0;
        String madeBy = "";
        final String[] madeByList = this.madeBy.split(",");
        for (final String str : this.madeBy.split(",")) {
            if (i == 0) {
                if (madeByList.length == 1) {
                    list.add(ChatColor.DARK_AQUA + Language.Info_MadeBy.getMessage() + ": " + ChatColor.AQUA + str);
                } else {
                    list.add(ChatColor.DARK_AQUA + Language.Info_MadeBy.getMessage() + ": " + ChatColor.AQUA + str + ", ");
                }
            } else {
                madeBy = i == madeByList.length - 1 ? madeBy + str : madeBy + str + ", ";
            }
            if (madeBy.split(",").length >= 4) {
                list.add(ChatColor.AQUA + madeBy);
                madeBy = "";
            }
            ++i;
        }
        list.add(ChatColor.DARK_BLUE + "-----------");
        if (status == ArenaStatus.Running) {
            list.add(Language.Sign_Running.getMessage());
        } else if (status == ArenaStatus.Stopped) {
            list.add(Language.Sign_Stopped.getMessage());
        } else if (status == ArenaStatus.Lobby) {
            list.add(MAX.matcher(INGAME.matcher(Language.Sign_Lobby.getMessage()).replaceAll(Matcher.quoteReplacement(String.valueOf(players)))).replaceAll(Matcher.quoteReplacement(String.valueOf(maxPlayers))));
        } else if (status == ArenaStatus.Reseting || status == ArenaStatus.EndLobby) {
            list.add(Language.Sign_Reseting.getMessage());
        }
        return list;
    }

    public boolean hideFromAutoSign() {
        return players >= maxPlayers || status != ArenaStatus.Lobby;
    }

    public enum ArenaStatus {
        Stopped(1),
        Lobby(2),
        Running(3),
        Reseting(4),
        EndLobby(5);
        
        private final int selected_id;

        ArenaStatus(final int id) {
            selected_id = id;
        }

        public int getID() {
            return selected_id;
        }
    }

}

