/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.grassminevn.bwhub.bungeecord;

import com.grassminevn.bwhub.BedwarsHub;
import org.bukkit.Bukkit;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

public class JobManager {
    private static final List<JobStatus> jobs = new ArrayList<>();
    private static SocketManager manager;

    public static void onEnable() {
        manager = new SocketManager(){

            @Override
            public void onReceive(final DatagramPacket packet) {
                Communication.onPacketReceived(packet);
            }
        };
        manager.inject();
        manager.startReceivingPackets();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BedwarsHub.plugin, () -> {
            if (jobs.size() >= 1) {
                int i = 0;
                for (final JobStatus job : new ArrayList<>(jobs)) {
                    if (i > 8) break;
                    if (job.getChannel().isRegistred()) {
                        manager.sendPacket(job.getChannel().getInetAddress(), job.getChannel().getPort(), job.getPacket().getPacketString().getBytes());
                        job.done();
                        jobs.remove(job);
                    }
                    ++i;
                }
            }
        }, 0L, 20L);
    }

    public static void onDisable() {
        manager.close();
    }

    public static void addJob(final JobStatus status) {
        jobs.add(status);
    }

}

