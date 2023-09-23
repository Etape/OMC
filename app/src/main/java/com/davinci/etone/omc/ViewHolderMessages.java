package com.davinci.etone.omc;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference refDisc = db.collection("Discussion");
        CollectionReference refMes = db.collection("Message");
        CollectionReference refUser = db.collection("User");
        if (message.getEmetteur().equals(userId)){
            viewHolder.container2.setBackgroundResource(R.drawable.border_rect_gris_2);
            viewHolder.container1.setGravity(Gravity.END);
            viewHolder.usr.setVisibility(View.GONE);
        }
        else {
            viewHolder.container2.setBackgroundResource(R.drawable.border_rect_bleu);
            viewHolder.container1.setGravity(Gravity.START);
            viewHolder.usr.setVisibility(View.GONE);
        }
        if(!type.equals("forum"))
            viewHolder.usr.setVisibility(View.GONE);
        else if(!message.getEmetteur().equals(userId)){
            refUser.whereEqualTo("id",message.getEmetteur()).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    for (QueryDocumentSnapshot userD : userDocs) {
                        viewHolder.usr.setText(map_user(userD).getNom());
                        break;
                    }
                }
             });
        }
        if(message.getEmetteur().equals("0") & message.getContenu().contains("Id :")){
            String mil_id=message.getContenu().split("Id :")[1];
            viewHolder.container1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(view.getContext(),Activity_personnel.class);
                    intent.putExtra("pers_id", mil_id);
                    view.getContext().startActivity(intent);
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
        java.util.Calendar calcurrent = java.util.Calendar.getInstance(Locale.getDefault());
        calcurrent.setTimeInMillis(currentTime * 1000);
        int dayTime= cal.get(Calendar.DAY_OF_WEEK);
        int daycur= calcurrent.get(Calendar.DAY_OF_WEEK);
        Log.i("Date","Date day of the week "+daycur);
        if (currentTime-time<(24*3600) & daycur==dayTime) {
            String date = DateFormat.format("kk:mm", cal).toString();
            return date;
        }
        if(daycur-dayTime<2 & currentTime-time<2*(24*3600) ){
            String date = DateFormat.format("kk:mm", cal).toString();
            date = "Hier "+ date;
            return date;
        }
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        String hour = DateFormat.format("hh:mm", cal).toString();
        date = date+" "+hour;
        return date;
    }
    public User map_user(QueryDocumentSnapshot docU){
        User user1=new User();
        user1.id=docU.getString("id");
        user1.cni=docU.getString("cni");
        user1.activite= Math.toIntExact(docU.getLong("activite"));
        user1.code=docU.getString("code");
        user1.commune=docU.getString("commune");
        user1.comite_base=docU.getString("comite_base");
        user1.creation_date=docU.getLong("creation_date");
        user1.date_naissance=docU.getString("date_naissance");
        user1.departement=docU.getString("departement");
        user1.departement_org=docU.getString("departement_org");
        user1.email=docU.getString("email");
        user1.matricule_parti=docU.getString("matricule_parti");
        user1.nom=docU.getString("nom");
        user1.parrain=docU.getString("parrain");
        user1.pays=docU.getString("pays");
        user1.parti=docU.getString("parti");
        user1.password=docU.getString("password");
        user1.region=docU.getString("region");
        user1.prenom=docU.getString("prenom");
        user1.sexe=docU.getString("sexe");
        user1.sympatisant=docU.getString("sympatisant");
        user1.type=docU.getString("type");
        user1.sous_comite_arr=docU.getString("sous_comite_arr");
        user1.telephone=docU.getString("telephone");
        return user1;
    }
}
