/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package com.grassminevn.bwhub;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private final String name;
    private final String madeBy;
    private int players;
    private final int maxPlayers;
    private ArenaStatus status = ArenaStatus.Lobby;

    public Arena(final String name, final String madeBy, final int maxPlayers) {
        this.name = name;
        this.madeBy = madeBy;
        this.maxPlayers = maxPlayers;
    }

    public void setPlayers(final int players) {
        this.players = players;
        Events.updateView();
    }

    public void setStatus(final ArenaStatus status) {
        this.status = status;
        System.out.println("Updated " + name + " status to " + status.name());
        Events.updateView();
    }

    public String getName() {
        return name;
    }

    public int getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public List<String> getLore() {
        final List<String> list = new ArrayList<>();
        list.add("§fMap: " + madeBy);
        if (status == ArenaStatus.Running) {
            list.add(Language.Sign_Running.getMessage());
        } else if (status == ArenaStatus.Stopped) {
            list.add(Language.Sign_Stopped.getMessage());
        } else if (status == ArenaStatus.Lobby) {
            list.add(Language.Sign_Lobby.getMessage());
        } else if (status == ArenaStatus.Reseting || status == ArenaStatus.EndLobby) {
            list.add(Language.Sign_Reseting.getMessage());
        }
        list.add("§fNgười chơi: " + players + "/" + maxPlayers);
        return list;
    }

    public boolean hideFromAutoSign() {
        return players >= maxPlayers || status != ArenaStatus.Lobby;
    }

    public enum ArenaStatus {
        Stopped(),
        Lobby(),
        Running(),
        Reseting(),
        EndLobby()
    }
}

