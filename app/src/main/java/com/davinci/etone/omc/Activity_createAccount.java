package com.davinci.etone.omc;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_createAccount extends AppCompatActivity {
    FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ImageView back,upload_file;
    EditText nom,prenom,email,telephone,cni,date,parrain,matricule,password,conf_password,code;
    AutoCompleteTextView militant,parti,region,departement,commune,pays,departement_org,indicatif;
    RadioButton sexe_homme,sexe_femme,symp_oui,symp_non;
    Button but_create;
    TextView ok_but;
    LinearLayout matricule_layout,dialogBox;
    TextView error;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        code=findViewById(R.id.code);
        dialogBox=findViewById(R.id.dialogBox);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        militant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().toUpperCase().trim().equals("OUI"))
                    matricule_layout.setVisibility(View.VISIBLE);
                else
                    matricule_layout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                else if(email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher
                        (email.getText().toString()).matches()){
                    error.setText("Veuillez renseigner un email correct !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(password.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner un mot de passe !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(conf_password.getText().toString().isEmpty() || !conf_password.getText()
                        .toString().equals(password.getText().toString())){
                    error.setText("Veuillez confirmer votre mot de passe !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(date.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner une date correcte !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(cni.getText().toString().isEmpty() || cni.getText().toString().length()
                        !=13){
                    error.setText("Veuillez renseigner une cni correcte !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(militant.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner le champ militant!");
                    error.setVisibility(View.VISIBLE);
                }
                else if(militant.getText().toString().trim().toUpperCase().equals("OUI") && parti.getText()
                        .toString
                                ().isEmpty()){
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
                    User inscription = new User();
                    inscription.setNom(nom.getText().toString());
                    inscription.setPrenom(prenom.getText().toString());
                    inscription.setEmail(email.getText().toString().trim());
                    inscription.setPassword(password.getText().toString());
                    inscription.setCni(cni.getText().toString());
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
                    if (!matricule.getText().toString().isEmpty())
                        inscription.setParti(matricule.getText().toString());

                    inscription.setPays(pays.getText().toString());
                    inscription.setRegion(region.getText().toString());
                    inscription.setDepartement(departement.getText().toString());
                    inscription.setDepartement_org(departement_org.getText().toString());
                    inscription.setCommune(commune.getText().toString());
                    but_create.setClickable(false);
                    DatabaseReference refPre = Db.getReference().child("Preinscription");
                    DatabaseReference refUser = Db.getReference().child("User");
                    if (!parrain.getText().toString().isEmpty()) {
                        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean test1 = false;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user.getId().toUpperCase().equals(parrain.getText().toString().trim()
                                            .toUpperCase())) {
                                        test1 = true;
                                        break;
                                    }
                                }
                                if (test1) {
                                    inscription.setParrain(parrain.getText().toString().trim());
                                    refPre.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            boolean test = true;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                User pre = snapshot.getValue(User.class);
                                                if (pre.getEmail().equals(inscription.getEmail())) {
                                                    test = false;
                                                    break;
                                                }
                                            }
                                            if (test) {
                                                String key = refPre.push().getKey();
                                                inscription.setId(key);
                                                Calendar calendar=Calendar.getInstance();
                                                inscription.setCreation_date(calendar
                                                        .getTimeInMillis()/1000);
                                                auth.createUserWithEmailAndPassword(inscription
                                                        .getEmail(),inscription.getPassword())
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()){
                                                                    FirebaseUser ui=auth.getCurrentUser();
                                                                    ui.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void
                                                                        onComplete(@NonNull Task<Void> task1) {
                                                                            if (task1.isSuccessful()){
                                                                                dialogBox.setVisibility(View.VISIBLE);
                                                                                code.setVisibility(View.GONE);
                                                                                ok_but.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View view) {
                                                                                        if (ui.isEmailVerified()){
                                                                                            refPre.child(key).setValue(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    Toast.makeText(Activity_createAccount.this,
                                                                                                            "Compte ouvert avec succes", Toast.LENGTH_SHORT).show();
                                                                                                    but_create.setClickable(true);
                                                                                                    dialogBox.setVisibility(View.GONE);
                                                                                                    progressBar.setVisibility(View.GONE);
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });

                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(Activity_createAccount.this,
                                                        "compte deja presente ", Toast
                                                                .LENGTH_SHORT)
                                                        .show();
                                                but_create.setClickable(true);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        refPre.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean test = true;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User pre = snapshot.getValue(User.class);
                                    if (pre.getEmail().equals(inscription.getEmail())) {
                                        test = false;
                                        break;
                                    }
                                }
                                if (test) {
                                    String key = refPre.push().getKey();
                                    inscription.setId(key);
                                    Calendar calendar=Calendar.getInstance();
                                    inscription.setCreation_date(calendar
                                            .getTimeInMillis()/1000);
                                    auth.createUserWithEmailAndPassword(inscription
                                            .getEmail(),inscription.getPassword())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
                                                        FirebaseUser ui=auth.getCurrentUser();
                                                        ui.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void
                                                            onComplete(@NonNull Task<Void> task1) {
                                                                if (task1.isSuccessful()){
                                                                    dialogBox.setVisibility(View.VISIBLE);
                                                                    code.setVisibility(View.GONE);
                                                                    ok_but.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            if (ui.isEmailVerified()){
                                                                                refPre.child(key).setValue(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(Activity_createAccount.this,
                                                                                                "Compte ouvert avec succes", Toast.LENGTH_SHORT).show();
                                                                                        but_create.setClickable(true);
                                                                                        dialogBox.setVisibility(View.GONE);
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                } else {
                                    Toast.makeText(Activity_createAccount.this,
                                            "compte deja presente ", Toast
                                                    .LENGTH_SHORT)
                                            .show();
                                    but_create.setClickable(true);
                                    progressBar.setVisibility(View.GONE);
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
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
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
