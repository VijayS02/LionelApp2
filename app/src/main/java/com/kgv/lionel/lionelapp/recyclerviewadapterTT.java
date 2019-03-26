package com.kgv.lionel.lionelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class recyclerviewadapterTT extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LAYOUT_ONE= 0;
    private static final int LAYOUT_TWO= 1;
    ArrayList<String[]> data;
    ArrayList<Integer> typ;

    public recyclerviewadapterTT(Context context, model dat)
    {
        this.data = dat.data;
        this.typ = dat.type;

    }


    @Override
    public int getItemViewType(int position)
    {
        return this.typ.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType==LAYOUT_ONE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitemtt,parent,false);
            viewHolder = new ViewHolderOne(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sectionheader,parent,false);
            viewHolder= new ViewHolderTwo(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(data.get(position) == null){
            return;
        }
        if(holder.getItemViewType()== LAYOUT_ONE)
        {
            ViewHolderOne hold = (ViewHolderOne) holder;
            String[] cur = data.get(position);


            try {
                if (cur.length != 5) {

                    String x = cur[0];
                    hold.className.setText(x);
                    hold.clinf.setText("");
                    hold.tstart.setText("00:00");
                    hold.tend.setText("23:59");
                    return;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            hold.className.setText(cur[2]);
            hold.clinf.setText(cur[1].replace("@",""));
            hold.tstart.setText(cur[3]);
            hold.tend.setText(cur[4]);
        }
        else {

            ViewHolderTwo vaul = (ViewHolderTwo) holder;
            vaul.tx.setText(data.get(position)[0]);
            //vaultItemHolder.name.setText(displayText);


        }

    }

    //****************  VIEW HOLDER 1 ******************//

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView className;
        TextView clinf;
        TextView tstart;
        TextView tend;
        RelativeLayout parentLayout;
        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            className  = itemView.findViewById(R.id.className);
            clinf  = itemView.findViewById(R.id.classInfo);
            tstart = itemView.findViewById(R.id.startTime);
            tend = itemView.findViewById(R.id.endtime);
            parentLayout = itemView.findViewById(R.id.parent_layoutTT);
        }
    }


    //****************  VIEW HOLDER 2 ******************//

    public class ViewHolderTwo extends RecyclerView.ViewHolder{
        TextView tx;
        public ViewHolderTwo(View itemView) {
            super(itemView);
            tx = itemView.findViewById(R.id.secHd);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}