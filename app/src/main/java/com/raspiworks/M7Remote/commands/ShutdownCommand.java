package com.raspiworks.M7Remote.commands;
import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;

/**
 * Defines the shutdown Command
 * Author: David Hinchliffe
 */



public class ShutdownCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public ShutdownCommand(ServerConnectionFactoryInterface serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void execute() {
        serverConnection.createNewServerConnection().execute("shutdown");
    }
}
