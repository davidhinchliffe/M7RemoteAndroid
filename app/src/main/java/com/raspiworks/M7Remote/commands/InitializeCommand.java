package com.raspiworks.M7Remote.commands;

/**
 * Concrete implementation of the initialize command. Invokes the execute command on the receiver,
 * ServerConnection
 *
 *  Author: David Hinchliffe <belgoi@gmail.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;
import com.raspiworks.M7Remote.invoker.M7ServerController;

public class InitializeCommand implements RemoteCommand {
    private M7ServerController controller;
    private ServerConnectionFactoryInterface serverConnection;

    public InitializeCommand(ServerConnectionFactoryInterface serverConnection)
    {
        this.serverConnection=serverConnection;
    }
    public void execute() {
        //invokes the execute command on the receiver
        serverConnection.createNewServerConnection().execute("initialize");
    }

}
