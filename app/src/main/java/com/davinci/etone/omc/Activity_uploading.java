package com.davinci.etone.omc;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
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
        DatabaseReference refBv=Db.getReference().child("Bv");
        refBv.keepSynced(true);
        refBv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bvs.clear();
                Log.i("Firebase ","Firebase Bvs loading");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Bv bv=postSnapshot.getValue(Bv.class);
                    bvs.add(bv);
                }
                Log.i("Firebase ","Firebase Bvs loaded");
                Toast.makeText(Activity_uploading.this, "Firebase Bvs loaded", Toast
                        .LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                page.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference refPre=Db.getReference().child("Preinscription");
        DatabaseReference refIns=Db.getReference().child("Inscription");

        refIns.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inscriptions.clear();
                Log.i("Firebase ","Firebase inscriptions loading");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Inscription bv=postSnapshot.getValue(Inscription.class);
                    inscriptions.add(bv);
                }
                Log.i("Firebase ","Firebase inscriptions loaded");
                Toast.makeText(Activity_uploading.this, "Firebase inscriptions loaded", Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preinscriptions.clear();
                Log.i("Firebase ","Firebase preinscriptions loading");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Preinscription bv=postSnapshot.getValue(Preinscription.class);
                    preinscriptions.add(bv);
                }
                Log.i("Firebase ","Firebase preinscriptions loaded");
                Toast.makeText(Activity_uploading.this, "Firebase preinscriptions loaded", Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
