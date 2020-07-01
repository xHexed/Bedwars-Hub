/*
 * Decompiled with CFR 0.145.
 */
package de.marcely.bwbc;

public enum Permission {
    Command_Summon("bw.summon"),
    Command_List("bw.list"),
    Command_Info("bw.info"),
    Command_Join("bw.join"),
    BetaUser("bw.beta");
    
    private final String selected_permission;

    Permission(final String permission) {
        selected_permission = permission;
    }

    public String getPermission() {
        return selected_permission;
    }
}

