package com.raspiworks.M7Remote.ui;

/**
 * Creates a custom switch view to handle storing a reference to the arm & disarm commands
 *
 *  Author: David Hinchliffe <belgoi@gmail.com>
 */

import android.content.Context;
import android.widget.Switch;
//import android.widget.TextView;

import com.raspiworks.M7Remote.commands.ArmCommand;
import com.raspiworks.M7Remote.commands.DisarmCommand;
import com.raspiworks.M7Remote.commands.RemoteCommand;
import com.raspiworks.M7Remote.commands.StatusCommand;

//TODO: CLEAN UP THIS METHOD
public class ArmingSwitch extends Switch {
    RemoteCommand armCommand;
    RemoteCommand disarmCommand;
    RemoteCommand statusCommand;
   // TextView responseField=null;
    public ArmingSwitch(Context mContext){
        super(mContext);

    }
    public void setArmCommand(ArmCommand command){
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
    }
   // public RemoteCommand getStatusCommand(){
   //     return statusCommand;
   // }

    //public TextView getResponseField(){
    //    return responseField;
    //}


}


