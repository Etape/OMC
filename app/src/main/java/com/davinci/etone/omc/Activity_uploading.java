package com.davinci.etone.omc;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Activity_uploading extends AppCompatActivity {
    ImageView back,accept;
    User user;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    TextView registration_type,registration_file,loading_state,loading_succeed,loading_failed,file_error;
    ProgressBar loading_progressBar,progressBar;
    Button valider,change_file;
    RecyclerView recyclerviewLoading;
    LinearLayout page,loading;
    FirebaseUser Ui=auth.getCurrentUser();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ArrayList<Registration_failed> faileds=new ArrayList<>();
    ArrayList<Bv> bvs=new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Preinscription> preinscriptions=new ArrayList<>();
    ArrayList<Inscription> inscriptions=new ArrayList<>();
    ViewHolderRegistration viewHolderRegistration;
    String filepath,registrationType;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);
        back=findViewById(R.id.back);
        accept=findViewById(R.id.accept);
        file_error=findViewById(R.id.file_error);
        registration_type=findViewById(R.id.registration_type);
        registration_file=findViewById(R.id.registration_file);
        loading_state=findViewById(R.id.loading_state);
        loading_succeed=findViewById(R.id.loading_succeed);
        loading_failed=findViewById(R.id.loading_failed);
        loading_progressBar=findViewById(R.id.loading_progressBar);
        progressBar=findViewById(R.id.progressBar);
        valider=findViewById(R.id.valider);
        change_file=findViewById(R.id.change_file);
        recyclerviewLoading=findViewById(R.id.recyclerviewLoading);

        page=findViewById(R.id.page);
        loading=findViewById(R.id.loading);

        progressBar.setVisibility(View.VISIBLE);
        page.setVisibility(View.GONE);
        final CollectionReference refUs = db.collection("User");
        final CollectionReference refBv = db.collection("Bv");
        final CollectionReference refPre = db.collection("Preinscription");
        final CollectionReference refIns = db.collection("Inscription");
        refBv.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot bvDocs, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.i("Firestore", "Listen failed.", e);
                    return;
                }
                bvs.clear();
                for(QueryDocumentSnapshot doc : bvDocs){
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
                    bvs.add(bv1);

                    Log.i("Firebase ","Firebase Bvs loaded");
                    Toast.makeText(Activity_uploading.this, "Firebase Bvs loaded", Toast
                            .LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    page.setVisibility(View.VISIBLE);
                }
            }
        });

        refIns.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot insDocs, @Nullable FirebaseFirestoreException e) {
                inscriptions.clear();
                Log.i("Firebase ","Firebase inscriptions loading");
                for (QueryDocumentSnapshot insD : insDocs) {
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

                    inscriptions.add(ins);
                }
                Log.i("Firebase ","Firebase inscriptions loaded");
                Toast.makeText(Activity_uploading.this, "Firebase inscriptions loaded", Toast
                        .LENGTH_SHORT).show();
            }
        });

        refPre.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot preDocs, @Nullable FirebaseFirestoreException e) {
                preinscriptions.clear();
                Log.i("Firebase ","Firebase preinscriptions loading");
                for (QueryDocumentSnapshot preD : preDocs) {
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
                    preinscriptions.add(pre);
                }
                Log.i("Firebase ","Firebase preinscriptions loaded");
                Toast.makeText(Activity_uploading.this, "Firebase preinscriptions loaded", Toast
                        .LENGTH_SHORT).show();
            }
        });


        filepath=getIntent().getStringExtra("filepath");
        registrationType=getIntent().getStringExtra("type");

        recyclerviewLoading.setLayoutManager(new LinearLayoutManager(this));
        viewHolderRegistration =new ViewHolderRegistration(this,faileds);
        recyclerviewLoading.setAdapter(viewHolderRegistration);
        registration_file.setText(filepath.split("/")[-1]);

        loading.setVisibility(View.GONE);

        if(getFormat(filepath).equals("bv")){
            file_error.setVisibility(View.GONE);
            valider.setVisibility(View.VISIBLE);
            registration_type.setText("Enregistrement des Bureaux de vote");
        }
        else if(getFormat(filepath).equals("preinscriptions")){
            file_error.setVisibility(View.GONE);
            valider.setVisibility(View.VISIBLE);
            registration_type.setText("Enregistrement des Preinscriptions");
        }
        else if(getFormat(filepath).equals("inscriptions")){
            file_error.setVisibility(View.GONE);
            valider.setVisibility(View.VISIBLE);
            registration_type.setText("Enregistrement des Inscriptions");
        }
        else {
            file_error.setVisibility(View.VISIBLE);
            valider.setVisibility(View.GONE);
            registration_type.setText("Enregistrement par fichier");
        }
        valider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                valider.setClickable(false);
                change_file.setClickable(false);
                if(getFormat(filepath).equals("bv")){
                    InputStreamReader iS = null;
                    try {
                        iS = new InputStreamReader(Files.newInputStream(Paths.get(filepath)));
                        BufferedReader reader = new BufferedReader(iS);
                        reader.readLine();
                        String line;
                        int count=0;
                        while ((line = reader.readLine()) != null) {
                            String[] cells = line.split(";");
                            if(count!=0){
                               Bv bv=new Bv();
                               bv.setBv_pays(cells[0]);
                                bv.setBv_pays(cells[1]);
                                bv.setBv_pays(cells[2]);
                                bv.setBv_pays(cells[3]);
                                bv.setBv_pays(cells[4]);
                                bv.setBv_pays(cells[5]);
                                bv.setBv_pays(cells[6]);
                                bv.setBv_pays(cells[7]);
                            }
                            count++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Activity_uploading.this, "file not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(getFormat(filepath).equals("preinscriptions")){

                }
                else if(getFormat(filepath).equals("inscriptions")){

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getFormat(String filepath){
        if(filepath.split(".")[1].equals("csv")){
            InputStreamReader is = null;
            try {
                is = new InputStreamReader(Files.newInputStream(Paths.get(filepath)));
                BufferedReader reader = new BufferedReader(is);
                reader.readLine();
                String line;
                int i = 0;
                if ((line = reader.readLine()) != null) {
                    String[] cells = line.split(";");
                    if (cells[6].contains("CE")){
                        return "inscription";
                    }
                    if (cells[5].contains("Volontaire")){
                        return "bv";
                    }
                    if (cells[6].contains("cni")){
                        return "preinscription";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            }
        }
        else{
        return "None";
        }
        return "None";
    }
    public boolean verifyInPre(Preinscription test, ArrayList<Preinscription> list){
        for (int i=0; i<list.size();i++){
            if (test.getCni().equals(list.get(i).getCni()))
                return true;
        }
        return false;
    }
    public boolean verifyInIns(Inscription test, ArrayList<Inscription> list){
        for (int i=0; i<list.size();i++){
            if (test.getCni().equals(list.get(i).getCni()))
                return true;
        }
        return false;
    }
    public boolean verifyInBv(Bv test, ArrayList<Bv> list){
        for (int i=0; i<list.size();i++){
            if (test.getBv_name().equals(list.get(i).getBv_name()))
                return true;
        }
        return false;
    }
}
