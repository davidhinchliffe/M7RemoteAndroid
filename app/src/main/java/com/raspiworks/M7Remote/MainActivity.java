package com.raspiworks.M7Remote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.raspiworks.M7Remote.commands.ShutdownCommand;
import com.raspiworks.M7Remote.invoker.M7ServerCommandInvoker;
import com.raspiworks.M7Remote.serverconnection.ServerConnectionFactory;
import com.raspiworks.M7Remote.ui.Keyboard;
import com.raspiworks.M7Remote.ui.OnSetMaxChannelsListener;
import com.raspiworks.M7Remote.ui.ArmingSwitchChangeListener;
import com.raspiworks.M7Remote.ui.ArmingSwitch;
import com.raspiworks.M7Remote.commands.ArmCommand;
import com.raspiworks.M7Remote.commands.DisarmCommand;
import com.raspiworks.M7Remote.commands.InitializeCommand;
import com.raspiworks.M7Remote.commands.RemoteCommand;
import com.raspiworks.M7Remote.commands.ResetCommand;
import com.raspiworks.M7Remote.commands.StatusCommand;
import com.raspiworks.M7Remote.commands.TestConnectionCommand;
import com.raspiworks.M7Remote.ui.UIControls;


import java.util.regex.Pattern;

import static android.view.Gravity.CENTER;

public class MainActivity extends Activity implements OnSetMaxChannelsListener{
    private String launcherIpAddress=new String();
    private TextView connectMsg;
    private TextView txtHiddenChannelCount;
    private M7ServerCommandInvoker invoker;
    private int maxChannels=0;
    private Button buttonConnect;
    private Button buttonNext;
    private Button buttonShutdown;
    private EditText txtIpAddress;
    private int numberOfChannelsArmed=0;
    private static final Pattern PARTIAl_IP_ADDRESS = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"+
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$");
    //constant for shared preferences
    public static final String IPADDRESS="launcherIpAddress";
    private static final int REQUEST_CODE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  mContext=this;
        invoker =new M7ServerCommandInvoker();
        //find the textView controls and assign them to a variable
        connectMsg=(TextView)findViewById(R.id.connectionMessage);
        buttonConnect=(Button) findViewById(R.id.connectButton);
        buttonNext=(Button) findViewById(R.id.nextButton);
        txtHiddenChannelCount=(TextView)findViewById(R.id.hiddenChannelCount);
        buttonNext.setEnabled(false);
        txtHiddenChannelCount.setVisibility(View.INVISIBLE);
        txtIpAddress=(EditText)findViewById(R.id.ipAddress);
        //set the Event Handlers
        setHiddenChannelCountOnTextChanged();
        setIpAddressOnTextChanged();
        setButtonConnectOnClick();
        setConnectMsgOnTextChanged();
        //hidden TextView to pass into a command to get the number of channels available
        if(savedInstanceState!=null)
        {
            maxChannels=savedInstanceState.getInt("MaxChannels");
            invoker.setMaxChannels(maxChannels);
            launcherIpAddress=savedInstanceState.getString("ServerIpAddress");
            txtIpAddress.setText(launcherIpAddress);

            buttonConnect.callOnClick();
        }
        else{
            SharedPreferences preferences=this.getPreferences(MODE_PRIVATE);
            String ipAddress=preferences.getString(IPADDRESS,"");
            if(!ipAddress.isEmpty()){
                txtIpAddress.setText(ipAddress);
            }

        }
        //hide soft keyboard upon initialization
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_reset:
                prepareRemoteForReset();
                return true;
            case R.id.action_shutdown:
                prepareRemoteForShutdown();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==REQUEST_CODE)
            if(resultCode==RESULT_OK){
                if (data.getBooleanExtra("reset",true)){
                    //all channels have been fired from the firingActivity therefore, the
                    //controller needs to be reset so that all gpio pins can be set back to low.
                    prepareRemoteForReset();
                }
            }
            else if(resultCode==RESULT_CANCELED){
                //Abort was called, onResume is called next per the activity life cycle.
            }
        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("MaxChannels",maxChannels);
        savedInstanceState.putString("ServerIpAddress",launcherIpAddress);
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
        //if maxChannels==0 then this is the first time through
        //or the shutdown command has been issued otherwise restore the state
        if (maxChannels>0){
            setupArmingControls(maxChannels);
        }
    }
  /* public void onStop(){
        super.onStop();
        int i=0;
        i++;
    }
    public void onDestroy(){
        super.onDestroy();
        int i=0;
        i++;

    }*/
    private void prepareRemoteForReset(){
        invoker.reset();
        buttonConnect.setEnabled(true);
        buttonNext.setEnabled(false);
        txtIpAddress.setFocusable(true);
        maxChannels=0;
        TableLayout channelsTable=(TableLayout) findViewById(R.id.channelTable);
        channelsTable.removeAllViews();

   }
    private void prepareRemoteForShutdown(){
        //to make certain that the raspberry pi is properly shutdown, the
        //shutdown command needs to be issued so that all provisioned pins
        //can be put back to the low state.
        invoker.shutdown();
        //enable/disable the buttons
        buttonConnect.setEnabled(true);
        buttonNext.setEnabled(false);
        txtIpAddress.setFocusable(true);
        //have to reset MaxChannels back to zero
        maxChannels=0;
        //clear the table containing the channels
        TableLayout channelsTable=(TableLayout) findViewById(R.id.channelTable);
        channelsTable.removeAllViews();
        //calls onResume after this is called when it is called from onActivityResult
    }
    private void initializeQueryCommands(){
        //Server connection uses an AsyncTask to connect to the server via wifi
        //this connection is a once and done connection so a Factory class is used to create
        //the connection as needed.
        ServerConnectionFactory initializeLauncher;
        ServerConnectionFactory testConnection;
        ServerConnectionFactory resetLauncher;
        ServerConnectionFactory shutdownLauncher;

        //create the ServerConnectionFactoryInterface object for each command
        initializeLauncher=new ServerConnectionFactory(txtHiddenChannelCount,launcherIpAddress);
        resetLauncher=new ServerConnectionFactory(connectMsg,launcherIpAddress);
        testConnection=new ServerConnectionFactory(connectMsg,launcherIpAddress);
        shutdownLauncher=new ServerConnectionFactory(connectMsg,launcherIpAddress);

        //attach the ServerConnectionFactoryInterface object to the command
        RemoteCommand initialize=new InitializeCommand(initializeLauncher);
        RemoteCommand test=new TestConnectionCommand(testConnection);
        RemoteCommand reset=new ResetCommand(resetLauncher);
        RemoteCommand shutdown=new ShutdownCommand(shutdownLauncher);

        //assign commands to controller
        invoker.setQueryCommands(initialize,reset,test,shutdown);

    }
    private void setConnectMsgOnTextChanged(){
        connectMsg.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start,int before,int count){}
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            @Override
            public void afterTextChanged(Editable s){
                //initialize Launcher. the number of channels available is returned and onSetMaxChannels is called
                if (connectMsg.getText().toString().equals("connected successfully")) {
                    //The Firing System has been previously initialized and the max channels have been returned
                    //This occurs when the activity is restoring state after an interruption
                    buttonConnect.setEnabled(false);
                    //TODO: DELETE FOLLOWING LINE
                   // buttonShutdown.setEnabled(true);
                    //don't allow the ip Address to be changed once it has been set
                    //it is allowed to be modified after a shutdown is performed
                    txtIpAddress.setFocusable(false);
                    //save ip address in shared preferences
                    saveIpAddress();
                    //the firing system hasn't been initialized.  This happens upon first connection
                    if (maxChannels == 0)
                        invoker.initialize();

                }
                //After the invoker is reset, the ipAddress field can once again be
                //modified
                else if(s.toString().equals("shutdown successful")){
                    txtIpAddress.setFocusableInTouchMode(true);
                    txtIpAddress.setFocusable(true);

                }
            }

        });

    }
    private void setStatusOnTextChanged(TextView txtStatus,final ArmingSwitch armingSwitch){
        final Context context=this;
        txtStatus.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start,int before,int count){}
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            @Override
            public void afterTextChanged(Editable s){
                //keep up with numberOfChannelsArmed so that the nextButton can be enabled and
                //reenabled as expected.
                if (s.toString().equals("Armed")){
                    armingSwitch.setChecked(true);
                    numberOfChannelsArmed++;
                    new UIControls(context).enableView(buttonNext);
                }
                else if(s.toString().equals("Disarmed")){
                    armingSwitch.setChecked(false);
                    if(numberOfChannelsArmed>0) {
                        numberOfChannelsArmed--;
                    }
                    if (numberOfChannelsArmed==0){
                        new UIControls(context).disableView(buttonNext);
                        buttonNext.setEnabled(false);
                    }

                }
            }
        });
    }
    private void setIpAddressOnTextChanged(){
        //uses regex to validate the entry of an Ip Address.  It performs the check as the characters
        //are being inputted.
        final EditText ipAddress=(EditText) findViewById(R.id.ipAddress);
        ipAddress.addTextChangedListener(new TextWatcher(){
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after) {}

            private String mPreviousText = "";
            @Override
            public void afterTextChanged(Editable s) {
                if(PARTIAl_IP_ADDRESS.matcher(s).matches()) {
                    mPreviousText = s.toString();
                } else {
                    s.replace(0, s.length(), mPreviousText);
                }
            }
        });
    }

    @Override
    public void OnSetMaxChannels(String s){
        //callback method used by ServerConnection when initialize is called
        //the max number of channels is returned from the server and
        //assigned to a variable
        try{
            maxChannels=Integer.parseInt(s);
            invoker.setMaxChannels(maxChannels);
            setupArmingControls(maxChannels);
        }catch(NumberFormatException e){
            maxChannels=0;
        }
    }
    private void setHiddenChannelCountOnTextChanged(){
        txtHiddenChannelCount.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start,int before,int count){}
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            @Override
            public void afterTextChanged(Editable text){
                //initialize Launcher. the number of channels available is returned and onSetMaxChannels is called
                //callback method used by ServerConnection when initialize is called
                //the max number of channels is returned from the server and
                //assigned to a variable
                try{
                    maxChannels=Integer.parseInt(text.toString());
                    invoker.setMaxChannels(maxChannels);
                    setupArmingControls(maxChannels);
                }catch(NumberFormatException e){
                    maxChannels=0;
                }
            }
        });

    }
    public void nextButtonOnClick(View view){
        Bundle bundle=new Bundle();
        bundle.putString("ipAddress",launcherIpAddress);
        bundle.putInt("maxChannels",maxChannels);
        Intent intent=new Intent(this,IgniteActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }
    private void setButtonConnectOnClick(){
        buttonConnect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launcherIpAddress=txtIpAddress.getText().toString();
                connectMsg.setText("");
                initializeQueryCommands();
                hideKeyBoard();
                invoker.testConnection();
                Toast toast=Toast.makeText(getApplicationContext(),"Connecting to server", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }

    public void saveIpAddress(){
        //use SharedPreferences to store the ipAddress
        //since only one preference file is needed, getPreferences was used over
        //getSharedPreferences
        SharedPreferences preferences=this.getPreferences(MODE_PRIVATE);
        //get an Editor object to ensure the file maintains integrity
        SharedPreferences.Editor preferenceEditor=preferences.edit();
        //put the data into the editor
        preferenceEditor.putString(IPADDRESS,launcherIpAddress);
        //save preferences in the background
        preferenceEditor.apply();


    }
    public void hideKeyBoard(){
        Keyboard.hideKeyboard(this);
    }

    private void setupArmingControls(int numberOfChannels){
        //Assigns the arming & disarming commands to the arming switches,assigns them to channel, and draws them on screen
        //uses the maxChannels variable that was initialized when the connection was established to
        //determine how many channels are needed.
        TableLayout channelsTable=(TableLayout) findViewById(R.id.channelTable);
        channelsTable.removeAllViews();
        for(int channel=0;channel<numberOfChannels;channel++) {
            TableRow row = new TableRow(this);

            //create the labels for the channel numbers
            TextView channelLabel;
            channelLabel=new UIControls(this).createTextView(this,CENTER,.75,0,0,0,0);
            channelLabel.setText(String.format("%d",channel));

            //create the arm/disarm status TextView
            TextView txtStatus;
            txtStatus=new UIControls(this).createTextView(this,CENTER,1.25,25,0,0,0);

            //create the arming switch
            ArmingSwitch armingSwitch=new UIControls(this).drawArmingSwitch();
            //OnTextChanged increments/decrements the number of channels armed so the next button can be displayed correctly
            setStatusOnTextChanged(txtStatus,armingSwitch);
            //register the OnClickListener for the arming button
            armingSwitch.setOnCheckedChangeListener(new ArmingSwitchChangeListener());

            //create the commands
            ArmCommand armCommand= new ArmCommand(new ServerConnectionFactory(txtStatus,launcherIpAddress,channel));
            DisarmCommand disarmCommand= new DisarmCommand(new ServerConnectionFactory(txtStatus,launcherIpAddress,channel));
            StatusCommand statusCommand=new StatusCommand(new ServerConnectionFactory(txtStatus,launcherIpAddress,channel));

            //setup the invoker
            invoker.setActionCommands(channel,armCommand,disarmCommand,statusCommand);

            //setup the arming switch with a reference to the invoker and channel so that
            //the invoker can be called on the OnCheckedChangeListener when button is pressed
            armingSwitch.setInvoker(invoker);
            armingSwitch.setChannel(channel);

            //add components to the table
            row.addView(channelLabel);
            row.addView(armingSwitch);
            row.addView(txtStatus);
            channelsTable.addView(row);



        }

    }


}
