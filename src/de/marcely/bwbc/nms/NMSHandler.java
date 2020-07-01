/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package de.marcely.bwbc.nms;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class NMSHandler {
    public static void setNoAI(final Entity bukkitEntity) {
        final ArmorStand armorstand = (ArmorStand)bukkitEntity.getWorld().spawnEntity(bukkitEntity.getLocation().clone().add(0.0, -1.45, 0.0), EntityType.ARMOR_STAND);
        armorstand.setGravity(false);
        armorstand.addPassenger(bukkitEntity);
        armorstand.setVisible(false);
        setNBTTag(armorstand);
        setNBTTag(bukkitEntity);
    }

    private static void setNBTTag(final Entity entity) {
        final net.minecraft.server.v1_12_R1.Entity nmsEntity = ((CraftEntity)entity).getHandle();
        final NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }
}

