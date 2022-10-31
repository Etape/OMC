package com.davinci.etone.omc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ViewHolderTchat extends RecyclerView.Adapter<ViewHolderTchat.viewHolder> {
    Context context;
    ArrayList<Message> list;
    public ViewHolderTchat(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderTchat.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_message,viewGroup,false);
        return new ViewHolderTchat.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderTchat.viewHolder viewHolder, int position) {
        final Message disc=list.get(position);
        FirebaseAuth auth= FirebaseAuth.getInstance();
        final FirebaseUser Ui=auth.getCurrentUser();
        final FirebaseDatabase myDB=FirebaseDatabase.getInstance();
        Query UserQuery= myDB.getReference("User");
        UserQuery.orderByKey();
        UserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User test = postSnapshot.getValue(User.class);
                    if(test.getEmail().trim().equals(Ui.getEmail().trim())){
                        viewHolder.date.setText(getDate(disc.getDate_envoi()));
                        if(disc.getEmetteur().equals(test.getId())){
                            viewHolder.container.setGravity(Gravity.END);
                            viewHolder.Message.setBackgroundResource(R.drawable.border_rect_gris_2);
                            viewHolder.user.setVisibility(View.GONE);
                        }
                        else {
                            viewHolder.user.setText(disc.getEmetteur());
                            viewHolder.container.setGravity(Gravity.LEFT);
                            viewHolder.Message.setBackgroundResource(R.drawable.border_rect_bleu);
                        }
                        viewHolder.Message.setText(disc.getContenu());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Database", "Failed to read value.", databaseError.toException());
                Log.i("Database", "Connexion error");
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView date,Message,user;
        RelativeLayout container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            user=itemView.findViewById(R.id.user);
            container=itemView.findViewById(R.id.container);
            Message=itemView.findViewById(R.id.message);

        }
    }
    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        long currentTime=System.currentTimeMillis()/1000;
        if (currentTime-time<(24*3600)) {
            String date = DateFormat.format("hh:mm", cal).toString();
            return date;
        }
        if(currentTime-time>=(24*3600) & currentTime-time<=2*(24*3600)){
            String date = "Hier";
            return date;
        }
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
    }
}
