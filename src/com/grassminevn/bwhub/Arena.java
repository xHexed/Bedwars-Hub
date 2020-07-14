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
    private int maxPlayers;
    private ArenaStatus status = ArenaStatus.Lobby;
    private final ArenaType type;
    private final int arenaNumber;

    public Arena(final String name, final String madeBy, final int maxPlayers) {
        this.name = name;
        this.madeBy = madeBy;
        this.maxPlayers = maxPlayers;

        if (name.contains("solo")) {
            type = ArenaType.SOLO;
        }
        else if (name.contains("duo")) {
            type = ArenaType.DUO;
        }
        else if (name.contains("squad")) {
            type = ArenaType.SQUAD;
        }
        else if (name.contains("mega")) {
            type = ArenaType.MEGA;
        }
        else {
            type = ArenaType.CUSTOM;
        }

        int i = name.length() - 1;
        final StringBuilder number = new StringBuilder();
        char pos = name.charAt(i);
        while (Util.isInteger(pos)) {
            number.insert(0, pos);
            i--;
            pos = name.charAt(i);
        }
        arenaNumber = Integer.parseInt(number.toString());
    }

    public void setPlayers(final int players) {
        this.players = players;
        Events.updateView(this);
    }

    public void setStatus(final ArenaStatus status) {
        this.status = status;
        Events.updateView(this);
    }

    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
        Events.updateView(this);
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

    public ArenaType getArenaType() {
        return type;
    }

    public int getArenaNumber() {
        return arenaNumber;
    }

    public enum ArenaStatus {
        Stopped,
        Lobby,
        Running,
        Reseting,
        EndLobby
    }

    public enum ArenaType {
        SOLO,
        DUO,
        SQUAD,
        MEGA,
        CUSTOM
    }
}

