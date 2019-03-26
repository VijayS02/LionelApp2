package com.kgv.lionel.lionelapp;

import java.util.ArrayList;

public class model {

    public static final int NORM=0;
    public static final int HEAD=1;

    public  ArrayList<Integer> type;
    public ArrayList< String[]> data;

    public model(ArrayList<Integer> typ,  ArrayList<String[]> data)
    {
        this.type=typ;
        this.data=data;

    }
}