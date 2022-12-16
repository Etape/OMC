package com.davinci.etone.omc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

            DatabaseReference ref = Db.getReference().child("Bv");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Bv bvi=postSnapshot.getValue(Bv.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
            DatabaseReference refBv=Db.getReference().child("Bv");
            DatabaseReference refUser=Db.getReference().child("User");
            DatabaseReference refPre=Db.getReference().child("Preinscription");
            DatabaseReference refIns=Db.getReference().child("Inscription");

            refBv.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Bv bvi=postSnapshot.getValue(Bv.class);
                        if(bvi.getBv_commune().equals(comIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        User bvi=postSnapshot.getValue(User.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=postSnapshot.getValue(Preinscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refIns.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=postSnapshot.getValue(Inscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
            DatabaseReference refBv=Db.getReference().child("Bv");
            DatabaseReference refUser=Db.getReference().child("User");
            DatabaseReference refPre=Db.getReference().child("Preinscription");
            DatabaseReference refIns=Db.getReference().child("Inscription");

            refBv.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Bv bvi=postSnapshot.getValue(Bv.class);
                        if(bvi.getBv_dep().equals(depIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        User bvi=postSnapshot.getValue(User.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=postSnapshot.getValue(Preinscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refIns.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=postSnapshot.getValue(Inscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
            DatabaseReference refBv=Db.getReference().child("Bv");
            DatabaseReference refUser=Db.getReference().child("User");
            DatabaseReference refPre=Db.getReference().child("Preinscription");
            DatabaseReference refIns=Db.getReference().child("Inscription");

            refBv.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Bv bvi=postSnapshot.getValue(Bv.class);
                        if(bvi.getBv_region().equals(regIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        User bvi=postSnapshot.getValue(User.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=postSnapshot.getValue(Preinscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refIns.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=postSnapshot.getValue(Inscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
            DatabaseReference refBv=Db.getReference().child("Bv");
            DatabaseReference refUser=Db.getReference().child("User");
            DatabaseReference refPre=Db.getReference().child("Preinscription");
            DatabaseReference refIns=Db.getReference().child("Inscription");

            refBv.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Bv bvi=postSnapshot.getValue(Bv.class);
                        if(bvi.getBv_pays().equals(paysIntent)){
                            count+=1;
                        }
                    }
                    nb_bv.setText("nombre de BV : "+count );
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        User bvi=postSnapshot.getValue(User.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final int[] act24h = {0};
            final int[] act30j = {0};
            final int[] act24ih = {0};
            final int[] act30ij = {0};
            refPre.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30j[0]=0;
                    act24h[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Preinscription bvi=postSnapshot.getValue(Preinscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refIns.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count =0;
                    act30ij[0]=0;
                    act24ih[0]=0;
                    long actual=System.currentTimeMillis()/1000;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Inscription bvi=postSnapshot.getValue(Inscription.class);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
