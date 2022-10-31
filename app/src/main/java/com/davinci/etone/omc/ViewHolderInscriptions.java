package com.davinci.etone.omc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ViewHolderInscriptions extends RecyclerView.Adapter<ViewHolderInscriptions.viewHolder> {
    Context context;
    ArrayList<Inscription> list;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();

    public ViewHolderInscriptions(Context context, ArrayList<Inscription> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderInscriptions.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_inscription,viewGroup,false);
        return new ViewHolderInscriptions.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderInscriptions.viewHolder viewHolder, int position) {
        final Inscription user=list.get(position);
        viewHolder.person_name.setText(user.getNom()+" "+ user.getPrenom());
        viewHolder.person_commune.setText("BV : "+user.getBv());
        viewHolder.person_reg.setText("enregistre le : "+getDate(user.getCreation_date()));
        viewHolder.person_poste.setText("Sexe : "+user.getSexe()+"  Age : "+calculateAge(Long
                .getLong(user.getDate_naissance())));

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
            person_poste=itemView.findViewById(R.id.person_sexe_age);
            person_commune=itemView.findViewById(R.id.person_commune);
            person_reg=itemView.findViewById(R.id.person_date);
        }
    }

    private int calculateAge(long timebirth){
        int age = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            age= Period.between(LocalDate.ofEpochDay(timebirth),LocalDate.now()).getYears();
            return age;
        }
        else {
            Calendar calact = Calendar.getInstance(Locale.ENGLISH);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timebirth * 1000);
            calact.getTime().getYear();
            age=calact.getTime().getYear()-cal.getTime().getYear();
            return age;
        }
    }
    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String dated = android.text.format.DateFormat.format("dd-MM-yyyy", cal).toString();
        return dated;
    }

}
