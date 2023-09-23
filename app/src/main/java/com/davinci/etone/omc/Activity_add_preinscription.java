package com.davinci.etone.omc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_add_preinscription extends AppCompatActivity {

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    ImageView back,upload_file;
    EditText nom,prenom,email,telephone,cni,date,parrain;
    AutoCompleteTextView region,departement,commune,pays,departement_org,indicatif;
    RadioButton sexe_homme,sexe_femme,symp_oui,symp_non;
    Button but_create;
    LinearLayout matricule_layout;
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
        setContentView(R.layout.activity_add_preinscription);

        nom=findViewById(R.id.nom);
        indicatif=findViewById(R.id.indicatif);
        back=findViewById(R.id.back);
        upload_file=findViewById(R.id.upload_file);
        prenom=findViewById(R.id.prenom);
        email=findViewById(R.id.email);
        telephone=findViewById(R.id.telephone);
        cni=findViewById(R.id.cni);
        date=findViewById(R.id.date);
        parrain=findViewById(R.id.parrain);
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
        ArrayAdapter<String> adapter_partis = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, partis);
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
        pays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                progressBar.setVisibility(View.VISIBLE);
                if (charSequence.toString().trim().equals("Cameroun")){
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
                        (email.getText().toString().trim()).matches()){
                    error.setText("Veuillez renseigner un email correct !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(date.getText().toString().isEmpty()){
                    error.setText("Veuillez renseigner une date correcte !");
                    error.setVisibility(View.VISIBLE);
                }
                else if(cni.getText().toString().isEmpty() || cni.getText().toString().length()
                        !=8){
                    error.setText("Veuillez renseigner une cni correcte !");
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
                    Preinscription inscription = new Preinscription();
                    inscription.setNom(nom.getText().toString());
                    inscription.setPrenom(prenom.getText().toString());
                    inscription.setEmail(email.getText().toString().trim());
                    inscription.setCni(cni.getText().toString().trim());
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

                    inscription.setPays(pays.getText().toString());
                    inscription.setRegion(region.getText().toString());
                    inscription.setDepartement(departement.getText().toString());
                    inscription.setDepartement_org(departement_org.getText().toString());
                    inscription.setCommune(commune.getText().toString());
                    but_create.setClickable(false);
                    if (!parrain.getText().toString().isEmpty()) {
                        refUser.whereEqualTo("code", parrain.getText().toString().trim()).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                boolean test1 = false;
                                for (QueryDocumentSnapshot userD : userDocs) {
                                    User user = map_user(userD);
                                    test1 = true;

                                }
                                if (test1) {
                                    inscription.setParrain(parrain.getText().toString().trim());
                                    refPre.whereEqualTo("cni",inscription.getCni()).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                            boolean test = true;
                                            for (QueryDocumentSnapshot insD : insDocs) {
                                                Preinscription pre = map_pre(insD);
                                                test = false;
                                            }
                                            if (test) {
                                                String key = refPre.document().getId();
                                                inscription.setId(key);
                                                Calendar calendar=Calendar.getInstance();
                                                inscription.setCreation_date(calendar.getTimeInMillis()/1000);
                                                refPre.document(key).set(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(Activity_add_preinscription.this,
                                                                "Preinscription ajoutee avec succes ", Toast.LENGTH_SHORT)
                                                                .show();
                                                        but_create.setClickable(true);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(Activity_add_preinscription.this,
                                                        "Preinscription deja presente ", Toast.LENGTH_SHORT)
                                                        .show();
                                                but_create.setClickable(true);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }

                                    });

                                } else {
                                    error.setText("ID du Parrain non reconnu");
                                    but_create.setClickable(true);
                                    error.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                        });
                    }
                    else {
                        refPre.whereEqualTo("cni",inscription.getCni()).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                boolean test = true;
                                for (QueryDocumentSnapshot insD : insDocs) {
                                    Preinscription pre = map_pre(insD);
                                    test = false;
                                }
                                if (test) {
                                    String key = refPre.document().getId();
                                    inscription.setId(key);
                                    Calendar calendar=Calendar.getInstance();
                                    inscription.setCreation_date(calendar.getTimeInMillis()/1000);
                                    refPre.document(key).set(inscription).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Activity_add_preinscription.this,
                                                    "Preinscription ajoutee avec succes ", Toast.LENGTH_SHORT)
                                                    .show();
                                            but_create.setClickable(true);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    Toast.makeText(Activity_add_preinscription.this,
                                            "Preinscription deja presente ", Toast.LENGTH_SHORT)
                                            .show();
                                    but_create.setClickable(true);
                                    progressBar.setVisibility(View.GONE);
                                }
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
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(str_date);
            timestamp=date.getTime()/1000L;
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
