package com.kgv.lionel.lionelapp;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class dataParseFromTxt {
    public static void main(String [] args){
        //getHomework("data.txt");
    }

    public static String[][][] getTimetable(String fileName,Activity ac){
        try {
            FileReader fr = new FileReader(new File(ac.getFilesDir(),fileName));
            String data = "";
            int i;

                while ((i = fr.read()) != -1)
                    //System.out.print((char) i);
                    data += (char) i;
            fr.close();
            //System.out.print(Arrays.toString(rawDat));
            //String id = rawDat[0];
            ArrayList<String[]> timetable = new ArrayList<>();
            String[][][] finalTt = new String[10][6][3];
           // System.out.println(Arrays.toString(rawDat));

            String[] tempTt1 = data.split(System.getProperty( "line.separator" ));
            //System.out.println("\n"+tempTt1.length);
            for(int day =0;day<10;day++){
                //System.out.print(Arrays.toString(tempTt1));
                String[] tempTt2 = tempTt1[day].split("\\|");
                //System.out.println(Arrays.toString(tempTt2));
                for(int period =0;period<6;period++){
                    finalTt[day][period] = tempTt2[period].split(",");

                }

            }
  /*          for(int day =0; day<10;day++){
                for(int per =0;per<6;per++){
                    System.out.print(finalTt[day][per][0] + " ");

                }
                System.out.print("\n");


            }*/
            //System.out.println(Arrays.toString(finalTt[2][4]));
            return finalTt;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }
    public static String[][] getBLT(String filename,Activity ac){
        //System.out.println("getBLT:Called.");
        String[][] finalBLT;
        try {
            //FileReader fr = new FileReader(new File(ac.getFilesDir(),filename));
            String data = "";
            // System.out.println("getBLT:File opened.");
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(ac.getFilesDir(), filename)));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }

            //System.out.println("getBLT:File read.");
            //fr.close();
            data = text.toString();
            String[] temp1 = data.split(System.getProperty("line.separator"));
            //System.out.println("getBLT:Data split");
            finalBLT = new String[temp1.length][4];
            for (int hwind = 0; hwind < temp1.length; hwind++) {
                finalBLT[hwind] = temp1[hwind].split("~~\\|~~");
            }
            //System.out.println("getBLT:Data fully parsed.");
            return finalBLT;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static String[][] getHomework(String filename,Activity ac){
        String[][] finalTmp;
        try {
            FileReader fr = new FileReader(new File(ac.getFilesDir(),filename));
            String data = "";
            int i;

            while ((i = fr.read()) != -1)
                //System.out.print((char) i);
                data += (char) i;
            fr.close();
            String[] temp1 = data.split(System.getProperty( "line.separator" ));
            finalTmp = new String[temp1.length][4];
            for(int hwind = 0;hwind<temp1.length;hwind++){
                finalTmp[hwind] = temp1[hwind].split("~\\|~");
            }
            //System.out.print(Arrays.toString(finalTmp[1]));
            return finalTmp;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static String getId(String file,Activity ac)throws IOException {
        FileReader fr = new FileReader(new File(ac.getFilesDir(),file));
        String data = "";
        int i;

        while ((i = fr.read()) != -1)
            //System.out.print((char) i);
            data += (char) i;
        fr.close();
        return data.split("~~~~\\|\\|~~~~")[0];
    }
}
