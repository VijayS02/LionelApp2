package com.kgv.lionel.lionelapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;


public class loginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        findViewById(R.id.loadingbar).setVisibility(View.INVISIBLE);


        final Button signIn = (Button) findViewById(R.id.loginBtn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usrName = (EditText) findViewById(R.id.ursNameBx);
                EditText pswdBox = (EditText) findViewById(R.id.pswdTxt);
                ProgressBar bar = (ProgressBar)findViewById(R.id.loadingbar);
                bar.setVisibility(View.VISIBLE);
                bar.invalidate();
                signIn.setClickable(false);
                signIn.setEnabled(false);
                signIn.invalidate();
                taskParams pars = new taskParams(usrName.getText().toString(),pswdBox.getText().toString());
                Context context = getApplicationContext();
                new freshLogin(context,loginScreen.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,pars);

                //findViewById(R.id.loadingbar).setVisibility(View.GONE);





            }

        });
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


        }
        else {
            TextView cs = (TextView) findViewById(R.id.connectionStatus);
            cs.setText("No Connection - Try restart app");
            cs.setVisibility(View.VISIBLE);
            cs.invalidate();
            signIn.setEnabled(false);
            signIn.setClickable(false);

            connected = false;
        }




    }





}


