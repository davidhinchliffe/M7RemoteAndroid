/**
 * Concrete implementation of the initialize command. Invokes the execute command on the receiver,
 * ServerConnection
 *
 *  Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */
package com.raspiworks.M7Remote.commands;

import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;
import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;

public class InitializeCommand implements RemoteCommand {
    private M7ServerCommandInvoker controller;
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
