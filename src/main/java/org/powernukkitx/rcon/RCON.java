package org.powernukkitx.rcon;

import cn.nukkit.Server;
import cn.nukkit.command.RemoteConsoleCommandSender;
import cn.nukkit.event.server.RemoteServerCommandEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import java.io.IOException;

//https://developer.valvesoftware.com/wiki/Source_RCON_Protocol

public class RCON {
    private final Server server;
    private final RCONServer serverThread;

    public RCON(Server server, String password, String address, int port) {
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Empty RCON password");
        }

        this.server = server;

        try {
            this.serverThread = new RCONServer(address, port, password);
            this.serverThread.start();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error during startup", e);
        }
        
    }

    public void check() {
        if (this.serverThread == null) {
            return;
        } else if (!this.serverThread.isAlive()) {
            return;
        }

        RCONCommand command;

        while ((command = serverThread.receive()) != null) {
            RemoteConsoleCommandSender sender = new RemoteConsoleCommandSender();
            RemoteServerCommandEvent event = new RemoteServerCommandEvent(sender, command.getCommand());

            this.server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.server.executeCommand(sender, command.getCommand());
            }

            this.serverThread.respond(command.getSender(), command.getId(), TextFormat.clean(sender.getMessages()));
        }
    }

    public void close() {
        try {
            synchronized (serverThread) {
                serverThread.close();
                serverThread.wait(5000);
            }
        } catch (InterruptedException exception) {
        }
    }
}