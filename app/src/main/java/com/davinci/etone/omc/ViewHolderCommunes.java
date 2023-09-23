package com.davinci.etone.omc;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewHolderCommunes extends RecyclerView.Adapter<ViewHolderCommunes.viewHolder> {
    Context context;
    ArrayList<Commune> list;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();

    public ViewHolderCommunes(Context context, ArrayList<Commune> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderCommunes.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_commune,viewGroup,false);
        return new ViewHolderCommunes.viewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderCommunes.viewHolder viewHolder, int position) {
        final Commune commune=list.get(position);
        viewHolder.commune_name.setText(commune.getCom_name());
        final int[] couverture = {0};
        final int[] couvertureTotal = { 0 };
        for (int i=0;i<commune.getUpdatedBvlist().size();i++){
            Bv bv=commune.getUpdatedBvlist().get(i);
            if (bv.getBv_commune().equals(commune.getCom_name())){
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
        viewHolder.commune_couverture.setText("Couverture : "+couverture[0]+"/"+couvertureTotal[0]);
        viewHolder.commune_bv.setText("Nbre Bv : " +couvertureTotal[0]);
        viewHolder.commune_dec.setText("DEC :"+commune.getCom_dec());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),Activity_bv.class);
                intent.putExtra("com_name", commune.getCom_name());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView commune_name,commune_dec,commune_couverture,commune_bv;
        ProgressBar progressBar;
        RelativeLayout container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.container);
            commune_name=itemView.findViewById(R.id.commune_name);
            commune_bv=itemView.findViewById(R.id.commune_bv);
            commune_dec=itemView.findViewById(R.id.commune_dec);
            commune_couverture=itemView.findViewById(R.id.commune_couverture);
        }
    }

}
