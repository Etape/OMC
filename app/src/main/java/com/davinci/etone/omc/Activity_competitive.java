package com.davinci.etone.omc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Activity_competitive extends AppCompatActivity {

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ImageView back,edit;
    EditText bv_name;
    LinearLayout container;
    RecyclerView recyclerViewactiv,recyclerViewinact;
    TextView territoire;
    ProgressBar progressBar;
    String depIntent;
    String regIntent;
    String paysIntent;
    ViewHolderCommunes viewHolderAct,viewHolderInAct;
    ArrayList<Preinscription> pres=new ArrayList<>();
    ArrayList<Inscription> ins=new ArrayList<>();

    ArrayList<Commune> ActivList=new ArrayList<>();
    ArrayList<Commune> InactivList=new ArrayList<>();
    ArrayList<User> users=new ArrayList<>();
    ArrayList<String> communes_listed_dep=new ArrayList<>();
    ArrayList<String> communes_listed_reg=new ArrayList<>();
    ArrayList<String> communes_listed_pays=new ArrayList<>();
    ArrayList<String> communes_listed_dep_ins=new ArrayList<>();
    ArrayList<String> communes_listed_reg_ins=new ArrayList<>();
    ArrayList<String> communes_listed_pays_ins=new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference refPre = db.collection("Preinscription");
    CollectionReference refIns = db.collection("Inscription");
    CollectionReference refUser = db.collection("User");
    CollectionReference refDisc = db.collection("Discussion");
    CollectionReference refHistory = db.collection("Info");
    CollectionReference refBv = db.collection("Bv");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitive);

        depIntent=getIntent().getStringExtra("dep");
        regIntent=getIntent().getStringExtra("reg");
        paysIntent=getIntent().getStringExtra("pays");

        back=findViewById(R.id.back);
        progressBar=findViewById(R.id.progressBar);
        recyclerViewactiv=findViewById(R.id.recyclerviewactiv);
        recyclerViewinact=findViewById(R.id.recyclerviewinact);

        viewHolderAct =new ViewHolderCommunes(this,ActivList);
        viewHolderInAct =new ViewHolderCommunes(this,InactivList);

        recyclerViewactiv.setAdapter(viewHolderAct);
        recyclerViewinact.setAdapter(viewHolderInAct);

        refPre.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot preDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                communes_listed_dep.clear();
                communes_listed_reg.clear();
                communes_listed_pays.clear();
                ActivList.clear();
                InactivList.clear();
                pres.clear();
                for (QueryDocumentSnapshot postSnapshot : preDocs) {
                    progressBar.setVisibility(View.VISIBLE);
                    Preinscription bvi=map_pre(postSnapshot);
                    pres.add(bvi);
                }
            }

        });
        refIns.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                communes_listed_dep_ins.clear();
                communes_listed_reg_ins.clear();
                communes_listed_pays_ins.clear();
                ActivList.clear();
                InactivList.clear();
                for (QueryDocumentSnapshot postSnapshot : insDocs) {
                    progressBar.setVisibility(View.VISIBLE);
                    Inscription bvi=map_ins(postSnapshot);
                    ins.add(bvi);
                }
                progressBar.setVisibility(View.GONE);
            }

        });
        refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                users.clear();
                for (QueryDocumentSnapshot userD : userDocs) {
                    progressBar.setVisibility(View.VISIBLE);
                    User user = map_user(userD);
                    users.add(user);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        ArrayList<String> communes_dep=new ArrayList<>();
        ArrayList<String> communes_reg=new ArrayList<>();
        ArrayList<String> communes_pays=new ArrayList<>();
        for (int j=0;j<pres.size();j++){
            Preinscription bvi=pres.get(j);
            if(depIntent!=null & bvi.getDepartement().equals(depIntent) ){
                if (!verifyIn(bvi.getCommune(),communes_listed_dep)){
                    communes_listed_dep.add(bvi.getCommune());
                }
            }
            else if(regIntent!=null & bvi.getRegion().equals(regIntent) ){
                if (!verifyIn(bvi.getCommune(),communes_listed_reg)){
                    communes_listed_reg.add(bvi.getCommune());
                }
            }
            else if(paysIntent!=null & bvi.getPays().equals(paysIntent) ){
                if (!verifyIn(bvi.getCommune(),communes_listed_pays)){
                    communes_listed_pays.add(bvi.getCommune());
                }
            }
        }

        for (int j=0;j<ins.size();j++){
            Inscription bvi=ins.get(j);
            if(depIntent!=null & bvi.getDepartement().equals(depIntent) ){
                if (!verifyIn(bvi.getCommune(),communes_listed_dep_ins)){
                    communes_listed_dep_ins.add(bvi.getCommune());
                }
            }
            else if(regIntent!=null & bvi.getRegion().equals(regIntent) ){
                if (!verifyIn(bvi.getCommune(),communes_listed_reg_ins)){
                    communes_listed_reg_ins.add(bvi.getCommune());
                }
            }
            else if(paysIntent!=null & bvi.getPays().equals(paysIntent) ){
                if (!verifyIn(bvi.getCommune(),communes_listed_pays_ins)){
                    communes_listed_pays_ins.add(bvi.getCommune());
                }
            }
        }

        if(depIntent!=null){
            for (int i=0;i<communes_listed_dep.size();i++){
                if (!verifyIn(communes_listed_dep.get(i),communes_dep)){
                    communes_dep.add(communes_listed_dep.get(i));
                }
            }
            for (int i=0;i<communes_listed_dep_ins.size();i++){
                if (!verifyIn(communes_listed_dep_ins.get(i),communes_dep)){
                    communes_dep.add(communes_listed_dep.get(i));
                }
            }
            for (int i=0;i<pres.size();i++){
                if (verifyIn(pres.get(i).getCommune(),communes_dep)){
                    Commune commune=new Commune();
                    for (int d=0;d<users.size();d++){
                        if (users.get(i).getType().equals("DEC") & users.get(i).getCommune().equals(pres.get(i).getCommune()))
                            commune.setCom_dec(users.get(i).getNom()+" "+
                                    users.get(i).getPrenom());
                    }
                    commune.setCom_name(communes_dep.get(i));
                    ActivList.add(commune);
                }
                else{
                    Commune commune=new Commune();
                    for (int d=0;d<users.size();d++){
                        if (users.get(i).getType().equals("DEC") & users.get(i).getCommune().equals(pres.get(i).getCommune()))
                            commune.setCom_dec(users.get(i).getNom()+" "+
                                    users.get(i).getPrenom());
                    }
                    commune.setCom_name(communes_dep.get(i));
                    InactivList.add(commune);
                }
            }
            viewHolderInAct.notifyDataSetChanged();
            viewHolderAct.notifyDataSetChanged();
        }
        if(regIntent!=null){
            for (int i=0;i<communes_listed_reg.size();i++){
                if (!verifyIn(communes_listed_reg.get(i),communes_reg)){
                    communes_reg.add(communes_listed_reg.get(i));
                }
            }
            for (int i=0;i<communes_listed_reg_ins.size();i++){
                if (!verifyIn(communes_listed_reg_ins.get(i),communes_reg)){
                    communes_reg.add(communes_listed_reg_ins.get(i));
                }
            }
            for (int i=0;i<pres.size();i++){
                if (verifyIn(pres.get(i).getCommune(),communes_reg)){
                    Commune commune=new Commune();
                    for (int d=0;d<users.size();d++){
                        if (users.get(i).getType().equals("DEC") & users.get(i).getCommune().equals(pres.get(i).getCommune()))
                            commune.setCom_dec(users.get(i).getNom()+" "+
                                    users.get(i).getPrenom());
                    }
                    commune.setCom_name(communes_reg.get(i));
                    ActivList.add(commune);
                }
                else{
                    Commune commune=new Commune();
                    for (int d=0;d<users.size();d++){
                        if (users.get(i).getType().equals("DEC") & users.get(i).getCommune().equals(pres.get(i).getCommune()))
                            commune.setCom_dec(users.get(i).getNom()+" "+
                                    users.get(i).getPrenom());
                    }
                    commune.setCom_name(communes_reg.get(i));
                    InactivList.add(commune);
                }
            }

            viewHolderInAct.notifyDataSetChanged();
            viewHolderAct.notifyDataSetChanged();
        }
        if(paysIntent!=null){
            for (int i=0;i<communes_listed_pays.size();i++){
                if (!verifyIn(communes_listed_pays.get(i),communes_pays)){
                    communes_pays.add(communes_listed_pays.get(i));
                }
            }
            for (int i=0;i<communes_listed_pays_ins.size();i++){
                if (!verifyIn(communes_listed_pays_ins.get(i),communes_pays)){
                    communes_pays.add(communes_listed_pays_ins.get(i));
                }
            }
            for (int i=0;i<pres.size();i++){
                if (verifyIn(pres.get(i).getCommune(),communes_pays)){
                    Commune commune=new Commune();
                    for (int d=0;d<users.size();d++){
                        if (users.get(i).getType().equals("DEC") & users.get(i).getCommune().equals(pres.get(i).getCommune()))
                            commune.setCom_dec(users.get(i).getNom()+" "+
                                    users.get(i).getPrenom());
                    }
                    commune.setCom_name(communes_pays.get(i));
                    ActivList.add(commune);
                }
                else{
                    Commune commune=new Commune();
                    for (int d=0;d<users.size();d++){
                        if (users.get(i).getType().equals("DEC") & users.get(i).getCommune().equals(pres.get(i).getCommune()))
                            commune.setCom_dec(users.get(i).getNom()+" "+
                                    users.get(i).getPrenom());
                    }
                    commune.setCom_name(communes_pays.get(i));
                    InactivList.add(commune);
                }
            }

            viewHolderInAct.notifyDataSetChanged();
            viewHolderAct.notifyDataSetChanged();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public boolean verifyIn(String test, ArrayList<String> list){
        for (int i=0; i<list.size();i++){
            if (test.equals(list.get(i)))
                return true;
        }
        return false;
    }
    public Bv map_bv(QueryDocumentSnapshot doc){
        Bv bv1=new Bv();
        bv1.bv_attente = Math.toIntExact(doc.getLong("bv_attente"));
        bv1.bv_recolte = Math.toIntExact(doc.getLong("bv_recolte"));
        bv1.bv_commune = doc.getString("bv_commune");
        bv1.bv_dep = doc.getString("bv_dep");
        bv1.bv_name = doc.getString("bv_name");
        bv1.bv_pays = doc.getString("bv_pays");
        bv1.bv_region = doc.getString("bv_region");
        bv1.bv_vol1_name = doc.getString("bv_vol1_name");
        bv1.bv_vol2_name = doc.getString("bv_vol2_name");
        bv1.bv_vol3_name = doc.getString("bv_vol3_name");
        bv1.bv_vol4_name = doc.getString("bv_vol4_name");
        bv1.bv_vol1_tel = doc.getString("bv_vol1_tel");
        bv1.bv_vol2_tel = doc.getString("bv_vol2_tel");
        bv1.bv_vol3_tel = doc.getString("bv_vol3_tel");
        bv1.bv_vol4_tel = doc.getString("bv_vol4_tel");
        return bv1;
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
    public Discussion map_disc(QueryDocumentSnapshot discD){
        Discussion dis=new Discussion();
        dis.id=discD.getString("id");
        dis.title=discD.getString("title");
        dis.type=discD.getString("type");
        dis.initiateur=discD.getString("initiateur");
        dis.interlocuteur=discD.getString("interlocuteur");
        dis.last_date=discD.getLong("last_date");
        dis.last_message=discD.getString("last_message");
        dis.last_writer=discD.getString("last_writer");

        return dis;
    }
    public Message map_mes(QueryDocumentSnapshot mesD){
        Message mes=new Message();
        mes.emetteur=mesD.getString("emetteur");
        mes.recepteur=mesD.getString("recepteur");
        mes.contenu=mesD.getString("contenu");
        mes.disc_id=mesD.getString("disc_id");
        mes.etat=mesD.getString("etat");
        mes.id=mesD.getId();
        mes.date_envoi=mesD.getLong("date_envoi");
        return mes;
    }
}
