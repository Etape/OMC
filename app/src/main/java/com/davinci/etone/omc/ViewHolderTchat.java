package com.davinci.etone.omc;

import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference refDisc = db.collection("Discussion");
        CollectionReference refMes = db.collection("Message");
        CollectionReference refUser = db.collection("User");
        refUser.whereEqualTo("email",Ui.getEmail()).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot userD : userDocs) {
                    User test = map_user(userD);
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

                }
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
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.getDefault());
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
