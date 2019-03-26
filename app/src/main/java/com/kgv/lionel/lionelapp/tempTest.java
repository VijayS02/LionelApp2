package com.kgv.lionel.lionelapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class tempTest {
    public static void main(String[] args){
        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss Z");
        String strDate = mdformat.format(calendar.getTime());
        System.out.println(strDate);
        Date date = calendar.getTime();
        try {
            date = mdformat.parse("2018/25/11 17:52:33 +0800");
        }catch(Exception e){
            e.printStackTrace();
        }
        long diff = calendar.getTime().getTime() - date.getTime();

        System.out.println((diff * 0.001 /60 ));
        //display(strDate);
    }
}
