package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class dataCompareWithCal {
    public static void main(String[] args){




    }
    public static String[][] getDayFromWeekDay(String day,String[][][] timeTable){
        int num = 0;
        String[] parsDat = day.split(" ");
        if(parsDat[0].equals("Week")){
            num += (Integer.parseInt(parsDat[1]) -1) *5;
            if(parsDat[2].equals("Tuesday")){
                num+=1;
            }else if(parsDat[2].equals("Wednesday")){
                num+=2;
            }else if(parsDat[2].equals("Thursday")){
                num+=3;
            }else if(parsDat[2].equals("Friday")){
                num+=4;
            }
            String[][] retstr = new String[6][5];
            for(int i =0;i<timeTable[num].length;i++){
                String[] cur = timeTable[num][i];
                String[] tmp = {cur[0],cur[1],cur[2],getTimeStart(parsDat[2],i),getTimeEnd(parsDat[2],i)};
                retstr[i] =  tmp;
            }
           return retstr;
        }else {
            String[][] hol = {{day}};
            return hol;
        }

    }
    public static String getTimeEnd(String day,int pr) {
        if (day.equals("Monday")) {
            switch (pr) {
                case 0:
                    return "9:20";
                case 1:
                    return "10:20";
                case 2:
                    return "11:30";
                case 3:
                    return "12:30";
                case 4:
                    return "14:00";
            }
        } else {
            switch (pr) {
                case 0:
                    return "9:15";
                case 1:
                    return "10:25";
                case 2:
                    return "12:15";
                case 3:
                    return "13:25";
                case 4:
                    return "15:20";
            }
        }
        return "?";
    }

        public static String getTimeStart(String day,int pr) {
            if (day.equals("Monday")) {
                switch (pr) {
                    case 0:
                        return "8:30";
                    case 1:
                        return "9:30";
                    case 2:
                        return "10:40";
                    case 3:
                        return "11:30" ;
                    case 4:
                        return "13:10";
                }
            } else {
                switch (pr) {
                    case 0:
                        return "8:15";
                    case 1:
                        return "9:25";
                    case 2:
                        return "11:15";
                    case 3:
                        return "12:25";
                    case 4:
                        return "14:20";
                }

            }
            return "?";
        }

    public static int getCurPeriod(String nextDay){
        SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
        SimpleDateFormat wkday = new SimpleDateFormat("E");
        try {
            Date comp = mdformat.parse(nextDay);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            Date cur =  calendar.getTime();
            long diff = comp.getTime() - cur.getTime();
            if(diff>0){
                return 0;

            }else{
                if(wkday.format(mdformat.parse(nextDay)).equals("Mon")==false){
                    if (diff > -33300000) {
                        return 0;
                    } else if (diff > -37500000) {
                        return 1;
                    } else if (diff > -44100000) {
                        return 2;
                    } else if (diff > -48300000) {
                        return 3;
                    } else if (diff > -55200000) {
                        return 4;
                    }
                }else {
                    if (diff > -33600000) {
                        return 0;
                    }else if (diff > -37200000) {
                        return 1;
                    } else if (diff > -41400000) {
                        return 2;
                    } else if (diff > -45000000) {
                        return 3;
                    } else if (diff > -50400000) {
                        return 4;
                    }
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;

    }
    public static String[] compareTTwithFile(Activity ac,int inc){
        InputStream is = ac.getResources().openRawResource(R.raw.dat);
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer, "UTF8");
            String theString = writer.toString();
            String[] raw1 = theString.split("\n");
            int count = 0;
            String[][] finalDat = new String[raw1.length][9];
            for(String entry:raw1){
                if(entry.split(",")[0].equals("SUMMARY")==false) {


                    finalDat[count] = entry.split(",");
                    count++;
                }
            }
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
            SimpleDateFormat wdky = new SimpleDateFormat("E");
            Date cur =  calendar.getTime();
            count = 0;
            for(String[] ech : finalDat){

                //System.out.print(Arrays.toString(ech));
                Date comp = mdformat.parse(ech[1]);
                //System.out.print(Arrays.toString(finalDat[count]) + " " + (comp.getTime() - cur.getTime()) + "\n");

                long diff = comp.getTime() - cur.getTime();
                //System.out.print("Time diff = " + diff + " Comp time is: " + comp.getHours() + " " + comp.getMinutes());
                //System.out.println(diff);
                if(wdky.format(comp).equals("Mon")){
                    if (diff >= -50400000) {

                        //System.out.print(Arrays.toString(finalDat[count-1]));
                        //System.out.print("Time diff = " + diff + " time is: " + comp.getDate() + " " + comp.getMonth());
                        //System.out.println(Arrays.toString(finalDat[count]));
                        try {
                            return finalDat[count+inc];
                        }catch(Exception e){
                            return finalDat[count];
                        }
                    }
                }else {
                    if (diff >= -55200000) {

                        //System.out.print(Arrays.toString(finalDat[count-1]));
                        //System.out.print("Time diff = " + diff + " time is: " + comp.getDate() + " " + comp.getMonth());
                        //System.out.println(Arrays.toString(finalDat[count]));
                        try {
                            return finalDat[count+inc];
                        }catch(Exception e){
                            return null;
                        }
                    }
                }
                count++;

            }





        }
        catch(Exception e){
            e.printStackTrace();
        }



        return null;

    }
}
