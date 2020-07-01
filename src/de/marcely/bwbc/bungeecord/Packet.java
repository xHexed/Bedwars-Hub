/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc.bungeecord;

public abstract class Packet {
    public abstract String getPacketString();

    public enum PacketType {
        OUT_PacketChannelEnabled(0),
        OUT_PacketConnectPlayer(2),
        IN_PacketChannelExists(4),
        IN_PacketArenaCreate(5),
        IN_PacketArenaRemove(6),
        IN_PacketArenaUpdateSlots(7),
        IN_PacketArenaUpdateMaxSlots(8),
        IN_PacketArenaUpdateStatus(9),
        IN_PacketArenaUpdateName(10),
        IN_PacketArenaUpdateIcon(11),
        IN_PacketArenaUpdateMadeBy(13),
        IN_PacketArenaUpdateTeamPlayers(14);
        
        private final int selected_id;

        PacketType(final int id) {
            selected_id = id;
        }

        public int getID() {
            return selected_id;
        }
    }

}

