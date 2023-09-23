package com.davinci.etone.omc;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Activity_bv extends AppCompatActivity {

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ImageView back,edit;
    AutoCompleteTextView region,departement,commune,pays,departement_org,indicatif;
    AutoCompleteTextView volontaire1,volontaire2,volontaire3,volontaire4;
    EditText bv_name;
    Button validate;
    LinearLayout container,container_commune;
    TextView error;
    ProgressBar progressBar,progressBar2;
    ArrayList<String> communes=new ArrayList<>();
    ArrayList<String> regions=new ArrayList<>();
    ArrayList<String> departements=new ArrayList<>();
    ArrayList<String[]> rdc=new ArrayList<>();
    ArrayList<String[]> militants=new ArrayList<>();
    ArrayList<String> payslist=new ArrayList<>();
    ArrayList<User> bureau=new ArrayList<>();
    String bvIntent;
    String comIntent;
    String depIntent;
    String regIntent;
    String paysIntent;
    TextView commune_name,commune_prd,nb_bv,nb_ins,nb_pre,nb_mil,activite_30j,activite_24h,commune_actives,commune_inactives,title;
    RecyclerView recyclerViewCom,recyclerViewDep,recyclerViewReg,recyclerViewPays;
    ViewHolderMilitants viewHolderMil;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference refPre = db.collection("Preinscription");
    CollectionReference refIns = db.collection("Inscription");
    CollectionReference refUser = db.collection("User");
    CollectionReference refDisc = db.collection("Discussion");
    CollectionReference refMes = db.collection("Message");
    CollectionReference refBv = db.collection("Bv");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bv);

        viewHolderMil =new ViewHolderMilitants(this,bureau);

        bvIntent=getIntent().getStringExtra("Bv_name+com");
        comIntent=getIntent().getStringExtra("com_name");
        depIntent=getIntent().getStringExtra("dep_name");
        regIntent=getIntent().getStringExtra("reg_name");
        paysIntent=getIntent().getStringExtra("pays_name");

        container_commune=findViewById(R.id.container_commune);
        recyclerViewCom=findViewById(R.id.recyclerviewCom);
        recyclerViewDep=findViewById(R.id.recyclerviewDep);
        recyclerViewReg=findViewById(R.id.recyclerviewReg);
        recyclerViewPays=findViewById(R.id.recyclerviewPays);
        title=findViewById(R.id.title);
        commune_name=findViewById(R.id.commune_name);
        commune_prd=findViewById(R.id.commune_prd);
        nb_bv=findViewById(R.id.nb_bv);
        nb_ins=findViewById(R.id.nb_ins);
        nb_pre=findViewById(R.id.nb_pre);
        nb_mil=findViewById(R.id.nb_mil);
        activite_30j=findViewById(R.id.activite_30j);
        activite_24h=findViewById(R.id.activite_24h);
        commune_actives=findViewById(R.id.commune_actives);
        commune_inactives=findViewById(R.id.commune_inactives);

        recyclerViewCom.setAdapter(viewHolderMil);
        recyclerViewDep.setAdapter(viewHolderMil);
        recyclerViewReg.setAdapter(viewHolderMil);
        recyclerViewPays.setAdapter(viewHolderMil);

        bv_name=findViewById(R.id.nom);
        back=findViewById(R.id.back);
        region=findViewById(R.id.region);
        departement=findViewById(R.id.departement);
        commune=findViewById(R.id.commune);
        pays=findViewById(R.id.pays);
        volontaire1=findViewById(R.id.volontaire1);
        volontaire2=findViewById(R.id.volontaire2);
        volontaire3=findViewById(R.id.volontaire3);
        volontaire4=findViewById(R.id.volontaire4);
        progressBar=findViewById(R.id.progressBar);
        edit=findViewById(R.id.edit);
        container=findViewById(R.id.bv_container);
        error=findViewById(R.id.error);
        validate=findViewById(R.id.validate);

        LoadCommunes();

        ArrayAdapter<String> adapter_pays = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, payslist);
        pays.setAdapter(adapter_pays);
        ArrayAdapter<String> adapter_commune = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, communes);
        commune.setAdapter(adapter_commune);
        ArrayAdapter<String> adapter_region = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, regions);
        region.setAdapter(adapter_region);
        ArrayAdapter<String> adapter_dep = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, departements);
        departement.setAdapter(adapter_dep);
        error.setVisibility(View.GONE);
        validate.setVisibility(View.GONE);
        if(bvIntent!=null){
            container_commune.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            String[] values=bvIntent.split(":");
            String bv=values[0];
            String com=values[1];
            String dep=values[2];

            refBv.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot bvDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    for (QueryDocumentSnapshot postSnapshot : bvDocs) {
                        Bv bvi=map_bv(postSnapshot);
                        if(bvi.getBv_dep().equals(dep) & bvi.getBv_commune().equals(com) & bvi.getBv_name().equals(bv)){
                            bv_name.setText(bv);
                            region.setText(bvi.getBv_region());
                            departement.setText(dep);
                            commune.setText(com);
                            pays.setText(bvi.getBv_pays());
                            volontaire1.setText(bvi.getBv_vol1_name());
                            volontaire2.setText(bvi.getBv_vol2_name());
                            volontaire3.setText(bvi.getBv_vol3_name());
                            volontaire4.setText(bvi.getBv_vol4_name());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

            });
        }

        else if (comIntent!=null){
            container.setVisibility(View.GONE);
            container_commune.setVisibility(View.VISIBLE);
            commune_name.setText(comIntent);
            commune_actives.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            commune_inactives.setVisibility(View.GONE);
            for (int i=0;i<rdc.size();i++){
                if(rdc.get(i)[2].equals(comIntent)){
                    commune_prd.setText("Cameroun, "+rdc.get(i)[0]+", "+rdc.get(i)[1]);
                }
            }

            refBv.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot bvDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : bvDocs) {
                        Bv bvi=map_bv(postSnapshot);
                        progressBar.setVisibility(View.VISIBLE);
                        if(bvi.getBv_commune().equals(comIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });
            refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : userDocs) {
                        User bvi=map_user(postSnapshot);
                        bureau.clear();
                        if(bvi.getCommune().equals(comIntent) & !bvi.getType().equals("Citoyen")) {
                            count+=1;
                            if(!bvi.getType().equals("Militant") & !bvi.getType().equals("Volontaire")){
                                bureau.add(bvi);
                            }
                        }
                    }
                    nb_mil.setText("nombre de Militants : "+count );
                    progressBar.setVisibility(View.GONE);
                    viewHolderMil.notifyDataSetChanged();
                }

            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot preDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : preDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=map_pre(postSnapshot);
                        if(bvi.getCommune().equals(comIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24h[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30j[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre de Preinscriptions : "+count );
                    activite_24h.setText("Activite des 24h  : "+ act24h[0]);
                    activite_30j.setText("Activite des 30 Jours : "+ act30j[0]);
                    progressBar.setVisibility(View.GONE);
                }

            });
            refIns.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : insDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=map_ins(postSnapshot);
                        if(bvi.getCommune().equals(comIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24ih[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30ij[0] +=1;
                            }
                        }
                    }
                    nb_ins.setText("nombre d' Inscriptions : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });

            activite_24h.setText("Activite des 24h  : "+ act24h[0]+act24ih[0]);
            activite_30j.setText("Activite des 30 Jours : "+ act30j[0]+ act30ij[0]);

        }
        else if (depIntent!=null){
            commune_actives.setClickable(true);
            commune_inactives.setClickable(true);
            edit.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            container_commune.setVisibility(View.VISIBLE);
            commune_name.setText(depIntent);
            for (int i=0;i<rdc.size();i++){
                if(rdc.get(i)[1].equals(depIntent)){
                    commune_prd.setText("Cameroun, "+rdc.get(i)[0]);
                }
            }

            refBv.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot bvDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : bvDocs) {
                        Bv bvi=map_bv(postSnapshot);
                        if(bvi.getBv_dep().equals(depIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });
            refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : userDocs) {
                        User bvi=map_user(postSnapshot);
                        bureau.clear();
                        if(bvi.getDepartement().equals(comIntent) & !bvi.getType().equals("Citoyen")) {
                            count+=1;
                            if(!bvi.getType().equals("Militant") & !bvi.getType().equals("Volontaire")){
                                bureau.add(bvi);
                            }
                        }
                    }
                    nb_mil.setText("nombre de Militants : "+count );
                    progressBar.setVisibility(View.GONE);
                    viewHolderMil.notifyDataSetChanged();
                }

            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot preDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : preDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=map_pre(postSnapshot);
                        if(bvi.getCommune().equals(comIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24h[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30j[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre de Preinscription : "+count );
                    activite_24h.setText("Activite des 24h  : "+ act24h[0]);
                    activite_30j.setText("Activite des 30 Jours : "+ act30j[0]);
                    progressBar.setVisibility(View.GONE);
                }

            });
            refIns.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : insDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=map_ins(postSnapshot);
                        if(bvi.getDepartement().equals(comIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24ih[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30ij[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre d' Inscription : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });

            activite_24h.setText("Activite des 24h  : "+ act24h[0]+act24ih[0]);
            activite_30j.setText("Activite des 30 Jours : "+ act30j[0]+ act30ij[0]);

            activite_24h.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Activity_bv.this,Activity_competitive.class);
                    intent.putExtra("dep",depIntent);
                    startActivity(intent);
                }
            });
            activite_30j.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Activity_bv.this,Activity_competitive.class);
                    intent.putExtra("dep",depIntent);
                    startActivity(intent);
                }
            });

        }
        else if (regIntent!=null){
            container.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            container_commune.setVisibility(View.VISIBLE);
            commune_name.setText(regIntent);

            commune_actives.setClickable(true);
            commune_inactives.setClickable(true);
            commune_prd.setText("Cameroun ");

            refBv.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot bvDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : bvDocs) {
                        Bv bvi=map_bv(postSnapshot);
                        if(bvi.getBv_region().equals(regIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });
            refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : userDocs) {
                        User bvi=map_user(postSnapshot);
                        bureau.clear();
                        if(bvi.getRegion().equals(regIntent) & !bvi.getType().equals("Citoyen")) {
                            count+=1;
                            if(!bvi.getType().equals("Militant") & !bvi.getType().equals("Volontaire")){
                                bureau.add(bvi);
                            }
                        }
                    }
                    nb_mil.setText("nombre de Militants : "+count );
                    progressBar.setVisibility(View.GONE);
                    viewHolderMil.notifyDataSetChanged();
                }

            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot preDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : preDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=map_pre(postSnapshot);
                        if(bvi.getRegion().equals(regIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24h[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30j[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre de Preinscription : "+count );
                    activite_24h.setText("Activite des 24h  : "+ act24h[0]);
                    activite_30j.setText("Activite des 30 Jours : "+ act30j[0]);
                    progressBar.setVisibility(View.GONE);
                }

            });
            refIns.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : insDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=map_ins(postSnapshot);
                        if(bvi.getRegion().equals(comIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24ih[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30ij[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre d' Inscription : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });

            activite_24h.setText("Activite des 24h  : "+ act24h[0]+act24ih[0]);
            activite_30j.setText("Activite des 30 Jours : "+ act30j[0]+ act30ij[0]);

            activite_24h.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Activity_bv.this,Activity_competitive.class);
                    intent.putExtra("reg",regIntent);
                    startActivity(intent);
                }
            });
            activite_30j.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Activity_bv.this,Activity_competitive.class);
                    intent.putExtra("reg",regIntent);
                    startActivity(intent);
                }
            });


        }
        else if (paysIntent!=null){
            commune_actives.setClickable(true);
            commune_inactives.setClickable(true);
            edit.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            container_commune.setVisibility(View.VISIBLE);
            commune_name.setText(paysIntent);

            refBv.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot bvDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : bvDocs) {
                        Bv bvi=map_bv(postSnapshot);
                        if(bvi.getBv_pays().equals(paysIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });
            refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    int count=0;
                    for (QueryDocumentSnapshot postSnapshot : userDocs) {
                        User bvi=map_user(postSnapshot);
                        bureau.clear();
                        if(bvi.getPays().equals(paysIntent) & !bvi.getType().equals("Citoyen")) {
                            count+=1;
                            if(!bvi.getType().equals("Militant") & !bvi.getType().equals("Volontaire")){
                                bureau.add(bvi);
                            }
                        }
                    }
                    nb_mil.setText("nombre de Militants : "+count );
                    progressBar.setVisibility(View.GONE);
                    viewHolderMil.notifyDataSetChanged();
                }

            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot preDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : preDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=map_pre(postSnapshot);
                        if(bvi.getPays().equals(paysIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24h[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30j[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre de Preinscription : "+count );
                    activite_24h.setText("Activite des 24h  : "+ act24h[0]);
                    activite_30j.setText("Activite des 30 Jours : "+ act30j[0]);
                    progressBar.setVisibility(View.GONE);
                }

            });
            refIns.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (QueryDocumentSnapshot postSnapshot : insDocs) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=map_ins(postSnapshot);
                        if(bvi.getPays().equals(paysIntent) ){
                            count+=1;
                            if (actual-bvi.getCreation_date()<3600*24){
                                act24ih[0] +=1;
                            }
                            if (actual-bvi.getCreation_date()<3600*24*30){
                                act30ij[0] +=1;
                            }
                        }
                    }
                    nb_pre.setText("nombre d' Inscription : "+count );
                    progressBar.setVisibility(View.GONE);
                }

            });

            activite_24h.setText("Activite des 24h  : "+ act24h[0]+act24ih[0]);
            activite_30j.setText("Activite des 30 Jours : "+ act30j[0]+ act30ij[0]);

            activite_24h.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Activity_bv.this,Activity_competitive.class);
                    intent.putExtra("pays",paysIntent);
                    startActivity(intent);
                }
            });
            activite_30j.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Activity_bv.this,Activity_competitive.class);
                    intent.putExtra("pays",paysIntent);
                    startActivity(intent);
                }
            });

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private ArrayList<String> LoadCountries() {
        InputStreamReader is = null;
        ArrayList<String> countries=new ArrayList<>();
        try {
            is = new InputStreamReader(getAssets()
                    .open("sql_pays.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                //Toast.makeText(this, "file found", Toast.LENGTH_SHORT).show();
                String[] cells = line.split(";");
                countries.add(cells[3]);
            }
            Toast.makeText(this, "upload terminee", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
        }
        return countries;
    }
    private void LoadCommunes() {
        InputStreamReader is = null;
        DatabaseReference refBv=Db.getReference().child("Bv");
        ArrayList<String> registeredBv=new ArrayList<>();
        Toast.makeText(this, "loading communes", Toast.LENGTH_SHORT).show();
        try {
            is = new InputStreamReader(getAssets()
                    .open("rdc.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            int i=0;
            regions.add("LITTORAL");
            regions.add("CENTRE");
            regions.add("ADAMAOUA");
            regions.add("NORD");
            regions.add("EXTREME-NORD");
            regions.add("OUEST");
            regions.add("EST");
            regions.add("SUD-OUEST");
            regions.add("NORD-OUEST");
            regions.add("SUD");
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(";");
                rdc.add(cells);
                if(!verifyIn(cells[2],communes))
                    communes.add(cells[2]);
                if(!verifyIn(cells[1],departements))
                    departements.add(cells[1]);
            }
            Toast.makeText(this, "loading complete", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, Service_AddBv.class);
            //startService(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
        }
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
