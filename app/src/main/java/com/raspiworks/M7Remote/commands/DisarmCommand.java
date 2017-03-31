package com.raspiworks.M7Remote.commands;

/**
 * Concrete implementation of the disarm command. Invokes the execute command on the receiver,
 * ServerConnection
 *
 *  Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;

public class DisarmCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public DisarmCommand(ServerConnectionFactoryInterface serverConnection)
    {
        this.serverConnection=serverConnection;
    }
    public void execute() {
        //gets a new server connection object so the command can be executed
        serverConnection.createNewServerConnection().execute("disarm");
    }

}

