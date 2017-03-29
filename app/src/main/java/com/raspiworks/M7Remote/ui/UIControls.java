package com.raspiworks.M7Remote.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import static android.view.Gravity.CENTER;

/**
 * Created by David on 2/8/2017.
 *
 * extending ContextWrapper provides a way to get a context without introducting memory leaks
 * by storing a reference to a context that might outlive the activity that called it. Quite simply
 * the ContextWrapper takes in a base context and inherits its functionality without changing the
 * original context.
 *
 * This solution came from: http://itekblog.com/android-context-in-non-activity-class/
 */

public class UIControls extends ContextWrapper {

    public UIControls(Context base){
        super(base);
    }
    public int convertToPixels(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public TextView createTextView(Context context, int gravity, double layout_weight, int pad_left, int pad_top, int pad_right, int pad_bottom){
        TableRow.LayoutParams lp=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,(float)layout_weight);
        TextView textView = new TextView(context);
        textView.setGravity(gravity);
        textView.setWidth(0);
        textView.setPadding(convertToPixels(pad_left),convertToPixels(pad_top),convertToPixels(pad_right),convertToPixels(pad_bottom));
        textView.setLayoutParams(lp);

        return textView;
    }
    public ArmingSwitch drawArmingSwitch(){
        TableRow.LayoutParams lp;
        lp=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,(float)1);

        ArmingSwitch armSwitch =  new ArmingSwitch(this);
        int w=armSwitch.getSwitchMinWidth();
        armSwitch.setLayoutParams(lp);
        armSwitch.setGravity(CENTER);
        armSwitch.setSwitchMinWidth(convertToPixels(75));
        return armSwitch;
    }
    public void enableView(View view){
        if(!view.isEnabled()){
            view.setEnabled(true);
        }
    }
    public void disableView(View view){
        if(view.isEnabled()){
            view.setEnabled(false);
        }
    }
}
