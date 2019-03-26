package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;


public class asyncGetBLT extends AsyncTask<Void,Void,Void> {
    Context context;
    WeakReference<Activity> mWeakActivity;
    public asyncGetBLT(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }

    protected  Void doInBackground(Void...urls){
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk/local/mis/bulletin/bulletin.php",mWeakActivity.get());
        if(data == null){
            return null;
        }
        System.out.println("asyncGetBLT: Data has been collected from lionel.");
        Document doc = Jsoup.parse(data);
        Elements tx = doc.select("div.span9");
        String[][] fin = new String[tx.size()][4];
        for(int i =0;i<tx.size();i++) {
            //System.out.println(tx.get(i).select("h4.itemheading").text());
            fin[i][0] = tx.get(i).select("h4.itemheading").text();
            fin[i][1] = tx.get(i).select("div.itemhook").text();
            fin[i][2] = tx.get(i).select("div.itemtext").text();
            fin[i][3] = tx.get(i).select("div.itemmeta").text();


        }
        String output = "";
        for(String[] x:fin){
            output+= x[0] + "~~|~~"+x[1] + "~~|~~"+x[2] + "~~|~~"+x[3] + System.getProperty( "line.separator" );
        }
        output = output.substring(0, output.length() - (System.getProperty( "line.separator" ).length()+2));
        try {
            FileWriter fw = new FileWriter(new File(mWeakActivity.get().getFilesDir(), "blt.txt"));
            fw.write(output);
            System.out.println("asyncgetHw.main: Hw file created.");
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
