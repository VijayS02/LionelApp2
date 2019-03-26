package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class asyncUpdateData extends AsyncTask<Void,Void,Void> {
    Context context;
    WeakReference<Activity> mWeakActivity;
    public asyncUpdateData(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }
    protected void onPreExecute(){
        //context = this.getApplicationContext();
        System.out.println("asyncUpdateData:Update data called.");
    }
    protected  Void doInBackground(Void...ids){

        try{

            Context context = mWeakActivity.get().getApplicationContext();
            System.out.println("asyncUpdateData: Waiting to get ID.");
            String id =  new asyncGetID(context,mWeakActivity.get()).execute().get();
            if(id == null){
                return null;
            }
            System.out.println("asyncUpdateData: Waiting to get data. ID collected.");
            new asyncGetTT(context,mWeakActivity.get()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,id);
            new asyncGetHw(context,mWeakActivity.get()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            new asyncGetBLT(context,mWeakActivity.get()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            System.out.println("asyncUpdateData: Homework,Bulletin and timetable collected.");
            FileWriter fw = new FileWriter(new File(mWeakActivity.get().getFilesDir(),"data.txt"));
            fw.write(id);
            fw.write("~~~~||~~~~");
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss Z");
            String strDate = mdformat.format(calendar.getTime());
            fw.write(strDate);
            fw.close();


        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try{
            ProgressBar pb = mWeakActivity.get().findViewById(R.id.progressbar);
            pb.setVisibility(View.INVISIBLE);}
        catch(Exception e){
            System.out.println("AsyncGetHw: Failed to update progressbar status.");
        }
        try{
            homeScreen.updateScreen(mWeakActivity.get());
        }catch(Exception e){

        }
    }
}
