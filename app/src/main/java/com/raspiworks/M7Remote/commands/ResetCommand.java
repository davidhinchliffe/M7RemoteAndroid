package com.raspiworks.M7Remote.commands;

/**
 * Concrete implementation of the resetCommand. executes reset on the receiver Server Connection
 *
 * Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;


public class ResetCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public ResetCommand(ServerConnectionFactoryInterface serverConnection)
    {
        this.serverConnection=serverConnection;
    }
    public void execute() {
        //TODO: the armCommand should be the one acting upon the ServerConnection and not the controller
        serverConnection.createNewServerConnection().execute("reset");
    }
}

