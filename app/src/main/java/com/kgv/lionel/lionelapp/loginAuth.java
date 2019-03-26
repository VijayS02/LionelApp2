package com.kgv.lionel.lionelapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class loginAuth {

    public static String getDataFromLionel(String url,Activity ac){
        String res = "";
        try{
            res = oldLog("cookie.txt",url,ac);
            if(res != "-1" && res != "" && res != null){
                return res;
            }
        }catch(FileNotFoundException e){
            System.out.println("loginAuth.getDataFromLionel: Cookies file does not exist.");
        }
        try{
            res = refreshCookie(url,"loginFo.txt",ac);
            if(res != "-1" && res != "" && res != null){
                return res;
            }
        }catch(FileNotFoundException e){
            System.out.println("loginAuth.getDataFromLionel: User Login file does not exist.");
        }
        if(ac.getClass() != loginScreen.class) {
            Intent intent = new Intent(ac, loginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            ac.startActivity(intent);
            ac.finish();
        }
        //return to  home screen
        return null;



    }



    static void disableSSLCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public static String refreshCookie(String url,String usrFl,Activity ac) throws FileNotFoundException{
        System.out.println("LoginAuth.refreshCookies: Attempting to refresh cookies.");
        try {
            disableSSLCertCheck();

        }catch(Exception e ){
            e.printStackTrace();
        }


            FileReader fr=new FileReader(new File(ac.getFilesDir(),usrFl));
            String dat = "";
            int i;
            try {
                while ((i = fr.read()) != -1)
                    //System.out.print((char) i);
                    dat += (char) i;
                fr.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            String[] usrDat = dat.split("~~\\|~~");

            String x = freshLog(url,usrDat[0],usrDat[1],ac);
            if(x!=null){
                System.out.println("LoginAuth.refreshCookies: Refresh Success");
            }else{
                System.out.println("LoginAuth.refreshCookies: Refresh Failed...!");

            }
            return x;
            //System.out.println("LoginAuth.refreshCookies: Refresh Failed...!");


        //return null;
    }

    public static String freshLog( String url ,String username, String password,Activity ac) {
        String log = "https://lionel2.kgv.edu.hk/login/index.php";
        try {

            disableSSLCertCheck();
            Connection.Response res = Jsoup.connect(log)
                    .data("username", username,"password", password)
                    .userAgent("Mozilla")
                    .method(Connection.Method.POST)
                    .ignoreHttpErrors(true)
                    .execute();
            Document doc = res.parse();
            //Elements links = doc.select("div.logininfo > a[href]");
            //String lnk = links.attr("href");
            //String id = lnk.substring(lnk.indexOf("id=")+3,lnk.length());
            if(doc.title().equals("LIONEL 2: Log in to the site")){

                System.out.println("LoginAuth.FreshLogin:Error getting data with Fresh login, Username/password may be incorrect."+username +":"+password);

                return null;
            }
            String MoodleSession = res.cookie("MoodleSession");
            Document doc1 = Jsoup.connect(url)
                    .cookie("MoodleSession", MoodleSession)
                    .get();


            try{
                FileWriter fw = new FileWriter(new File(ac.getFilesDir(),"cookie.txt"));
                try {
                    fw.write(MoodleSession);
                    System.out.println("LoginAuth.FreshLogin: Cookies to be inputted to txt file: " + MoodleSession);
                } finally {
                    fw.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            try{
                FileWriter fw = new FileWriter(new File(ac.getFilesDir(),"loginFo.txt"));
                try {
                    fw.write(username + "~~|~~" + password);
                    System.out.println("LoginAuth.FreshLogin: Info to be inputted to txt file: " +username + "~~|~~" + password );
                } finally {
                    fw.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return doc.html();

        }catch(UnknownHostException e1){
            System.out.println("LoginAuth.FreshLogin:No connection.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String oldLog(String ckFile,String url,Activity ac) throws FileNotFoundException {
        // Read cookie file to get cookie value
        // Use cookie in jsoup connection to get site given
        // return site raw html to be parsed by jsoup.
        System.out.println("LoginAuth.oldLogin: Attempting to use old cookies to login.");
        try {
            disableSSLCertCheck();
        }catch(Exception e){
            e.printStackTrace();
        }
        FileReader fr=new FileReader(new File(ac.getFilesDir(),ckFile));
        String cookie = "";
        int i;
        try {
            while ((i = fr.read()) != -1)
                //System.out.print((char) i);
                cookie += (char) i;
            fr.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(cookie);
        try {
            Document doc = Jsoup.connect(url)
                    .cookie("MoodleSession", cookie)
                    .get();
            //System.out.print(doc.title());
            if(doc.html().contains("You are not logged in.")){
                System.out.println("LoginAuth.oldLogin:Cookies failed.");
                return "-1";
            }
            System.out.println("LoginAuth.oldLogin: Old Cookies Worked!");
            return doc.html();
        }catch(UnknownHostException e1){
            System.out.println("LoginAuth.oldLogin:No connection.");
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }




}
