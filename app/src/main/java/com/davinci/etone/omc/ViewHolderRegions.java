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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ViewHolderRegions extends RecyclerView.Adapter<ViewHolderRegions.viewHolder> {
    Context context;
    ArrayList<Region> list;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();

    public ViewHolderRegions(Context context, ArrayList<Region> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderRegions.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_region,viewGroup,false);
        return new ViewHolderRegions.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderRegions.viewHolder viewHolder, int position) {
        final Region region=list.get(position);
        viewHolder.reg_name.setText(region.getReg_name());
        DatabaseReference refBv=Db.getReference().child("Bv");
        final int[] couverture = {0};
        final int[] couvertureTotal = { 0 };

        for (int i=0;i<region.getUpdatedBvlist().size();i++){
            Bv bv=region.getUpdatedBvlist().get(i);
            if (bv.getBv_region().equals(region.getReg_name())){
                couvertureTotal[0]++;
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
                if(count>0){
                    couverture[0]++;
                }
            }
        }

        viewHolder.reg_couverture.setText("Couverture : " +couverture[0]+"/"+couvertureTotal[0]);
        viewHolder.reg_bv.setText("Nbre Bv : " + +couvertureTotal[0]);
        viewHolder.reg_der.setText("DER"+region.getReg_der());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),Activity_bv.class);
                intent.putExtra("reg_name", region.getReg_name());
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
        TextView reg_name,reg_der,reg_couverture,reg_bv;
        ProgressBar progressBar;
        RelativeLayout container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.container);
            reg_name=itemView.findViewById(R.id.reg_name);
            reg_der=itemView.findViewById(R.id.reg_der);
            reg_couverture=itemView.findViewById(R.id.reg_couverture);
            reg_bv=itemView.findViewById(R.id.reg_bv);
        }
    }

}
