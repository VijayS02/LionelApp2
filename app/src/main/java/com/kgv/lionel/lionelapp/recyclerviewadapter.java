package com.kgv.lionel.lionelapp;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.Arrays;

public class recyclerviewadapter extends RecyclerView.Adapter<recyclerviewadapter.ViewHolder> {
    private static final String TAG = "recyclerviewadapter";
    private ArrayList<String[]> data = new ArrayList<>();
    private Context mContext;

    public recyclerviewadapter(ArrayList<String[]> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layoutlistitem,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    @TargetApi(21)
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        //System.out.println(data.get(i).length + " " + Arrays.toString(data.get(i)) );
        String[] x = data.get(i);
        holder.classCd.setText(x[0]);
        String jay= x[1] + ","  + x[2];
        if(jay.contains("today") || jay.contains("tomorrow")||jay.contains("overdue")){
            holder.dueDat.setTextColor(Color.RED);
        }
        holder.dueDat.setText(jay);

        holder.hwDat.setText(x[3].replace("~|","") );

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView text = (TextView) holder.hwDat;
                final LayoutParams params = (LayoutParams) text.getLayoutParams();
                if(params.height == ViewGroup.LayoutParams.WRAP_CONTENT || params.height == holder.wrapHeight){
                    holder.wrapHeight = holder.hwDat.getMeasuredHeight();
                    ValueAnimator anim = ValueAnimator.ofInt(text.getMeasuredHeight() , 158 );
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
                }else {


                    ValueAnimator anim = ValueAnimator.ofInt(text.getMeasuredHeight(), holder.wrapHeight);
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


                // ViewPropertyAnimator vp = new ViewPropertyAnimator(text);

                //Toast.makeText(mContext,data.get(i)[1],Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView classCd;
        TextView hwDat;
        TextView dueDat;
        RelativeLayout parentLayout;
        int wrapHeight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classCd  = itemView.findViewById(R.id.classID);
            dueDat  = itemView.findViewById(R.id.dueDateBx);
            hwDat  = itemView.findViewById(R.id.hwInf);
            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }
}
