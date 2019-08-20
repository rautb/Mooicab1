package com.example.mooicab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class connection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        new Timer().schedule(new TimerTask(){
            public void run() {
                connection.this.runOnUiThread(new Runnable() {
                    public void run() {

                        if(!isConnected(connection.this))
                        {

                            Intent intent=new Intent(getApplicationContext(),failed.class);
                            startActivity(intent);
                            finish();

                        }
                        else{

                            Intent intent=new Intent(getApplicationContext(),Login.class);
                            startActivity(intent);
                            finish();



                        }

                    }
                });
            }
        }, 3000);



    }

    public boolean isConnected(Context c){

        ConnectivityManager cm=(ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo=cm.getActiveNetworkInfo();

        if(netinfo!=null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile!=null && mobile.isConnectedOrConnecting()) || (wifi!=null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        }else
            return false;
    }





}
