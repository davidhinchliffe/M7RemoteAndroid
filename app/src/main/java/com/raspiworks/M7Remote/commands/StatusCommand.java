package com.raspiworks.M7Remote.commands;
/**
 * Concrete implementation of the status command. Executes the status command on the
 * server connection receiver
 *
 * Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */

import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;


public class StatusCommand implements RemoteCommand {
    private ServerConnectionFactoryInterface serverConnection;

    public StatusCommand(ServerConnectionFactoryInterface serverConnection){
        this.serverConnection=serverConnection;
    }
    public void execute() {
        serverConnection.createNewServerConnection().execute("status");
    }
}

