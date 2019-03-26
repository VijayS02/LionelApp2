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


public class asyncGetHw extends AsyncTask<Void,Void,Void> {
    Context context;
    WeakReference<Activity> mWeakActivity;
    public asyncGetHw(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }

    protected  Void doInBackground(Void...urls){
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk",mWeakActivity.get());
        Document doc = Jsoup.parse(data);
        Elements tx = doc.select("div.todo");
        String[][] fin = new String[tx.size()][4];
        for(int i =0;i<tx.size();i++) {
            if(tx.get(i).select("div.span9 > div.toggler").isEmpty() == false) {
                Elements inf = tx.get(i).select("div.span3 > div > div");
                ArrayList<String> hwInf = new ArrayList<String>();
                for(int z = 1; z<4; z++) {
                    fin[i][z-1] = inf.get(z).text();
                }

                //System.out.println(hwInf.toString());
                fin[i][3] = tx.get(i).select("div.span9 > div > div[style*=display:none]").text() ;


            }
            else {
                Elements inf = tx.get(i).select("div.span3 > div > div");
                ArrayList<String> hwInf = new ArrayList<String>();
                for(int z = 1; z<4; z++) {
                    fin[i][z-1] = inf.get(z).text();
                }
                //System.out.println(hwInf.toString());
                fin[i][3] = tx.get(i).select("div.span9").text();
                //System.out.println(tx.get(i).select("div.span3 > div > div > input[data-click]").attr("data-click")+ "\n");
            }
            //System.out.println(Arrays.toString(fin[i]));

        }
        String returnHw = "";
        for(String[] x: fin){
            returnHw+=dataCollection.joinArr("~|~",x) + System.getProperty( "line.separator" );
        }
        returnHw = returnHw.substring(0, returnHw.length() - (System.getProperty( "line.separator" ).length()+2));
        try {
            FileWriter fw = new FileWriter(new File(mWeakActivity.get().getFilesDir(), "hw.txt"));
            fw.write(returnHw);
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
