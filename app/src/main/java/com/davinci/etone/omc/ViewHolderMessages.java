package com.davinci.etone.omc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
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

public class ViewHolderMessages extends RecyclerView.Adapter<ViewHolderMessages.viewHolder> {
    Context context;
    String type;
    ArrayList<Message> list;
    String userId;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    DatabaseReference refMes=Db.getReference().child("Message");

    public ViewHolderMessages(Context context, ArrayList<Message> list,String type,String userId) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.userId = userId;

    }

    @NonNull
    @Override
    public ViewHolderMessages.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_message,viewGroup,false);
        return new ViewHolderMessages.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderMessages.viewHolder viewHolder, int position) {
        final Message message=list.get(position);
        viewHolder.date.setText(getDate(message.getDate_envoi()));
        viewHolder.Message.setText(message.getContenu());
        if (message.getEmetteur().equals(userId)){
            viewHolder.container2.setBackgroundResource(R.drawable.border_rect_gris_2);
            viewHolder.container1.setGravity(Gravity.END);
            viewHolder.usr.setVisibility(View.GONE);
        }
        if(message.getEtat().equals("non lu")){
            message.setEtat("lu");
            refMes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i("Firebase ","Firebase users loading");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Message mes = postSnapshot.getValue(Message.class);
                        if (mes.getDate_envoi()== message.getDate_envoi() & mes.getEmetteur().equals(message.getEmetteur()))
                        {
                            message.setId(postSnapshot.getKey());
                            refMes.child(postSnapshot.getKey()).setValue(message);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(!type.equals("forum"))
            viewHolder.usr.setVisibility(View.GONE);
        else if(!message.getEmetteur().equals(userId)){
            Db.getReference().child(message.getEmetteur()).addValueEventListener(new
             ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     viewHolder.usr.setText(dataSnapshot.child("nom").getValue(String.class));
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
        }

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView date,Message,usr;
        RelativeLayout container1,container2;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            container1=itemView.findViewById(R.id.container1);
            container2=itemView.findViewById(R.id.container2);
            Message=itemView.findViewById(R.id.message);
            usr=itemView.findViewById(R.id.user);

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
        String date = DateFormat.format("dd MM", cal).toString();
        return date;
    }
}
