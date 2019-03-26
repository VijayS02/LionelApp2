package com.kgv.lionel.lionelapp;

import android.app.Activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;


public class dataCollection {



    public static String joinArr(String delimiter,String[] arr){
        //System.out.print(Arrays.toString(arr));
        String strRtrn ="";
                for(String x: arr){
            strRtrn += x + delimiter;
                }
        strRtrn = strRtrn.substring(0, strRtrn.length() - 1);
        return strRtrn;
    }

    public static void writeInfo(Activity ac){
        try{
        String id = getID(ac);
        String[][][] tt = getTimetable(id,ac);
        String returntt = "";
        String[][] tempTt = new String[tt.length][6];
            for(int day =0;day<10;day++){
                for(int period =0;period<6;period++){
                    tempTt[day][period]=joinArr(",",tt[day][period]);
                }
                returntt +=  joinArr("|",tempTt[day]) + System.getProperty( "line.separator" );
            }
            returntt = returntt.substring(0, returntt.length() - (System.getProperty( "line.separator" ).length()+1));
        String[][] hw = getHomework(ac);
        String returnHw = "";
        for(String[] x: hw){
                returnHw+=joinArr("~|~",x) + System.getProperty( "line.separator" );
        }
            returnHw = returnHw.substring(0, returnHw.length() - (System.getProperty( "line.separator" ).length()+2));
            //System.out.print(returnHw + "-------" + returntt);
            FileWriter fw = new FileWriter(new File(ac.getFilesDir(),"data.txt"));
                fw.write(id);
                fw.write("~~~~||~~~~");
                fw.write(returntt);
                fw.write("~~~~||~~~~");
                fw.write(returnHw);
                fw.write("~~~~||~~~~");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss Z");
                String strDate = mdformat.format(calendar.getTime());
            fw.write(strDate);
                fw.close();


        }catch(Exception e){
            e.printStackTrace();
        }



    }

    public static String getID(Activity ac) throws IOException, URISyntaxException {
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk",ac);
        if(data == null) {
            return null;
        }

        //System.out.println(data);
        if(data.contains("You are not logged in.")){
            File file = new File(ac.getFilesDir(),"cookie.txt");
            try {
                boolean deleted = file.delete();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        Document doc = Jsoup.parse(data);
        Elements links = doc.select("div.logininfo > a[href]");
        String lnk = links.attr("href");
        System.out.print("DataColection:lnk=" + lnk+"\n");
        String id = lnk.substring(lnk.indexOf("id=")+3,lnk.length());
        return id;
    }

    public static String[][][] getTimetable(String id,Activity ac) throws  IOException, URISyntaxException, XPathExpressionException, ParserConfigurationException {
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk/local/mis/misc/printtimetable.php?sid="+id,ac);
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


        return tt;
    }

    public static String[][] getHomework(Activity ac) throws  IOException, URISyntaxException{
        String data = loginAuth.getDataFromLionel("https://lionel2.kgv.edu.hk",ac);
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
        return fin;
    }




}
