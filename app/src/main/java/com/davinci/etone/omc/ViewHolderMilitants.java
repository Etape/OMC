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

public class ViewHolderMilitants extends RecyclerView.Adapter<ViewHolderMilitants.viewHolder> {
    Context context;
    ArrayList<User> list;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();

    public ViewHolderMilitants(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderMilitants.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_personnel,viewGroup,false);
        return new ViewHolderMilitants.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderMilitants.viewHolder viewHolder, int position) {
        final User user=list.get(position);
        viewHolder.person_name.setText(user.getNom()+" "+ user.getPrenom());
        DatabaseReference refBv=Db.getReference().child("Preinscription");
        final int[] couverture = {0};
        final int[] couvertureTotal = { 0 };

        refBv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Preinscription pre=postSnapshot.getValue(Preinscription.class);
                    if (pre.getParrain().equals(user.getId())){
                        couverture[0]++;
                    }
                }
                Db.getReference().child("Inscription").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Inscription ins=postSnapshot.getValue(Inscription.class);
                            if (ins.getParrain().equals(user.getId())){
                                couverture[0]++;
                            }
                        }
                        viewHolder.person_reg.setText("Enregistrement : " +
                                ""+couverture[0]+"/"+couvertureTotal[0]);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewHolder.person_poste.setText("Poste : "+user.getType());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),Activity_personnel.class);
                intent.putExtra("pers_id", user.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView person_name,person_poste,person_commune,person_reg;
        ProgressBar progressBar;
        RelativeLayout container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.container);
            person_name=itemView.findViewById(R.id.person_name);
            person_poste=itemView.findViewById(R.id.person_poste);
            person_commune=itemView.findViewById(R.id.person_commune);
            person_reg=itemView.findViewById(R.id.person_reg);
        }
    }

}
