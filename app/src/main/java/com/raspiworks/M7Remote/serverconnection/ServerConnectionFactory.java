package com.raspiworks.M7Remote.serverconnection;

/**
 *  ServerConnectionFactory is a server connection where a TextView is bound to the
 *  connection to allow a response from the server to be displayed and accomodates commands
 *  that act upon a specific channel
 *
 *  Author: David Hinchliffe <belgoi@gmail.com>
 */

import android.widget.TextView;


import com.raspiworks.M7Remote.ui.ArmingSwitch;


public class ServerConnectionFactory implements ServerConnectionFactoryInterface {
    private int channel=-1;
    private TextView view;
    private String serverIpAddress;

   // public ServerConnectionFactory(String serverIpAddress){
   //     this.serverIpAddress=serverIpAddress;
   // }
    public ServerConnectionFactory(TextView view, String serverIpAddress){
        this.serverIpAddress=serverIpAddress;
        this.view=view;
    }
    public ServerConnectionFactory(TextView view, String serverIpAddress, int channel){
        this.view=view;
        this.serverIpAddress=serverIpAddress;
        this.channel=channel;
    }

    public ServerConnection createNewServerConnection()
    {
        if (view==null)
            return new ServerConnection(serverIpAddress);
        else if(channel <0)
            return new ServerConnection(view,serverIpAddress);
        //if neither of the above two conditions are true then the default server connection is returned
        return new ServerConnection(view,serverIpAddress,channel);
    }
}
