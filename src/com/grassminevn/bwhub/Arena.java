/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package com.grassminevn.bwhub;

import com.grassminevn.bwhub.inventory.arena.ArenaUpdateHandler;

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

    Arena(final String name, final String madeBy, final int maxPlayers) {
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
        ArenaUpdateHandler.updateView(this);
    }

    public void setStatus(final ArenaStatus status) {
        this.status = status;
        ArenaUpdateHandler.updateView(this);
    }

    void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
        ArenaUpdateHandler.updateView(this);
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
        switch (status) {
            case Running:
                list.add(Language.ARENA_RUNNING.toString());
                break;
            case Stopped:
                list.add(Language.ARENA_STOPPED.toString());
                break;
            case Lobby:
                list.add(Language.ARENA_LOBBY.toString());
                break;
            case Reseting:
            case Resetting:
            case EndLobby:
                list.add(Language.ARENA_RESETING.toString());
                break;
        }
        list.add("§fNgười chơi: " + players + "/" + maxPlayers);
        return list;
    }

    public boolean isJoinable() {
        return canAutoJoin() || status == ArenaStatus.Running;
    }

    boolean canAutoJoin() {
        return status == ArenaStatus.Lobby && players < maxPlayers;
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
        Resetting,
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

