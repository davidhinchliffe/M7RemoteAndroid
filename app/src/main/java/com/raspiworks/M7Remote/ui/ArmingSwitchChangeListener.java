package com.raspiworks.M7Remote.ui;
/**
 * OnChangeListener for the arming switch.  Makes the arming switch act like the invoker of the command object by calling the armCommand.execute
 * or the disarm.execute commands depending on state
 *
 * Author: David Hinchliffe <belgoi@gmail.com>
 */

import android.widget.CompoundButton;

public class ArmingSwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //arm channel
            ((ArmingSwitch)buttonView).getArmCommand().execute();

        } else {
            //disarm channel
            ((ArmingSwitch)buttonView).getDisarmCommand().execute();
        }
    }
}

