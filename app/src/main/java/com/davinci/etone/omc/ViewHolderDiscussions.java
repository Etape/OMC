package com.davinci.etone.omc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ViewHolderDiscussions extends RecyclerView.Adapter<ViewHolderDiscussions.viewHolder> {
    Context context;
    ArrayList<Discussion> list;
    String user_id;

    public ViewHolderDiscussions(Context context, ArrayList<Discussion> list,String user_id) {
        this.context = context;
        this.list = list;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ViewHolderDiscussions.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_forum,viewGroup,false);
        return new ViewHolderDiscussions.viewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDiscussions.viewHolder viewHolder, int position) {
        final Discussion disc=list.get(position);
        FirebaseDatabase Db=FirebaseDatabase.getInstance();
        viewHolder.date.setText(getDate(disc.getLast_date()));
        String text=""+disc.getTitle().charAt(0) +""+disc.getTitle().charAt(1);
        viewHolder.icon_txt.setText(text.toUpperCase());
        viewHolder.lastMessage.setText(disc.getLast_message());
        viewHolder.interlocuteur.setText(disc.getTitle());
        if (disc.getType().equals("sms")){
            viewHolder.icon_txt.setTextColor(R.color.colorPrimaryDark);
            viewHolder.icon_txt.setBackgroundResource(R.drawable.round_letter);
        }
        else if (disc.getType().equals("tchat"))
            viewHolder.icon_txt.setBackgroundResource(R.drawable.round_letter_tchat);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(view.getContext(),Activity_tchat.class);
                intent.putExtra("Id_disc",disc.getId());
                intent.putExtra("user_id",user_id);
                intent.putExtra("disc_title",disc.getTitle());
                intent.putExtra("type",disc.getType());
                view.getContext().startActivity(intent);
            }
        });
        if (user_id.equals(disc.getLast_writer()))
            viewHolder.last_user.setText("Vous:");
        else if (!user_id.equals(disc.getLast_writer()) & disc.getType().equals("simple"))
            viewHolder.last_user.setText("");
        else if(!disc.getLast_writer().equals("none"))
        Db.getReference().child("User").child(disc.getLast_writer())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("FireBase","User caracts 1"+dataSnapshot.getKey());
                        User u=dataSnapshot.getValue(User.class);
                        viewHolder.last_user.setText(u.getNom()+" :");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView date,interlocuteur,lastMessage,icon_txt,last_user;
        ImageView image;
        RelativeLayout container;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            icon_txt=itemView.findViewById(R.id.icon_txt);
            lastMessage=itemView.findViewById(R.id.last_message);
            last_user=itemView.findViewById(R.id.last_user);
            interlocuteur=itemView.findViewById(R.id.Interlocutor);
            container=itemView.findViewById(R.id.container);
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
        String date = DateFormat.format("dd/MM", cal).toString();
        return date;
    }
}
