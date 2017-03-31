/**
 * Creates a custom switch view to handle storing a reference to the arm & disarm commands
 *
 *  Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */
package com.raspiworks.M7Remote.ui;

import android.content.Context;
import android.widget.Switch;

//import android.widget.TextView;

import com.raspiworks.M7Remote.commands.ArmCommand;
import com.raspiworks.M7Remote.commands.DisarmCommand;
import com.raspiworks.M7Remote.commands.RemoteCommand;
import com.raspiworks.M7Remote.commands.StatusCommand;
import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;

//TODO: CLEAN UP THIS METHOD
public class ArmingSwitch extends Switch {
    RemoteCommand armCommand;
    RemoteCommand disarmCommand;
    RemoteCommand statusCommand;
    M7ServerCommandInvoker invoker;
    int channel;
   // TextView responseField=null;
    public ArmingSwitch(Context mContext){
        super(mContext);

    }
    public void setInvoker(M7ServerCommandInvoker invoker){
        this.invoker=invoker;
    }
    public M7ServerCommandInvoker getInvoker(){
        return invoker;
    }

  /*  public void setArmCommand(ArmCommand command){
        this.armCommand=command;
    }
    public void setDisarmCommand(DisarmCommand command){
        this.disarmCommand=command;
    }
    public void setStatusCommand(StatusCommand statusCommand){
        this.statusCommand=statusCommand;
    }
    public RemoteCommand getArmCommand(){
        return armCommand;
    }
    public RemoteCommand getDisarmCommand(){
        return disarmCommand;
    }*/
    public void setChannel(int channel){
        this.channel=channel;
    }
    public int getChannel(){
        return channel;
    }
}


