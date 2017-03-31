/**
 * OnClickListener for the Firing Button. When executed acts like the command invoker and
 * calls the fireCommand.execute method
 *
 * Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 **/
package com.raspiworks.M7Remote.ui;

import android.view.View;
import android.widget.Button;

import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;

public class FiringButtonOnClickListener implements Button.OnClickListener {
    private M7ServerCommandInvoker invoker;
    @Override
    public void onClick(View firingButton){
        invoker=((FiringButton)firingButton).getInvoker();
        invoker.fire(((FiringButton)firingButton).getChannel());
    }
}
