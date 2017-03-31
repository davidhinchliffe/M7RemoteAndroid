/**
 * OnChangeListener for the arming switch.  Makes the arming switch act like the invoker of the command object by calling the armCommand.execute
 * or the disarm.execute commands depending on state
 *
 * Author: David Hinchliffe <david.hinchliffe@raspiworks.com>
 */
package com.raspiworks.M7Remote.ui;

import android.widget.CompoundButton;
import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;

public class ArmingSwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
    private M7ServerCommandInvoker invoker;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //arm channel
            invoker=((ArmingSwitch)buttonView).getInvoker();
            invoker.arm(((ArmingSwitch)buttonView).getChannel());
        } else {
            //disarm channel
            invoker=((ArmingSwitch)buttonView).getInvoker();
            invoker.disarm(((ArmingSwitch)buttonView).getChannel());
        }
    }
}

