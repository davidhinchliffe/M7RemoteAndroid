package com.raspiworks.M7Remote.ui;

/**
 * Creates a custom button to handle calling the fireCommand
 *
 *  Author: David Hinchliffe <belgoi@gmail.com>
 */

import android.content.Context;
import android.widget.Button;

import com.raspiworks.M7Remote.commands.RemoteCommand;

public class FiringButton extends Button {
    private RemoteCommand fireCommand;
    public FiringButton(Context mContext){
        super(mContext);
    }
    public void setFireCommand(RemoteCommand fireCommand){
        this.fireCommand=fireCommand;
    }
    public RemoteCommand getFireCommand(){
        return fireCommand;
    }


}
