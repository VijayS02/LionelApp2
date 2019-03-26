package com.kgv.lionel.lionelapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.Arrays;

public class recyclerviewadapBLT extends RecyclerView.Adapter<recyclerviewadapBLT.ViewHolder> {
    private static final String TAG = "recyclerviewadapBLT";
    private ArrayList<String[]> data = new ArrayList<>();
    private Context mContext;

    public recyclerviewadapBLT(ArrayList<String[]> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_bulletinitem,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        if(data.get(i)==null ||  getItemCount() < i){
            return;
        }
        try {
            Log.d(TAG, "onBindViewHolderBLT: called.");
            //System.out.println(data.get(i).length + " " + Arrays.toString(data.get(i)) );
            final String[] x = data.get(i);
            holder.header.setText(x[0]);
            holder.inf.setText(x[1] + "\n\n" + x[2] + "\n\n" + x[3]);

            // holder.hwDat.setText(x[3].replace("~|","") );
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TextView text = (TextView) holder.inf;
                    final LayoutParams params = (LayoutParams) text.getLayoutParams();
                    if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT || params.height == holder.wrapHeight) {
                        if (holder.wrapHeight == Integer.MIN_VALUE) {
                            holder.wrapHeight = text.getMeasuredHeight();
                        }
                        ValueAnimator anim = ValueAnimator.ofInt(text.getMeasuredHeight(), 60);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                int val = (Integer) valueAnimator.getAnimatedValue();
                                ViewGroup.LayoutParams layoutParams = text.getLayoutParams();
                                layoutParams.height = val;
                                text.setLayoutParams(layoutParams);
                            }

                        });
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                text.setText(x[1]);
                            }
                        });
                        anim.setDuration(500);
                        anim.start();


                    } else {
                        text.setText(x[1] + "\n\n" + x[2] + "\n\n" + x[3]);
                        ValueAnimator anim = ValueAnimator.ofInt(60, holder.wrapHeight);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                int val = (Integer) valueAnimator.getAnimatedValue();
                                ViewGroup.LayoutParams layoutParams = text.getLayoutParams();
                                layoutParams.height = val;
                                text.setLayoutParams(layoutParams);
                            }
                        });
                        anim.setDuration(500);
                        anim.start();

                        //text.setTranslationZ(10);
                    }
                    text.setLayoutParams(params);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView header;
        TextView inf;
        RelativeLayout parentLayout;
        int wrapHeight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header  = itemView.findViewById(R.id.headr);
            inf  = itemView.findViewById(R.id.bltInf);
            wrapHeight= Integer.MIN_VALUE;
            parentLayout = itemView.findViewById(R.id.parent_layoutBLT);
        }
    }
}