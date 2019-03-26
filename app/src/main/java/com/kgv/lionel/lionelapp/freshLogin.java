package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;


public class freshLogin extends AsyncTask<taskParams, Integer, String> {

    Context context;
    WeakReference<Activity> mWeakActivity;
    public freshLogin(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }
    protected void onPreExecute(){
        //context = this.getApplicationContext();

    }
    @Override
    protected  String doInBackground(taskParams... params){
        String val ="";
        try{

            String usr = params[0].uname;
            String pswd = params[0].pwd;
            val = loginAuth.freshLog("https://lionel2.kgv.edu.hk/",usr,pswd,this.mWeakActivity.get());
            publishProgress(50);
            System.out.println("freshgLoginAsync: Waiting to get data.");
            //dataCollection.writeInfo(this.mWeakActivity.get());

            System.out.println("freshgLoginAsync: Data collected.");
            publishProgress(100);
        }catch(Exception e){
            e.printStackTrace();
        }

        return val;

    }

    protected void onProgressUpdate(Integer... values){

    }
    protected void onPostExecute(String result){
        try {
            new asyncUpdateData(mWeakActivity.get().getApplicationContext(), mWeakActivity.get()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.mWeakActivity.get().findViewById(R.id.loadingbar).setVisibility(View.GONE);
        Button signIn = (Button) this.mWeakActivity.get().findViewById(R.id.loginBtn);
        TextView txt = (TextView) this.mWeakActivity.get().findViewById(R.id.incorrectPwd);
        signIn.setClickable(true);
        signIn.setEnabled(true);
        if(result != null){
            super.onPostExecute(result);
            Intent intent = new Intent(context, homeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



            context.startActivity(intent);
            this.mWeakActivity.get().finish();
        }else{
            //System.out.println(result);
            txt.setText("Incorrect Username / Password");
            txt.setVisibility(View.VISIBLE);
            txt.invalidate();
        }

    }


}


