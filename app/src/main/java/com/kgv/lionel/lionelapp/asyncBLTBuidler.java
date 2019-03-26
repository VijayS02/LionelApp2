package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.kgv.lionel.lionelapp.dataParseFromTxt.getBLT;

public class asyncBLTBuidler extends AsyncTask<Void,Void,String[][]> {
    Context context;
    WeakReference<Activity> mWeakActivity;
    public asyncBLTBuidler(Context context, Activity activity) {
        this.context = context.getApplicationContext();
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }
    protected  String[][] doInBackground(Void...urls){

        return getBLT("blt.txt",mWeakActivity.get());
    }

    @Override
    protected void onPostExecute(String[][] strings) {
        super.onPostExecute(strings);
        homeScreen.createBLTRC(mWeakActivity.get(),strings);
    }
}
