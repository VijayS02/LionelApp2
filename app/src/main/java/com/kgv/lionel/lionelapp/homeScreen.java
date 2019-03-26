package com.kgv.lionel.lionelapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.kgv.lionel.lionelapp.dataParseFromTxt.getBLT;

public class homeScreen extends AppCompatActivity {

    //private TextView mTextMessage;

    @Override
    protected void onResume() {
        super.onResume();
        updateScreen(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    findViewById(R.id.hwLog).setVisibility(View.INVISIBLE);
                    findViewById(R.id.tt).setVisibility(View.VISIBLE);
                    findViewById(R.id.blt).setVisibility(View.INVISIBLE);
                   // mTextMessage.setText(R.string.title_home);
                    //updateScreen(homeScreen.this);
                    //updateScreen(homeScreen.this);
                    return true;
                case R.id.navigation_dashboard:
                    findViewById(R.id.hwLog).setVisibility(View.VISIBLE);
                    findViewById(R.id.tt).setVisibility(View.INVISIBLE);
                    findViewById(R.id.blt).setVisibility(View.INVISIBLE);
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    findViewById(R.id.hwLog).setVisibility(View.INVISIBLE);
                    findViewById(R.id.tt).setVisibility(View.INVISIBLE);
                    findViewById(R.id.blt).setVisibility(View.VISIBLE);
                   // mTextMessage.setText(R.string.title_notifications);
                    //createBLTRC(homeScreen.this);
                    return true;
            }
            return false;
        }
    };


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        //TESTING ONLY, CAUSES LOGINS TO FAIL EVERY TIME ( RESETS TEXT FILE CONTENTS )
        /*try{
            FileWriter fw = new FileWriter(new File(homeScreen.this.getFilesDir(),"cookie.txt"));
            try {
                fw.write("asdASKDJHKJHsd");
                System.out.println("Cookies successfully jumbled.");
            } finally {
                fw.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            FileWriter fw = new FileWriter(new File(homeScreen.this.getFilesDir(),"loginFo.txt"));
            try {
                fw.write("asdASKDJHKJHsd");
                System.out.println("Login successfully jumbled.");
            } finally {
                fw.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }*/
        System.out.println("Homescreen: Getfilesdir result"+this.getFilesDir());

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);







        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.btmNavig);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //TEST ONLY, MOVE LATER

        new asyncGetBLT(getApplicationContext(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss Z");
            String strDate = mdformat.format(calendar.getTime());
            System.out.println("Homescreen: Last update: " + strDate);
            Date date = calendar.getTime();
            try {
                FileReader fr = new FileReader(new File(homeScreen.this.getFilesDir(),"data.txt"));
                String data = "";
                int i;

                while ((i = fr.read()) != -1)
                    //System.out.print((char) i);
                    data += (char) i;
                fr.close();
                String[] rawDat = data.split("~~~~\\|\\|~~~~");

                date = mdformat.parse(rawDat[1]);
                Log.d("Updates:","Last update: " + date);
                Log.d("Home", "onCreate: TEXT FILE CONTENTS:" + data);
            }catch(FileNotFoundException e){
                Intent intent = new Intent(this, loginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                this.startActivity(intent);
                this.finish();
            }catch(ParseException e){
                Context context = getApplicationContext();
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                    new asyncUpdateData(context,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    new asyncGetHw(context,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
            catch(Exception e){
                e.printStackTrace();
                return;
            }
            long diff = calendar.getTime().getTime() - date.getTime();
            Log.d("Updates:","Update Difference: " + diff);
            if(((diff * 0.001) /60 )> 5 || diff<0){
                System.out.println("Homescreen: Diff: " + diff);
                ProgressBar pb = findViewById(R.id.progressbar);
                pb.setVisibility(View.VISIBLE);
                Context context = getApplicationContext();
                try {

                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                        new asyncUpdateData(context,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        new asyncGetHw(context,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                //updateScreen(this);
            }

        }
        else {
            System.out.print("Homescreen:No connection.");

        }

        RecyclerView r = findViewById(R.id.hwLog);
        final BottomNavigationView bw = findViewById(R.id.btmNavig);
        r.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && bw.isShown()) {
                   /* ObjectAnimator animation = ObjectAnimator.ofFloat(bw, "translationY", 100f);
                    animation.setDuration(1000);
                    animation.start();*/
                    bw.setVisibility(View.INVISIBLE);
                } else if (dy < 0 ) {
                    /*ObjectAnimator animation = ObjectAnimator.ofFloat(bw, "translationY", -100);
                    animation.setDuration(1000);
                    animation.start();*/
                    bw.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        RecyclerView r1 = findViewById(R.id.blt);
        r1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && bw.isShown()) {
                   /* ObjectAnimator animation = ObjectAnimator.ofFloat(bw, "translationY", 100f);
                    animation.setDuration(1000);
                    animation.start();*/
                    bw.setVisibility(View.INVISIBLE);
                } else if (dy < 0 ) {
                    /*ObjectAnimator animation = ObjectAnimator.ofFloat(bw, "translationY", -100);
                    animation.setDuration(1000);
                    animation.start();*/
                    bw.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        RecyclerView r2 = findViewById(R.id.tt);
        r2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && bw.isShown()) {
                   /* ObjectAnimator animation = ObjectAnimator.ofFloat(bw, "translationY", 100f);
                    animation.setDuration(1000);
                    animation.start();*/
                    bw.setVisibility(View.INVISIBLE);
                } else if (dy < 0 ) {
                    /*ObjectAnimator animation = ObjectAnimator.ofFloat(bw, "translationY", -100);
                    animation.setDuration(1000);
                    animation.start();*/
                    bw.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        try{
            updateScreen(this);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void createBLTRC(Activity ac,String[][] inf){


    }
    public static void updateScreen(Activity ac){
        try {
            //TextView txtvw = (TextView) ac.findViewById(R.id.timeTable);
            RecyclerView rv = ac.findViewById(R.id.hwLog);

            String[][] hw = dataParseFromTxt.getHomework("hw.txt",ac);
            ArrayList<String[]> hwRes = new ArrayList<>();
            for(String[] x:hw){
                hwRes.add(x);
            }
            recyclerviewadapter adapter = new recyclerviewadapter(hwRes,ac);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(ac));
            //txtvw.setText(hwRes);
            //hwLog.setText(hwRes);
            //txtvw.setText(hwRes);
            //hwLog.setText(hwRes);
            RecyclerView tt = ac.findViewById(R.id.tt);
            ArrayList<String[]> ttArr = new ArrayList<>();
            ArrayList<Integer> tmx = new ArrayList<>();
            SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
            SimpleDateFormat txt = new SimpleDateFormat("dd MMM");
            for(int i =0;i<14;i++){
                String[] ttdat = dataCompareWithCal.compareTTwithFile(ac,i);
                if(ttdat==null){
                    break;
                }
                //String[] ttdat = dataCompareWithCal.compareTTwithFile(ac,0);
                String dateText = txt.format(mdformat.parse(ttdat[1]));
                String[][] tets = dataCompareWithCal.getDayFromWeekDay(ttdat[0],dataParseFromTxt.getTimetable("tt.txt",ac));
                int xz = 0;
                String[] tmp = {dateText+ ", " +ttdat[0]};
                ttArr.add(tmp);
                tmx.add(1);
                for(String[] y:tets){
                    if(xz==5){
                        if(y[0].equals("STUDYPD")==false){
                            ttArr.add(y);
                            tmx.add(0);
                        }
                    }else {
                        ttArr.add(y);
                        tmx.add(0);
                        xz++;
                    }


                }
            }
            recyclerviewadapterTT ada = new recyclerviewadapterTT(ac.getApplicationContext(),new model(tmx,ttArr));
            tt.setAdapter(ada);
            tt.setLayoutManager(new LinearLayoutManager(ac));



           // TextView tt = (TextView) ac.findViewById(R.id.timeTable);
            String[] ttdat = dataCompareWithCal.compareTTwithFile(ac,0);
            //String[] ttdat = dataCompareWithCal.compareTTwithFile(ac,0);
            //String dateText = txt.format(mdformat.parse(ttdat[1]));
           String[][] store = dataCompareWithCal.getDayFromWeekDay(ttdat[0],dataParseFromTxt.getTimetable("tt.txt",ac));

            //new asyncBLTBuidler(ac.getApplicationContext(),ac).execute();


            RecyclerView rv1 = ac.findViewById(R.id.blt);

            //String[][] blt = ;

            ArrayList<String[]> bltres = new ArrayList<>();
            String[][] blt =  dataParseFromTxt.getBLT("blt.txt",ac);
            System.out.println(Arrays.toString(blt[0]));
            for(String[] x:blt){
                bltres.add(x);
            }
            recyclerviewadapBLT adapter1 = new recyclerviewadapBLT(bltres,ac);
            rv1.setAdapter(adapter1);
            rv1.setLayoutManager(new LinearLayoutManager(ac));
            //System.out.println("TESTING: " + Arrays.toString([0]));

            //tt.setText(dataCompareWithCal.compareTTwithFile(ac) );
            //System.out.print(dataCompareWithCal.compareTTwithFile(ac));
            //RecyclerView hwlog = (RecyclerView) findViewById(R.id)
            //tv.setText(dataParseFromTxt.getId("data.txt",homeScreen.this));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
