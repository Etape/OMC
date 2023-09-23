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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference refPre = db.collection("Preinscription");
        CollectionReference refIns = db.collection("Inscription");
        final int[] couvertureTotal = { 0 };
        final int[] couverture = { 0 };

        refPre.whereEqualTo("parrain",user.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot insD : insDocs) {
                    Preinscription pre = map_pre(insD);
                    if (pre.getParrain().equals(user.getId())){
                        couverture[0]++;
                    }
                }
                refIns.whereEqualTo("parrain",user.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot insD : insDocs) {
                            Inscription ins = map_ins(insD);
                            if (ins.getParrain().equals(user.getId())){
                                couverture[0]++;
                            }
                        }
                        viewHolder.person_reg.setText("Enregistrement : " +
                                ""+couverture[0]+"/"+couvertureTotal[0]);

                    }

                });
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
    public Inscription map_ins(QueryDocumentSnapshot insD){
        Inscription ins = new Inscription();
        ins.id = insD.getString("id");
        ins.nom = insD.getString("nom");
        ins.prenom = insD.getString("prenom");
        ins.sexe = insD.getString("sexe");
        ins.date_naissance = insD.getString("date_naissance");
        ins.telephone = insD.getString("telephone");
        ins.email = insD.getString("email");
        ins.region = insD.getString("region");
        ins.pays = insD.getString("pays");
        ins.departement = insD.getString("departement");
        ins.commune = insD.getString("commune");
        ins.bv = insD.getString("bv");
        ins.departement_org = insD.getString("departement_org");
        ins.cni = insD.getString("cni");
        ins.numero_ce = insD.getString("numero_ce");
        ins.parrain = insD.getString("parrain");
        ins.sympatisant = insD.getString("sympatisant");
        ins.creation_date = insD.getLong("creation_date");
        return ins;
    }
    public Preinscription map_pre(QueryDocumentSnapshot preD){
        Preinscription pre = new Preinscription();
        pre.id = preD.getString("id");
        pre.nom = preD.getString("nom");
        pre.prenom = preD.getString("prenom");
        pre.sexe = preD.getString("sexe");
        pre.date_naissance = preD.getString("date_naissance");
        pre.telephone = preD.getString("telephone");
        pre.email = preD.getString("email");
        pre.region = preD.getString("region");
        pre.pays = preD.getString("pays");
        pre.parti = preD.getString("parti");
        pre.matricule_parti = preD.getString("matricule_parti");
        pre.departement = preD.getString("departement");
        pre.commune = preD.getString("commune");
        pre.departement_org = preD.getString("departement_org");
        pre.cni = preD.getString("cni");
        pre.parrain = preD.getString("parrain");
        pre.sympatisant = preD.getString("sympatisant");
        pre.creation_date = preD.getLong("creation_date");
        return pre;

    }

}
