package com.davinci.etone.omc;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitive);

        depIntent=getIntent().getStringExtra("dep");
        regIntent=getIntent().getStringExtra("reg");
        paysIntent=getIntent().getStringExtra("pays");

        recyclerViewactiv=findViewById(R.id.recyclerviewactiv);
        recyclerViewinact=findViewById(R.id.recyclerviewinact);

        viewHolderAct =new ViewHolderCommunes(this,ActivList);
        viewHolderInAct =new ViewHolderCommunes(this,InactivList);

        recyclerViewactiv.setAdapter(viewHolderAct);
        recyclerViewinact.setAdapter(viewHolderInAct);


        DatabaseReference refPre=Db.getReference().child("Preinscription");
        DatabaseReference refUsers=Db.getReference().child("User");
        DatabaseReference refIns=Db.getReference().child("Inscription");

        refPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                communes_listed_dep.clear();
                communes_listed_reg.clear();
                communes_listed_pays.clear();
                ActivList.clear();
                InactivList.clear();
                pres.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Preinscription bvi=postSnapshot.getValue(Preinscription.class);
                    pres.add(bvi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refIns.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                communes_listed_dep_ins.clear();
                communes_listed_reg_ins.clear();
                communes_listed_pays_ins.clear();
                ActivList.clear();
                InactivList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Inscription bvi=postSnapshot.getValue(Inscription.class);
                    ins.add(bvi);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refIns.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    progressBar.setVisibility(View.VISIBLE);
                    User usr=postSnapshot.getValue(User.class);
                    users.add(usr);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    }

    public boolean verifyIn(String test, ArrayList<String> list){
        for (int i=0; i<list.size();i++){
            if (test.equals(list.get(i)))
                return true;
        }
        return false;
    }
}
