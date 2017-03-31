package com.raspiworks.M7Remote.commands;

/**
 * Concrete implementation of the arm command. Invokes the execute command on the receiver,
 * ServerConnection
 *
 *  Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;

public class ArmCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public ArmCommand(ServerConnectionFactoryInterface serverConnection)
    {
        this.serverConnection=serverConnection;
    }
    public void execute() {
        //gets a new receiver object so that the command can be executed.
        serverConnection.createNewServerConnection().execute("arm");
    }
}
