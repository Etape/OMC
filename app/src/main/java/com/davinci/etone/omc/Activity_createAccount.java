package com.davinci.etone.omc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Activity_createAccount extends AppCompatActivity {
    FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ImageView back,upload_file;
    EditText nom,prenom,email,telephone,cni,date,parrain,matricule,password,conf_password,code,sous_comite_arr,comite_base;
    AutoCompleteTextView militant,parti,region,departement,commune,pays,departement_org,indicatif;
    RadioButton sexe_homme,sexe_femme,symp_oui,symp_non;
    Button but_create,but_next;
    TextView ok_but,error_auth;
    String receiver="";
    Discussion discussion,discussionAd;
    LinearLayout authentication,more_infos,dialogBox;
    TextView error;
    User inscription = new User();
    ProgressBar progressBar;
    ArrayList<String> communes=new ArrayList<>();
    ArrayList<String> regions=new ArrayList<>();
    ArrayList<String> sexe=new ArrayList<>();
    ArrayList<String> departements=new ArrayList<>();
    ArrayList<String> bv=new ArrayList<>();
    ArrayList<String> Oui_Non=new ArrayList<>();
    ArrayList<String[]> rdc=new ArrayList<>();
    ArrayList<String> payslist=new ArrayList<>();
    ArrayList<String> partis=new ArrayList<>();
    ArrayList<String> indicatifs=new ArrayList<>();
    ArrayList<User> allUsers=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        sous_comite_arr=findViewById(R.id.sous_comite_arr);
        comite_base=findViewById(R.id.comite_base);
        code=findViewById(R.id.code);
        dialogBox=findViewById(R.id.dialogBox);
        authentication=findViewById(R.id.authentication);
        more_infos=findViewById(R.id.more_infos);
        but_next=findViewById(R.id.but_next);
        error_auth=findViewById(R.id.error_auth);

        ok_but=findViewById(R.id.ok_but);
        nom=findViewById(R.id.nom);
        indicatif=findViewById(R.id.indicatif);
        back=findViewById(R.id.back);
        prenom=findViewById(R.id.prenom);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        conf_password=findViewById(R.id.conf_password);
        telephone=findViewById(R.id.telephone);
        cni=findViewById(R.id.cni);
        date=findViewById(R.id.date);
        parrain=findViewById(R.id.parrain);
        militant=findViewById(R.id.militant);
        parti=findViewById(R.id.parti);
        region=findViewById(R.id.region);
        departement=findViewById(R.id.departement);
        commune=findViewById(R.id.commune);
        pays=findViewById(R.id.pays);
        departement_org=findViewById(R.id.departement_org);
        sexe_homme=findViewById(R.id.sexe_homme);
        sexe_femme=findViewById(R.id.sexe_femme);
        symp_oui=findViewById(R.id.symp_oui);
        symp_non=findViewById(R.id.symp_non);
        but_create=findViewById(R.id.but_create);
        error=findViewById(R.id.error);
        progressBar=findViewById(R.id.progressBar);

        payslist=LoadCountries();
        LoadCommunes();

        parti.setVisibility(View.GONE);
        Oui_Non.add("OUI");
        Oui_Non.add("NON");
        indicatifs.add("+237");
        indicatifs.add("+33");
        indicatifs.add("+242");
        indicatifs.add("+224");
        indicatifs.add("+15");

        partis.add("PCRN");
        partis.add("RDPC");
        partis.add("MRC");
        partis.add("UPC");
        partis.add("PADEC");
        partis.add("UNDP");
        partis.add("SDF");

        ArrayAdapter<String> adapter_yes = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Oui_Non);
        militant.setAdapter(adapter_yes);
        ArrayAdapter<String> adapter_partis = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, partis);
        parti.setAdapter(adapter_partis);
        ArrayAdapter<String> adapter_indicatifs = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, indicatifs);
        indicatif.setAdapter(adapter_indicatifs);
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
        departement_org.setAdapter(adapter_dep);

        authentication.setVisibility(View.VISIBLE);
        more_infos.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!auth.getCurrentUser().getEmail().isEmpty()){
                    auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.VISIBLE);
                            auth.signOut();
                            finish();
                        }
                    });
                }
                Log.i("Firebase currentUser ",auth.getCurrentUser().getEmail());
                auth.signOut();
                finish();
            }
        });
        pays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                progressBar.setVisibility(View.VISIBLE);
                if (charSequence.toString().trim().equals("Cameroun")){
                    regions.add("Littoral");
                    regions.add("Centre");
                    regions.add("Adamaoua");
                    regions.add("Nord");
                    regions.add("Extreme-Nord");
                    regions.add("Ouest");
                    regions.add("Est");
                    regions.add("Sud-Ouest");
                    regions.add("Nord-Ouest");
                    regions.add("Sud");
                    Log.i("Datasets","regions =="+regions.size());
                    adapter_region.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    regions.clear();
                    adapter_region.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        region.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                departements.clear();
                progressBar.setVisibility(View.VISIBLE);
                for(int e=0;e<rdc.size();e++){
                    if(rdc.get(e)[0].equals(charSequence.toString().toUpperCase()))
                    {
                        if(!verifyIn(rdc.get(e)[1],departements))
                            departements.add(rdc.get(e)[1]);
                    }
                }
                Log.i("Datasets","departements =="+departements.size());
                adapter_dep.notifyDataSetChanged();
                departement.setAdapter(adapter_dep);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        departement.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                communes.clear();
                progressBar.setVisibility(View.VISIBLE);
                for(int e=0;e<rdc.size();e++){
                    if(rdc.get(e)[1].equals(charSequence.toString()))
                    {
                        if(!verifyIn(rdc.get(e)[2],communes))
                            communes.add(rdc.get(e)[2]);
                    }
                }
                Log.i("Datasets","communes =="+communes.size());
                adapter_commune.notifyDataSetChanged();
                commune.setAdapter(adapter_commune);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        militant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().toUpperCase().equals("OUI")){
                    parti.setVisibility(View.VISIBLE);
                }
                else {
                    parti.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher
                        (email.getText().toString().trim()).matches()){
                    error_auth.setText("Veuillez renseigner un email correct !");
                    Log.i("Firebase",email.getText().toString());
                    error_auth.setVisibility(View.VISIBLE);
                }
                else if(password.getText().toString().isEmpty()){
                    error_auth.setText("Veuillez renseigner un mot de passe !");
                    error_auth.setVisibility(View.VISIBLE);
                }
                else if(conf_password.getText().toString().isEmpty() || !conf_password.getText()
                        .toString().equals(password.getText().toString())){
                    error_auth.setText("Veuillez confirmer votre mot de passe !");
                    error_auth.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    error_auth.setVisibility(View.GONE);
                    inscription.setEmail(email.getText().toString().trim());
                    inscription.setPassword(password.getText().toString());
                    auth.createUserWithEmailAndPassword(inscription.getEmail(),inscription.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.i("Firebase Authentication",auth.getCurrentUser().getEmail());
                            authentication.setVisibility(View.GONE);
                            more_infos.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.i("Firebase Authentication Failure",e.getMessage());
                        }
                    });
                }
            }
        });
        // The test phone number and code should be whitelisted in the console.
        but_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nom.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre nom !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(prenom.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre prenom !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(region.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre region !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(departement.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre departement !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(!sexe_homme.isChecked() && !sexe_femme.isChecked()){
                    error.setText("Veuillez renseigner votre sexe !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(!symp_oui.isChecked() && !symp_non.isChecked()){
                    error.setText("Veuillez renseigner le champ sympathisant !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(departement_org.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre departement d'origine !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(pays.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre pays !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(commune.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner votre commune !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(date.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner une date correcte !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(militant.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner le champ militant!");
                    error.setVisibility(View.VISIBLE);
                }
                else if(militant.getText().toString().trim().toUpperCase().equals("OUI") && parti.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner un parti correct !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(telephone.getText().toString().isEmpty() || (indicatif.getText().toString
                        ().equals("+237") & telephone.getText().toString().length()!=9 )||(
                        !telephone.getText().toString().startsWith("6") )){
                    error.setText("Veuillez renseigner un numero telephone correct !");
                    error.setVisibility(View.VISIBLE);
                }
                else {
                    error.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    inscription.setNom(nom.getText().toString());
                    inscription.setPrenom(prenom.getText().toString());
                    inscription.setTelephone(indicatif.getText().toString() + telephone.getText().toString
                            ());
                    if (getTimestamp(date.getText().toString()) != 0)
                        inscription.setDate_naissance("" + getTimestamp(date.getText().toString()));
                    if (sexe_homme.isChecked())
                        inscription.setSexe("Homme");
                    else
                        inscription.setSexe("Femme");
                    if (symp_oui.isChecked())
                        inscription.setSympatisant("Oui");
                    else
                        inscription.setSympatisant("Non");
                    if (!parti.getText().toString().isEmpty())
                        inscription.setParti(parti.getText().toString());

                    inscription.setPays(pays.getText().toString());
                    inscription.setRegion(region.getText().toString());
                    inscription.setDepartement(departement.getText().toString());
                    inscription.setDepartement_org(departement_org.getText().toString());
                    inscription.setCommune(commune.getText().toString());
                    but_create.setClickable(false);
                    DatabaseReference refPre = Db.getReference().child("Preinscription");
                    DatabaseReference refUser = Db.getReference().child("User");
                    if (!parrain.getText().toString().isEmpty()) {
                        final User[] parrainU = new User[1];
                        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean test1 = false;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user.getCode().equals(parrain.getText().toString().trim())) {
                                        parrainU[0] =user;
                                        test1 = true;
                                        break;
                                    }
                                }
                                if (test1) {
                                    inscription.setParrain(parrainU[0].getId());
                                        String key = refUser.push().getKey();
                                        inscription.setId(key);
                                        Calendar calendar=Calendar.getInstance();
                                        inscription.setCreation_date(calendar.getTimeInMillis()/1000);
                                        refUser.child(key).setValue(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.i("Firebase","user created");
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                but_create.setClickable(true);
                                                error.setText("Oups!!!  Probleme de connexion");
                                                error.setVisibility(View.VISIBLE);
                                            }
                                        });

                                } else {
                                    error.setText("ID du Parrain non reconnu");
                                    but_create.setClickable(true);
                                    error.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean test = true;
                                allUsers.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User pre = snapshot.getValue(User.class);
                                    allUsers.add(pre);
                                    if (pre.getEmail().equals(inscription.getEmail())) {
                                        test = false;
                                    }

                                }
                                if (test) {
                                    String key = refPre.push().getKey();
                                    inscription.setId(key);
                                    Calendar calendar=Calendar.getInstance();
                                    inscription.setCreation_date(calendar
                                            .getTimeInMillis()/1000);
                                    Log.i("Firebase","Creating user authentication");dialogBox.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    if (militant.getText().toString().trim().toUpperCase().equals("OUI") & parti
                                            .getText().toString().equals("PCRN")){
                                        dialogBox.setVisibility(View.VISIBLE);
                                        ok_but.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Log.i("Firebase","User authenticaticated");
                                                boolean accept=true;
                                                if(dialogBox.getVisibility()==View.VISIBLE){
                                                    if (comite_base.getText().toString().isEmpty() ){
                                                        comite_base.setBackgroundResource(R.color.red);
                                                        accept=false;
                                                    }
                                                    else if(sous_comite_arr.getText().toString().isEmpty()){
                                                        sous_comite_arr.setBackgroundResource(R.color.red);
                                                        accept=false;
                                                    }
                                                    else if(code.getText().toString().isEmpty() | code.getText()
                                                            .toString().length()!=8){
                                                        sous_comite_arr.setBackgroundResource(R.color.red);
                                                        accept=false;
                                                    }
                                                    else{
                                                        inscription.setComite_base(comite_base.getText().toString().toUpperCase());
                                                        inscription.setCni(code.getText().toString().toUpperCase
                                                                ());
                                                        inscription.setSous_comite_arr(sous_comite_arr.getText().toString().toUpperCase());
                                                        accept=true;
                                                    }
                                                }
                                                if(accept){
                                                    inscription.setType("Citoyen");
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    refUser.child(key).setValue(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            dialogBox.setVisibility(View.GONE);
                                                            progressBar.setVisibility(View.GONE);

                                                            Log.i("Firebase","sending messages for Militant");
                                                            if (militant.getText().toString().toUpperCase().trim().equals
                                                                    ("OUI") & parti.getText().toString().equals("PCRN")){
                                                                Message message =new Message();
                                                                message.setRecepteur(inscription.getId());
                                                                message.setEmetteur("0");
                                                                message.setContenu("Bienvenu dans l'univers OMC, vos " +
                                                                        "informations sont en cours de verification");
                                                                message.setDate_envoi(System.currentTimeMillis()/1000);
                                                                DatabaseReference refDisc=Db.getReference().child("Discussion");
                                                                DatabaseReference refMes=Db.getReference().child("Message");
                                                                String disc_id=refDisc.push().getKey();
                                                                String key=refMes.push().getKey();
                                                                message.setDisc_id(disc_id);
                                                                message.setId(key);
                                                                refMes.child(key).setValue(message);

                                                                Discussion discussion=new Discussion();
                                                                discussion.setInitiateur("0");
                                                                discussion.setInterlocuteur(inscription.getId());
                                                                discussion.setTitle("Message Systeme");
                                                                discussion.setType("Tchat");
                                                                discussion.setId(disc_id);
                                                                discussion.setLast_writer("0");
                                                                discussion.setLast_message(message.getContenu());
                                                                discussion.setLast_date(message.getDate_envoi());
                                                                refDisc.child(disc_id).setValue(discussion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        boolean found=false;
                                                                        for (int i=0;i<allUsers.size();i++){
                                                                            if (allUsers.get(i).getCommune().equals(inscription.getCommune()) &
                                                                                    allUsers.get(i).getType()
                                                                                            .equals("SG CA")){
                                                                                receiver=allUsers.get(i).getId();
                                                                                found=true;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (!found){
                                                                            receiver="-NDodudaW-J1USWvHkpB";
                                                                        }

                                                                        Message message =new Message();
                                                                        message.setRecepteur(receiver);
                                                                        message.setEmetteur("0");
                                                                        String contenu="Validation de Militant " +
                                                                                "\n Id :"+ inscription.getId();
                                                                        message.setContenu(contenu);
                                                                        message.setDate_envoi(System.currentTimeMillis()/1000);
                                                                        DatabaseReference refDisc=Db.getReference().child("Discussion");
                                                                        DatabaseReference refMes=Db.getReference().child("Message");
                                                                        String key=refMes.push().getKey();
                                                                        message.setId(key);
                                                                        refDisc.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                    Discussion disc = snapshot.getValue
                                                                                            (Discussion.class);
                                                                                    if (disc.getInitiateur().equals
                                                                                            ("0") & disc
                                                                                            .getInterlocuteur().equals
                                                                                                    (receiver) ) {
                                                                                        discussionAd=disc;
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                if(discussionAd==null){
                                                                                    discussionAd=new Discussion();
                                                                                    discussionAd.setInitiateur("0");
                                                                                    discussionAd.setInterlocuteur
                                                                                            (receiver);
                                                                                    discussionAd.setTitle("Message Systeme");
                                                                                    discussionAd.setType("Tchat");
                                                                                    discussionAd.setLast_writer("0");
                                                                                    discussionAd.setLast_message(message.getContenu());
                                                                                    discussionAd.setLast_date(message.getDate_envoi());
                                                                                    String disc_id=refDisc.push().getKey();
                                                                                    message.setDisc_id(disc_id);
                                                                                    discussionAd.setId(disc_id);
                                                                                    refDisc.child(disc_id).setValue
                                                                                            (discussionAd);
                                                                                    finish();
                                                                                }
                                                                                else{
                                                                                    String disc_id=discussionAd.getId();
                                                                                    message.setDisc_id(disc_id);
                                                                                    discussionAd.setLast_message(message.getContenu());
                                                                                    discussionAd.setLast_date(message.getDate_envoi());
                                                                                    refDisc.child(disc_id).setValue(discussionAd);
                                                                                    finish();
                                                                                }

                                                                                refMes.child(key).setValue(message);

                                                                            }
                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        but_create.setClickable(true);
                                                                        error.setText("Oups!!!  Probleme de connexion");
                                                                        error.setVisibility(View.VISIBLE);
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            but_create.setClickable(true);
                                                            error.setText("Oups!!!  Probleme de connexion");
                                                            error.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                                }
                                        }
                                    });
                                }
                                else {
                                    dialogBox.setVisibility(View.GONE);
                                    Log.i("Firebase","creating user authentication");
                                    refUser.child(key).setValue(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.i("Firebase","user created");
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            but_create.setClickable(true);
                                            error.setText("Oups!!!  Probleme de connexion");
                                            error.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

    }
    int count=0;
    @Override
    public void onBackPressed(){
        if(auth.getCurrentUser()!=null){
            auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signOut();
                    finish();
                }
            });
        }
        Log.i("Firebase currentUser ",auth.getCurrentUser().getEmail());
        auth.signOut();
        finish();
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
    private long getTimestamp(String str_date){
        long timestamp = 0;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
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
                bv.add(cells[3]);
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
