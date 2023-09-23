package com.davinci.etone.omc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference refDisc = db.collection("Discussion");
        CollectionReference refMes = db.collection("Message");
        CollectionReference refUser = db.collection("User");
        viewHolder.date.setText(getDate(disc.getLast_date()));
        String text=""+disc.getTitle().charAt(0) +""+disc.getTitle().charAt(1);
        viewHolder.icon_txt.setText(text.toUpperCase());
        viewHolder.lastMessage.setText(disc.getLast_message());
        viewHolder.interlocuteur.setText(disc.getTitle());
        final String[] disc_title = {disc.getTitle()};

        if (disc.getType().equals("sms")){
            viewHolder.icon_txt.setTextColor(R.color.colorPrimaryDark);
            viewHolder.icon_txt.setBackgroundResource(R.drawable.round_letter);
            String finalDisc_title = disc_title[0];
            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(view.getContext(),Activity_tchat.class);
                    intent.putExtra("Id_disc",disc.getId());
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("disc_title", finalDisc_title);
                    intent.putExtra("type",disc.getType());
                    view.getContext().startActivity(intent);
                }
            });
        }
        else if (disc.getType().equals("forum")){
            viewHolder.icon_txt.setTextColor(R.color.colorPrimaryDark);
            viewHolder.icon_txt.setBackgroundResource(R.drawable.round_letter);
            String finalDisc_title = disc_title[0];
            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent= new Intent(view.getContext(),Activity_tchat.class);
                    intent.putExtra("Id_disc",disc.getId());
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("disc_title", finalDisc_title);
                    intent.putExtra("type",disc.getType());
                    view.getContext().startActivity(intent);
                }
            });
        }
        else if (disc.getType().equals("tchat") | disc.getType().equals("Tchat")){
            viewHolder.icon_txt.setBackgroundResource(R.drawable.round_letter_tchat);
            refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    Log.i("Firebase ","Firebase users loading");
                    User user = null,user2 = null;
                    for (QueryDocumentSnapshot postSnapshot : userDocs) {
                        User usr = map_user(postSnapshot);
                        if (usr.getId().equals(user_id))
                        {
                            user=usr;
                        }
                        else if(usr.getId().equals(disc.getInitiateur()) & !usr.getId().equals(user_id)){
                            user2=usr;
                        }
                        if(user2!=null & user!=null)
                            break;
                    }
                    if(user2!=null & disc.getTitle().equals(user.getNom())){
                        viewHolder.interlocuteur.setText(user2.getNom());
                        User finalUser = user2;
                        String text=""+finalUser.getNom().charAt(0) +""+finalUser.getNom().charAt(1);
                        disc_title[0] =finalUser.getNom();
                        viewHolder.icon_txt.setText(text.toUpperCase());
                    }
                    String finalDisc_title = disc_title[0];
                    viewHolder.container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent= new Intent(view.getContext(),Activity_tchat.class);
                            intent.putExtra("Id_disc",disc.getId());
                            if(disc.getType().toUpperCase().equals("TCHAT") & !disc.getInterlocuteur().equals(user_id) & disc.getInitiateur().equals(user_id))
                                intent.putExtra("inter_id",disc.getInterlocuteur());
                            else if(disc.getType().toUpperCase().equals("TCHAT") & disc.getInterlocuteur().equals(user_id) & !disc.getInitiateur().equals(user_id))
                                intent.putExtra("inter_id",disc.getInitiateur());
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("disc_title", finalDisc_title);
                            intent.putExtra("type",disc.getType());
                            view.getContext().startActivity(intent);
                        }
                    });

                }
            });
        }

        if (user_id.equals(disc.getLast_writer()))
            viewHolder.last_user.setText("Vous:");
        else if (disc.getLast_writer().equals("none") & (disc.getType().equals("Tchat") | disc.getType().equals("tchat")))
            viewHolder.last_user.setText("");
        else if(!disc.getLast_writer().equals("none"))
        refUser.whereEqualTo("id",user_id).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot userD : userDocs) {
                        User u = map_user(userD);
                        viewHolder.last_user.setText(u.getNom()+" :");
                }
            }
        });
        refMes.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot mesDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                int countUnread=0;
                for (QueryDocumentSnapshot mesD : mesDocs) {
                    Message mes=new Message();
                    mes.emetteur=mesD.getString("emetteur");
                    mes.recepteur=mesD.getString("recepteur");
                    mes.contenu=mesD.getString("contenu");
                    mes.disc_id=mesD.getString("disc_id");
                    mes.etat=mesD.getString("etat");
                    mes.id=mesD.getId();
                    mes.date_envoi=mesD.getLong("date_envoi");
                    if(mes.getDisc_id().equals(disc.getId()) & !mes.getEmetteur().equals(user_id) & mes.getEtat().equals("non lu"))
                        countUnread++;
                }
                if (countUnread>0 & countUnread<1000)
                    viewHolder.unread_messages.setText(""+countUnread);
                else if (countUnread>=1000)
                    viewHolder.unread_messages.setText("999+");
                else
                    viewHolder.unread_messages.setVisibility(View.GONE);
            }

        });
    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView date,interlocuteur,lastMessage,icon_txt,last_user,unread_messages;
        ImageView image;
        RelativeLayout container;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            icon_txt=itemView.findViewById(R.id.icon_txt);
            lastMessage=itemView.findViewById(R.id.last_message);
            unread_messages=itemView.findViewById(R.id.unread_messages);
            last_user=itemView.findViewById(R.id.last_user);
            interlocuteur=itemView.findViewById(R.id.Interlocutor);
            container=itemView.findViewById(R.id.container);
        }
    }
    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time * 1000);
        long currentTime=System.currentTimeMillis()/1000;
        if (currentTime-time<(24*3600)) {
            String date = DateFormat.format("kk:mm", cal).toString();
            Calendar now = Calendar.getInstance();
            int dayP = now.get(Calendar.DAY_OF_WEEK);
            int dayAnt = cal.get(Calendar.DAY_OF_WEEK);
            if(dayP!=dayAnt)
                date = "Hier";
            return date;
        }
        if(currentTime-time>=(24*3600) & currentTime-time<=2*(24*3600)){
            String date = "Hier";
            return date;
        }
        String date = DateFormat.format("dd/MM", cal).toString();
        return date;
    }
    public User map_user(QueryDocumentSnapshot docU){
        User user1=new User();
        try{
            String test=docU.getString("email");

            user1.id=docU.getString("id");
            user1.cni=docU.getString("cni");
            try {
                user1.activite = Math.toIntExact(docU.getLong("activite"));
            }
            catch (Exception e1){
                user1.activite=0;
            }
            user1.code=docU.getString("code");
            user1.commune=docU.getString("commune");
            user1.comite_base=docU.getString("comite_base");
            try {
                user1.creation_date=docU.getLong("creation_date");
            }
            catch (Exception e1){

            }
            try {
                user1.date_naissance = docU.getString("date_naissance");
            }
            catch (Exception e3){
                user1.date_naissance = ""+docU.getLong("date_naissance");
            }
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

        }
        catch (Exception e4){
            user1.prenom=docU.getString("prenom");
            user1.nom=docU.getString("nom");
            user1.id=docU.getString("id");
            user1.type=docU.getString("type");

        }
        return user1;
    }
}
