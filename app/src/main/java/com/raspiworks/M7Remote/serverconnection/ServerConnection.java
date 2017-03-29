package com.raspiworks.M7Remote.serverconnection;

/**
 * Creates the receiver object for the commands to act upon. It calls the server once it receives a
 * command and updates the status if a textView has been passed once a response is received back from
 * the server. Since it is an AsyncTask a new thread is created so the UI thread isn't affected.
 *
 * Author: David Hinchliffe <belgoi@gmail.com>
 */
//TODO: CLEAN UP THIS CLASS

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ServerConnection extends AsyncTask<String,Void,String> {
    private HttpURLConnection serverConnection;

    private int channel=-1;
    private TextView responseView=null;
    private StringBuilder url=new StringBuilder();
    private String serverContext=":8080/RasPiWorks/M7Server?";
    private final String USER_AGENT = "Mozilla/5.0";
    //private String command;

    protected ServerConnection(TextView view,String serverIpAddress,int channel){
        this.responseView=view;
       // this.OnSetMaxChannels=null;
        //this.serverIpAddress=serverIpAddress;
        this.channel=channel;
        url.append("http://");
        url.append(serverIpAddress);
        url.append(serverContext);
    }

    protected ServerConnection(TextView view,String serverIpAddress){
        url.append("http://");
        url.append(serverIpAddress);
        url.append(serverContext);
        this.responseView=view;
       // this.OnSetMaxChannels=null;
    }
    protected ServerConnection(String serverIpAddress){
        this.responseView=null;      
       // url.append("http://" + serverIpAddress + serverContext);
        url.append("http://");
        url.append(serverIpAddress);
        url.append(serverContext);
    }

    //  public ServerConnection(){}

    @Override
    protected String doInBackground(String...args){
        //String url="http://" + serverIpAddress +":8080/RasPiWorks/LauncherUI?reset";
        //arguments passed in are ipAddress,command, and channel
        //TODO: add a check for each one in case they are passed in varying orders.
        String response=new String();
        //command=args[0];
        if(args.length==1 && channel==-1)
            url.append(args[0]);
        else if(args.length==1 && channel >=0)
            url.append(args[0] +"=" + channel);

        try {

            //iniatilize URL & set http parameters
            URL address = new URL(url.toString());
            serverConnection=(HttpURLConnection)address.openConnection();
            //serverConnection.setRequestMethod("POST");
            serverConnection.setRequestMethod("GET");
            serverConnection.setRequestProperty("User-Agent",USER_AGENT);
            serverConnection.setConnectTimeout(8*1000);
            serverConnection.setReadTimeout(8*1000);
            int responseCode= serverConnection.getResponseCode();
            //serverConnection.setRequestProperty("Accept-Language","en-US,en;q=0.5");
            if(responseCode==HttpURLConnection.HTTP_OK)
                response=getServerResponse();
            else if(responseCode==HttpURLConnection.HTTP_CLIENT_TIMEOUT){
                response="Server Timed Out";
            }
            else if(responseCode==HttpURLConnection.HTTP_BAD_REQUEST){
                response="Invalid Parameters Sent";
            }
            else if(responseCode==HttpURLConnection.HTTP_NOT_FOUND){
                response="Bad Command";
            }
        }
        catch(MalformedURLException e) {
            response="Invalid Url";
        }
        catch(SocketTimeoutException e){
            response="Connection Timed out";
        }
        catch (IOException e){
            response="Invalid URL";
        }

        return response;
    }
    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        if (responseView !=null)
            responseView.setText(s);
        //else if(OnSetMaxChannels !=null && s!=null)
         //   OnSetMaxChannels.OnSetMaxChannels(s);
        Log.e("Response","" + s );
    }
    private String getServerResponse()
    {   StringBuilder buffer=new StringBuilder();

        try(BufferedReader response=new BufferedReader(new InputStreamReader(serverConnection.getInputStream()))){
            String inputLine;

            while((inputLine=response.readLine())!=null){
                buffer.append(inputLine);
            }

        }catch(IOException e){
            //TODO: handle this exception
        }
        return buffer.toString();
    }

}

