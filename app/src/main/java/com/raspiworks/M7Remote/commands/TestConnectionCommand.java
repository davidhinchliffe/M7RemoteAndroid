package com.raspiworks.M7Remote.commands;

/**
 * Concrete implementation of the testConnection command. Invokes the execute command on the receiver,
 * ServerConnection
 *
 *  Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;

public class TestConnectionCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public TestConnectionCommand(ServerConnectionFactoryInterface serverConnection)
    {
        this.serverConnection=serverConnection;
    }
    public void execute() {
        //invokes the execute command on the receiver
        serverConnection.createNewServerConnection().execute("test");
    }
}

