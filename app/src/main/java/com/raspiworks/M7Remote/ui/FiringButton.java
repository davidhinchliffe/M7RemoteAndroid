/**
 * Creates a custom button to handle calling the fireCommand
 *
 *  Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */
package com.raspiworks.M7Remote.ui;

import android.content.Context;
import android.widget.Button;

import com.raspiworks.M7Remote.commands.RemoteCommand;
import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;

public class FiringButton extends Button {
    private RemoteCommand fireCommand;
    private M7ServerCommandInvoker invoker;
    private int channel;

    public FiringButton(Context mContext){
        super(mContext);
    }
  /*  public void setFireCommand(RemoteCommand fireCommand){
        this.fireCommand=fireCommand;
    }
    public RemoteCommand getFireCommand(){
        return fireCommand;
    }*/
    public void setInvoker(M7ServerCommandInvoker invoker){
        this.invoker=invoker;
    }
    public M7ServerCommandInvoker getInvoker(){
        return invoker;
    }
    public void setChannel(int channel){
        this.channel=channel;
    }
    public int getChannel(){
        return channel;
    }

}
