/*
 * Decompiled with CFR 0.145.
 */
package com.grassminevn.bwhub;

public enum Permission {
    Reload("bw.reload"),
    Command_List("bw.list"),
    Command_Info("bw.info"),
    BetaUser("bw.beta");
    
    private final String selected_permission;

    Permission(final String permission) {
        selected_permission = permission;
    }

    public String getPermission() {
        return selected_permission;
    }
}

