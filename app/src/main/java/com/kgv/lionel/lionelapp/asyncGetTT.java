package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;

import static com.kgv.lionel.lionelapp.dataCollection.joinArr;


public class asyncGetTT extends AsyncTask<String,Void,Void> {
    Context context;
    WeakReference<Activity> mWeakActivity;
    public asyncGetTT(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }

    protected  Void doInBackground(String...ids){
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk/local/mis/misc/printtimetable.php?sid="+ids[0],mWeakActivity.get());
        if(data == null) {
            return null;
        }
        //System.out.print("DATA TT" + data);
        Document doc = Jsoup.parse(data);
        Elements tx = doc.select("TD.cell");
        //System.out.println(tx.text());
        String[][][] tt = new String[10][6][3];
        //System.out.println(tx.get(0).toString());
        for(int i =0;i<10;i++) {
            for(int z =0;z<6;z++) {
                //System.out.println((i*6)+z);
                //System.out.println(tx.get((i*6)+z + 1).text());
                String[] tmp = tx.get((i*6)+z + 1).text().split(" ", 3);

                if(tmp.length ==3) {

                    tt[i][z] = tmp;



                }else {
                    String[] free = {"STUDYPD","@-","Study Period -"};
                    tt[i][z] = free;
                }
                //System.out.println(tx.get((i*6)+z + 1 ).text());

            }



        }

        String returntt = "";
        String[][] tempTt = new String[tt.length][6];
        for(int day =0;day<10;day++){
            for(int period =0;period<6;period++){
                tempTt[day][period]=joinArr(",",tt[day][period]);
            }
            returntt +=  joinArr("|",tempTt[day]) + System.getProperty( "line.separator" );
        }
        returntt = returntt.substring(0, returntt.length() - (System.getProperty( "line.separator" ).length()+1));
        try {
            FileWriter fw = new FileWriter(new File(mWeakActivity.get().getFilesDir(), "tt.txt"));
            fw.write(returntt);
            System.out.println("asyncgettt.main: TT file created.");
            fw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

}
