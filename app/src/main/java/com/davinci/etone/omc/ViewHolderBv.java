package com.davinci.etone.omc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ViewHolderBv extends RecyclerView.Adapter<ViewHolderBv.viewHolder> {
    Context context;
    ArrayList<Bv> list;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();

    public ViewHolderBv(Context context, ArrayList<Bv> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderBv.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_bv,viewGroup,false);
        return new ViewHolderBv.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderBv.viewHolder viewHolder, int position) {
        final Bv bv=list.get(position);
        viewHolder.bv_name.setText(bv.getBv_name().charAt(0)+bv.getBv_name().substring(1)
                .toLowerCase());
        int count1=0,count2=0,count3=0,count4=0;
        if(!bv.getBv_vol1_name().equals("none") & !bv.getBv_vol1_name().isEmpty() )
            count1=1;
        if(!bv.getBv_vol2_name().equals("none") & !bv.getBv_vol2_name().isEmpty() )
            count2=1;
        if(!bv.getBv_vol3_name().equals("none") & !bv.getBv_vol3_name().isEmpty() )
            count3=1;
        if(!bv.getBv_vol4_name().equals("none") & !bv.getBv_vol4_name().isEmpty() )
            count4=1;
        int count=count1+count2+count3+count4;
        viewHolder.bv_vol.setText("Volontaires: "+count);
        viewHolder.bv_com.setText("Commune : "+bv.getBv_commune());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),Activity_bv.class);
                intent.putExtra("Bv_name+com", bv.getBv_name()+":"+bv.getBv_commune()+":"+bv.getBv_dep());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        com.github.siyamed.shapeimageview.RoundedImageView imageViewBackground;
        TextView bv_name,bv_com,bv_vol;
        ProgressBar progressBar;
        RelativeLayout container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.container);
            bv_name=itemView.findViewById(R.id.bv_name);
            bv_com=itemView.findViewById(R.id.bv_com);
            bv_vol=itemView.findViewById(R.id.bv_vol);
        }
    }

    public String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
