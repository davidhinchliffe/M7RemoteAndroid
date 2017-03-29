package com.raspiworks.M7Remote.ui;
/**
 * OnClickListener for the Firing Button. When executed acts like the command invoker and
 * calls the fireCommand.execute method
 *
 * Author: David Hinchliffe <belgoi@gmail.com>
 */

import android.view.View;
import android.widget.Button;

public class FiringButtonOnClickListener implements Button.OnClickListener {
    @Override
    public void onClick(View firingButton){
        ((FiringButton)firingButton).getFireCommand().execute();
    }
}
