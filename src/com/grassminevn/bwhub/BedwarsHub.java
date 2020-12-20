/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.Messenger
 */
package com.grassminevn.bwhub;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.grassminevn.bwhub.bungeecord.Communication;
import com.grassminevn.bwhub.config.Config;
import com.grassminevn.bwhub.config.LanguageConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalTime;

public class BedwarsHub
extends JavaPlugin {
    public static Plugin plugin;
    private ChannelFuture server;

    @Override
    public void onEnable() {
        plugin = this;
        final CommandExecutor cmd = new Command();
        getCommand("bw").setExecutor(cmd);
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        LanguageConfig.load();
        Config.load();

        final ThreadFactoryBuilder threadFactory = new ThreadFactoryBuilder().setNameFormat("BedwarsHub Server IO #%d").setDaemon(true);

        server = new ServerBootstrap()
                .group(new NioEventLoopGroup(1, threadFactory.build()), new NioEventLoopGroup(0, threadFactory.build()))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel channel) {
                        channel.pipeline().addLast(new StringDecoder(), new Communication());
                    }
                })
                .bind(2).syncUninterruptibly();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            final World world = Bukkit.getWorld("world");
            if (world == null) return;
            final LocalTime date = LocalTime.now();
            int time = (int) ((date.getHour() + ((double) date.getMinute() / 60) - 6) * 1000);
            if (time < 0) time += 24000;
            world.setTime(time);
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        server.channel().close().syncUninterruptibly();
    }

    public static String getVersion() {
        return plugin.getDescription().getVersion();
    }

}

