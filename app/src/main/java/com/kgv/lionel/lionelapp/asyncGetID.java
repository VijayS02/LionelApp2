package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;

public class asyncGetID extends AsyncTask<Void,Void,String> {
    Context context;
    WeakReference<Activity> mWeakActivity;
    public asyncGetID(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }

    protected  String doInBackground(Void...ids){
        System.out.println("AsyncGETID:Getting ids.");
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk",mWeakActivity.get());
        if(data == null) {
            return null;
        }

        Document doc = Jsoup.parse(data);
        Elements links = doc.select("div.logininfo > a[href]");
        String lnk = links.attr("href");
        System.out.print("lnk=" + lnk+"\n");
        String id = lnk.substring(lnk.indexOf("id=")+3,lnk.length());
        return id;
    }

}
