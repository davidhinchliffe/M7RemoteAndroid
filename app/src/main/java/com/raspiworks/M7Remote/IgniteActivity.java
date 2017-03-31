package com.raspiworks.M7Remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.raspiworks.M7Remote.commands.FireCommand;
import com.raspiworks.M7Remote.commands.RemoteCommand;
import com.raspiworks.M7Remote.commands.StatusCommand;
import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;
import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactory;
import com.raspiworks.M7Remote.ui.FiringButton;
import com.raspiworks.M7Remote.ui.FiringButtonOnClickListener;
import com.raspiworks.M7Remote.ui.UIControls;
import static android.view.Gravity.CENTER;


public class IgniteActivity extends Activity {
    private String m7IpAddress=new String();
    private int maxChannels=0;
    private M7ServerCommandInvoker invoker;
    private int numberOfChannelsArmed=0;
    private Button abortButton;
    private final static int RESPONSE_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignite);

        abortButton=(Button)findViewById(R.id.buttonAbort);
        Bundle bundle=getIntent().getExtras();
        if (bundle.containsKey("ipAddress")){
            m7IpAddress=bundle.getString("ipAddress");
        }
        if (bundle.containsKey("maxChannels")){
            maxChannels=bundle.getInt("maxChannels");
        }
        invoker =new M7ServerCommandInvoker();
        invoker.setMaxChannels(maxChannels);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("MaxChannels",maxChannels);
        savedInstanceState.putString("ServerIpAddress",m7IpAddress);
    }
    @Override
    public void onResume(){
        super.onResume();
        //if maxChannels==0 then this is the first time through
        //or the shutdown command has been issued otherwise restore the state
        if (maxChannels>0){
            setupFiringControls(maxChannels);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_ignite,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_abort:
                abortButton.callOnClick();
                return true;
            case R.id.action_shutdown:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //executed when the last active channel has been fired. It closes the activity
    //and passes control back to the main activity. It passes back the value "reset"
    //so the main activity can reset the invoker
    public void finishFiring(){
        Toast toast=Toast.makeText(this,"All channels have been fired",Toast.LENGTH_SHORT);
        toast.show();
        Intent intent=new Intent();
        intent.putExtra("reset",true);
        setResult(RESULT_OK,intent);
        this.finish();
    }
    //OnClickListener for the abort button.  It passes the value "abort" back to
    //the main activity
    public void abortOnClick(View view){
        Toast toast=Toast.makeText(this,"Aborting launch!!", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent=new Intent();
        intent.putExtra("abort","true");
        setResult(RESULT_CANCELED,intent);
        this.finish();
    }
    //Displays the first line on the activity. It gives a visual cue as to how many
    //channels are armed and ready
    private void displayHeader(int numberOfChannels){
        TextView header=(TextView)findViewById(R.id.Header);
        if (numberOfChannels==1){
            header.setText(String.format(getResources().getString(R.string.header),numberOfChannels));
        }
        else if(numberOfChannels>1){
            header.setText(String.format(getResources().getString(R.string.header_plural),numberOfChannels));
        }
    }
    //Keeps track of the number of channels are still armed and updates the header. When all channels have been fired then
    //finishFiring is called
    private void setStatusOnTextChanged(TextView txtStatus,final View firingButton){
        txtStatus.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start,int before,int count){}
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            @Override
            public void afterTextChanged(Editable s){
                if (s.toString().equals("Armed")){
                    firingButton.setEnabled(true);
                }
                else if(s.toString().equals("Disarmed")){
                    firingButton.setEnabled(false);
                    if(numberOfChannelsArmed>0) {
                        //starts off with maxChannels and is decreased for each channel
                        //that isn't armed
                        numberOfChannelsArmed--;
                    }
                    if (numberOfChannelsArmed==0){
                        //no more armed channels, nothing else to do here, return to main activity
                        finishFiring();
                    }
                }
                displayHeader(numberOfChannelsArmed);
            }
        });
    }
    //draws the firing buttons and the associated textViews and assigns the commands
    // to the buttons and the invoker (M7ServerCommandInvoker)
    public void setupFiringControls(int numberOfChannels){
        TableLayout table=(TableLayout)findViewById(R.id.firingTable);
        numberOfChannelsArmed=maxChannels;
        for(int channel=0;channel<numberOfChannels;++channel){
            TableRow row=new TableRow(this);
            float layout_weight=1;

            TextView txtStatus=(new UIControls(this)).createTextView(this,CENTER,1,0,0,0,0);
            TextView txtChannel=(new UIControls(this)).createTextView(this,CENTER,1,0,0,0,0);
            txtChannel.setText(String.format("%d",channel));


            //instantiate command objects and bind the txtStatus textView with the firing button
            RemoteCommand fireCommand=new FireCommand(new ServerConnectionFactory(txtStatus,m7IpAddress,channel));
            RemoteCommand statusCommand=new StatusCommand(new ServerConnectionFactory(txtStatus,m7IpAddress,channel));

            //add the fireCommand and statusCommad to the invoker
            invoker.setActionCommands(channel,fireCommand,statusCommand);

            //setup the firing button
            FiringButton buttonFire = new FiringButton(this);
            buttonFire.setEnabled(false);

            //create the firing button
            //give the firing button a reference to the invoker and a channel. The invoker is called in
            //the OnClickListener
            buttonFire.setInvoker(invoker);
            buttonFire.setChannel(channel);

            buttonFire.setOnClickListener(new FiringButtonOnClickListener());
            buttonFire.setText(getResources().getString(R.string.fire_label));
            buttonFire.setHeight(new UIControls(this).convertToPixels(40));

            //setup the layout parameters for the button, since it is placed in a tableRow, TableRow.LayoutParams is used
            TableRow.LayoutParams lp=new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,layout_weight);
            buttonFire.setLayoutParams(lp);
            //register the onClickListener for the firing button
            setStatusOnTextChanged(txtStatus,buttonFire);

            //add views to the table row and add that to the table
            row.addView(txtChannel);
            row.addView(buttonFire);
            row.addView(txtStatus);
            table.addView(row);
            //get the status of the channel. the status is bound to the channel row
            invoker.status(channel);

        }
    }


}


