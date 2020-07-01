/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package de.marcely.bwbc.bungeecord;

import de.marcely.bwbc.Console;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.*;

public class SocketManager
implements PacketReceiver {
    private DatagramSocket socket;
    private Thread receivingPacketsThread;

    private boolean isInjected() {
        return socket != null;
    }

    private boolean isReceivingPackets() {
        return receivingPacketsThread != null;
    }

    public void inject() {
        if (!isInjected()) {
            try {
                socket = new DatagramSocket(null);
                final InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), Bukkit.getServer().getPort());
                socket.bind(address);
                Console.printBungeecordInfo("Successfully bind into '" + InetAddress.getLocalHost().getHostAddress() + ":" + Bukkit.getServer().getPort());
            }
            catch (final SocketException | UnknownHostException e) {
                Console.printBungeecordWarn("Failed to bind into socket!!");
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (isInjected()) {
            stopReceivingPackets();
            socket.close();
            socket = null;
        }
    }

    public void sendPacket(final InetAddress address, final int port, final byte[] bytes) {
        final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        try {
            socket.send(packet);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void startReceivingPackets() {
        if (!isReceivingPackets()) {
            receivingPacketsThread = new Thread(() -> {
                while (isReceivingPackets()) {
                    final byte[] bytes = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                    try {
                        socket.receive(packet);
                    }
                    catch (final IOException iOException) {
                        // empty catch block
                    }
                    if (packet.getLength() < 1) continue;
                    onReceive(packet);
                }
            });
            receivingPacketsThread.start();
        }
    }

    private void stopReceivingPackets() {
        if (isReceivingPackets()) {
            receivingPacketsThread.interrupt();
            receivingPacketsThread = null;
        }
    }

    @Override
    public void onReceive(final DatagramPacket packet) {
    }

}

