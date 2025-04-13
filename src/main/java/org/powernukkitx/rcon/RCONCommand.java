package org.powernukkitx.rcon;

import java.nio.channels.SocketChannel;

public class RCONCommand {
    private final SocketChannel sender;
    private final int id;
    private final String command;

    public RCONCommand(SocketChannel sender, int id, String command) {
        this.sender = sender;
        this.id = id;
        this.command = command;
    }

    public SocketChannel getSender() {
        return this.sender;
    }

    public int getId() {
        return this.id;
    }

    public String getCommand() {
        return this.command;
    }
}