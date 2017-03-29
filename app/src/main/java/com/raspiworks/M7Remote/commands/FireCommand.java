package com.raspiworks.M7Remote.commands;

/**
 * Concrete implementation of the fire command. Invokes the execute command on the receiver,
 * ServerConnection
 *
 *  Author: David Hinchliffe <belgoi@gmail.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;

public class FireCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public FireCommand(ServerConnectionFactoryInterface serverConnection)
    {
        this.serverConnection=serverConnection;
    }
    public void execute() {
        //invokes the execute method on the receiver
        serverConnection.createNewServerConnection().execute("fire");
        //TODO: ADD A DELAY HERE TO MAKE SURE THE IGNITER LIGHTS
        serverConnection.createNewServerConnection().execute("disarm");
    }

}
