/**
 * M7ServerCommandInvoker is the invoker in the command pattern.  It manages the
 * command objects and is responsible for invoking the execute method when the appropriate command
 * is called.  It decouples the controller from the rest of the command pattern that does the actual work
 *
 * Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */
package com.raspiworks.M7Remote.invoker;

import com.raspiworks.M7Remote.commands.EmptyCommand;
//import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactoryInterface;
//import com.raspiworks.hydra.ui.MyCallBack;
//import com.belgoi.raspiworks.UI.onServerResponseListener;
import com.raspiworks.M7Remote.commands.RemoteCommand;

public class M7ServerCommandInvoker {
    private RemoteCommand armCommand[];
    private RemoteCommand disarmCommand[];
    private RemoteCommand fireCommand[];
    private RemoteCommand statusCommand[];
    private RemoteCommand initializeCommand;
    private RemoteCommand resetCommand;
    private RemoteCommand testConnectionCommand;
    private RemoteCommand shutdownCommand;



    public M7ServerCommandInvoker(){
        //initialize all commands to hold a reference to the empty command to avoid
        //null pointers
        initializeCommand=new EmptyCommand();
        resetCommand=new EmptyCommand();
        testConnectionCommand=new EmptyCommand();
        shutdownCommand=new EmptyCommand();
    }

    public void setActionCommands(int channel,RemoteCommand fireCommand,RemoteCommand statusCommand){
        //assigns two commands used during the firing activity
        this.statusCommand[channel]=statusCommand;
        this.fireCommand[channel]=fireCommand;
    }
    public void setActionCommands(int channel,RemoteCommand armCommand,RemoteCommand disarmCommand,RemoteCommand statusCommand){
        //assigns the 3 commands used during the arming activity
        this.armCommand[channel]=armCommand;
        this.disarmCommand[channel]=disarmCommand;
        this.statusCommand[channel]=statusCommand;
    }
    private void initializeActionCommands(int maxChannels){
        //intialize the command arrays to hold a reference to the empty command to avoid null pointers
        for(int i=0;i<maxChannels;i++){
            armCommand[i]=new EmptyCommand();
        }
        for(int i=0;i<maxChannels;i++){
            disarmCommand[i]=new EmptyCommand();
        }
        for(int i=0;i<maxChannels;i++){
            fireCommand[i]=new EmptyCommand();
        }
        for(int i=0;i<maxChannels;i++){
            statusCommand[i]=new EmptyCommand();
        }
    }

    public void setMaxChannels(int maxChannels)
    {
        //setter method to used to initialize the command arrays to
        //the size of the maxChannels
        this.armCommand=new RemoteCommand[maxChannels];
        this.disarmCommand=new RemoteCommand[maxChannels];
        this.fireCommand=new RemoteCommand[maxChannels];
        this.statusCommand=new RemoteCommand[maxChannels];
        initializeActionCommands(maxChannels);
    }
    public void setQueryCommands(RemoteCommand initalizeCommand,RemoteCommand resetCommand, RemoteCommand testConnectionCommand,RemoteCommand shutdownCommand){
        //setter method for the commands that don't act on a specific channel
        this.initializeCommand=initalizeCommand;
        this.resetCommand=resetCommand;
        this.testConnectionCommand=testConnectionCommand;
        this.shutdownCommand=shutdownCommand;

    }
    public void fire(int channelNumber){
        fireCommand[channelNumber].execute();
    }
    public void arm(int channelNumber){
        armCommand[channelNumber].execute();
    }
    public void disarm(int channelNumber){
        disarmCommand[channelNumber].execute();
    }
    public void status(int channelNumber){
        statusCommand[channelNumber].execute();
    }
    public void initialize(){
        initializeCommand.execute();
    }
    public void reset(){
        resetCommand.execute();
    }
    public void testConnection(){
        testConnectionCommand.execute();
    }
    public void shutdown(){
        shutdownCommand.execute();
    }


}

