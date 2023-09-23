package com.davinci.etone.omc;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Activity_personnel extends AppCompatActivity {

    User user=new User();
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    FirebaseDatabase Db=FirebaseDatabase.getInstance();

    ImageView edit,message,back;
    TextView id,round_letter,poste,commune,email,telephone,age,sexe,big_initials,complete_name,
            nom_titre,efforts_mois,efforts_totaux;
    AutoCompleteTextView edit_commune,edit_poste;
    ProgressBar progressBar;
    String user_id;
    LinearLayout edits;
    ArrayList<String> postes=new ArrayList<>();
    ArrayList<String> communes=new ArrayList<>();
    ArrayList<String> regions=new ArrayList<>();
    ArrayList<String[]> rdc=new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference refPre = db.collection("Preinscription");
    CollectionReference refIns = db.collection("Inscription");
    CollectionReference refUser = db.collection("User");
    CollectionReference refDisc = db.collection("Discussion");
    CollectionReference refMes = db.collection("Message");
    CollectionReference refBv = db.collection("Bv");

    private int calculateAge(long timebirth){
        int age = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            age= Period.between(LocalDate.ofEpochDay(timebirth),LocalDate.now()).getYears();
            return age;
        }
        else {
            Calendar calact = Calendar.getInstance(Locale.ENGLISH);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timebirth * 1000);
            calact.getTime().getYear();
            age=calact.getTime().getYear()-cal.getTime().getYear();
            return age;
        }
    }
    private void LoadCommunes() {
        InputStreamReader is = null;
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

            postes.add("PN");
            postes.add("SG");
            postes.add("DEP");
            postes.add("DED");
            postes.add("DEC");
            postes.add("DECON");
            postes.add("DEPAYS");
            postes.add("Point Focal Region");
            postes.add("SG DEP");
            postes.add("Delegue Regional");
            postes.add("SG CA");
            postes.add("Resp. Comm + Pub");
            postes.add("Agent Comm");
            postes.add("Resp. Lobbying");
            postes.add("SG1 DEP");
            postes.add("SG2 DEP");
            postes.add("SG3 DEP");
            postes.add("Mandataire ELECAM");
            postes.add("Volontaire");
            postes.add("Militant");
            postes.add("Citoyen");

            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(";");
                //bv.add(cells[3]);
                rdc.add(cells);
                if(!verifyIn(cells[2],communes))
                    communes.add(cells[2]);
                //if(!verifyIn(cells[1],departements))
                //   departements.add(cells[1]);
            }
            Toast.makeText(this, "loading complete", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, Service_AddBv.class);
            //startService(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
        }
    }
    private String getDepartement(String key){
        for(int i=0;i<rdc.size();i++){
            if(key.equals(rdc.get(i)[2]))
                return rdc.get(i)[1];
        }
        return "none";
    }
    private String getRegion(String key){
        for(int i=0;i<rdc.size();i++){
            if(key.equals(rdc.get(i)[2]))
                return rdc.get(i)[0];
        }
        return "none";
    }
    public boolean verifyIn(String test, ArrayList<String> list){
        for (int i=0; i<list.size();i++){
            if (test.equals(list.get(i)))
                return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel);
        user_id=getIntent().getStringExtra("pers_id");
        edits=findViewById(R.id.edits);
        progressBar=findViewById(R.id.progressBar);
        edit_commune=findViewById(R.id.edit_commune);
        edit_poste=findViewById(R.id.edit_poste);
        efforts_totaux=findViewById(R.id.efforts_totaux);
        efforts_mois=findViewById(R.id.efforts_mois);
        nom_titre=findViewById(R.id.nom_titre);
        complete_name=findViewById(R.id.complete_name);
        big_initials=findViewById(R.id.big_initials);
        sexe=findViewById(R.id.sexe);
        age=findViewById(R.id.age);
        telephone=findViewById(R.id.telephone);
        email=findViewById(R.id.email);
        commune=findViewById(R.id.commune);
        round_letter=findViewById(R.id.round_letter);
        poste=findViewById(R.id.poste);
        id=findViewById(R.id.id);
        back=findViewById(R.id.back);
        message=findViewById(R.id.message);
        edit=findViewById(R.id.edit);

        ArrayAdapter<String> adapter_commune = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, communes);
        edit_commune.setAdapter(adapter_commune);
        ArrayAdapter<String> adapter_poste = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, postes);
        edit_poste.setAdapter(adapter_poste);

        LoadCommunes();

        if(user_id!=null ){
            refUser.whereEqualTo("id",user_id).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    for (QueryDocumentSnapshot userD : userDocs) {
                        User user = map_user(userD);
                        email.setText("Email : "+user.getEmail());
                        id.setText("Id : "+user.getCode());
                        commune.setText("Militant Commune : "+user.getCommune());
                        poste.setText("Poste : "+user.getType());
                        edit_commune.setText(user.getCommune());
                        edit_poste.setText(user.getType());
                        telephone.setText("Telephone : "+user.getTelephone());
                        complete_name.setText(user.getNom()+" "+ user.getPrenom());
                        round_letter.setText(""+user.getNom().toUpperCase().charAt(0));
                        big_initials.setText(user.getNom().substring(0,2).toUpperCase());
                        nom_titre.setText(user.getNom().split(" ")[0]);
                        sexe.setText("Sexe : " +user.getSexe());
                        age.setText("Age : "+calculateAge(Long.valueOf(user.getDate_naissance())));
                        final int[] couverturePre = {0};
                        final int[] couvertureIns = {0};
                        final int[] couverturePremois = {0};
                        final int[] couvertureInsmois = {0};
                        long actual=System.currentTimeMillis()/1000;
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(edits.getVisibility()==View.VISIBLE){
                                    progressBar.setVisibility(View.VISIBLE);
                                  if (edit_commune.getText().toString().isEmpty())
                                      edit_commune.setHintTextColor(ContextCompat.getColor
                                              (Activity_personnel.this, R.color.red));
                                  else if (edit_poste.getText().toString().isEmpty())
                                      edit_poste.setHintTextColor(ContextCompat.getColor(Activity_personnel.this, R
                                              .color.red));
                                  else{
                                      refUser.document(user.getId()).update("region",getRegion(edit_commune.getText().toString()));
                                      refUser.document(user.getId()).update("departement",getDepartement(edit_commune.getText().toString()));
                                      refUser.document(user.getId()).update("commune",edit_commune.getText().toString());
                                      refUser.document(user.getId()).update("type",edit_poste.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if(task.isSuccessful()){
                                                  progressBar.setVisibility(View.GONE);
                                                  edit.setImageResource(R.drawable.ic_mode_edit_white_24dp);
                                                  edits.setVisibility(View.GONE);
                                                  commune.setText("Militant Commune : "+edit_commune.getText().toString());
                                                  poste.setText("Poste : "+edit_poste.getText().toString());
                                                  commune.setVisibility(View.VISIBLE);
                                                  poste.setVisibility(View.VISIBLE);
                                              }
                                              else {
                                                  progressBar.setVisibility(View.GONE);
                                                  edit.setImageResource(R.drawable.ic_mode_edit_white_24dp);
                                                  edits.setVisibility(View.GONE);
                                                  commune.setVisibility(View.VISIBLE);
                                                  poste.setVisibility(View.VISIBLE);
                                                  Toast.makeText(Activity_personnel.this, "Modification echouee, verifiez votre connexion Internet",
                                                          Toast.LENGTH_SHORT).show();
                                              }


                                          }
                                      });
                                  }
                                }
                                else{
                                    edit.setImageResource(R.drawable.ic_check_black_24dp);
                                    edits.setVisibility(View.VISIBLE);
                                    edit_commune.setText(edit_commune.getText().toString());
                                    edit_poste.setText(user.getType());
                                    commune.setVisibility(View.GONE);
                                    poste.setVisibility(View.GONE);
                                }
                            }
                        });
                        message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressBar.setVisibility(View.VISIBLE);
                                refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                        for (QueryDocumentSnapshot userD : userDocs) {
                                            User usr = map_user(userD);
                                            if (usr.getEmail().equals(Ui.getEmail())) {
                                                refDisc.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot discDocs, @Nullable FirebaseFirestoreException e) {
                                                        int i=0;
                                                        for (QueryDocumentSnapshot discD:discDocs) {
                                                            Discussion ds=map_disc(discD);
                                                            if (ds.getType().equals("tchat") & (ds.getInterlocuteur().equals(user_id) & ds.getInitiateur().equals(usr.getId()))
                                                                    | ds.getInterlocuteur().equals(usr.getId()) & ds.getInitiateur().equals(user_id)){
                                                                Intent intent= new Intent(Activity_personnel.this,Activity_tchat.class);
                                                                intent.putExtra("Id_disc",ds.getId());
                                                                intent.putExtra("user_id",usr.getId());
                                                                intent.putExtra("disc_title",ds.getTitle());
                                                                intent.putExtra("type",ds.getType());
                                                                progressBar.setVisibility(View.GONE);
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                            else if(i==discDocs.size()){
                                                                Discussion dsc= new Discussion();
                                                                dsc.setTitle(user.getNom());
                                                                dsc.setType("tchat");
                                                                dsc.setInitiateur(usr.getId());
                                                                dsc.setInterlocuteur(user_id);
                                                                String key = refDisc.document().getId();
                                                                dsc.setId(key);
                                                                refDisc.document(key).set(dsc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Intent intent= new Intent(Activity_personnel.this,Activity_tchat.class);
                                                                        intent.putExtra("Id_disc",dsc.getId());
                                                                        intent.putExtra("user_id",usr.getId());
                                                                        intent.putExtra("disc_title",dsc.getTitle());
                                                                        intent.putExtra("type",dsc.getType());
                                                                        progressBar.setVisibility(View.GONE);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        refPre.whereEqualTo("parrain",user.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                for (QueryDocumentSnapshot insD : insDocs) {
                                    Preinscription pre = map_pre(insD);
                                    couverturePre[0]++;
                                    if (actual-pre.getCreation_date()<30*24*3600)
                                        couverturePremois[0]++;


                                }
                                refIns.whereEqualTo("parrain",user.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                        for (QueryDocumentSnapshot insD : insDocs) {
                                            Inscription ins = map_ins(insD);
                                            if (ins.getParrain().equals(user.getId())){
                                                couvertureIns[0]++;
                                                if (actual-ins.getCreation_date()<30*24*3600)
                                                    couvertureInsmois[0]++;
                                            }
                                        }
                                        efforts_totaux.setText("Efforts globaux : " +
                                                ""+couverturePre[0]+" " +
                                                " Preins/"+couvertureIns[0]+" Ins");
                                        efforts_mois.setText("Efforts du mois: " +
                                                ""+couverturePre[0]+" " +
                                                "Preins/"+couvertureIns[0]+" Ins");
                                        progressBar.setVisibility(View.GONE);
                                    }

                                });
                            }
                        });
                    }
                }


            });
        }
        else{
            message.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            refUser.whereEqualTo("id",user_id).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    for (QueryDocumentSnapshot userD : userDocs) {
                        User usr = map_user(userD);
                       email.setText("Email : "+usr.getEmail());
                       id.setText("Id : "+usr.getCode());
                       commune.setText("Militant Commune : "+usr.getCommune());
                       poste.setText("Poste : "+usr.getType());
                       telephone.setText("Telephone : "+usr.getTelephone());
                       complete_name.setText(usr.getNom()+" "+ usr.getPrenom());
                       round_letter.setText(""+usr.getNom().toUpperCase().charAt(0));
                       big_initials.setText(usr.getNom().substring(0,2).toUpperCase());
                       nom_titre.setText(usr.getNom());
                       sexe.setText("Sexe : "+usr.getSexe());
                       age.setText("Age : "+calculateAge(Long.valueOf(usr.getDate_naissance())));
                       final int[] couverturePre = {0};
                       final int[] couvertureIns = {0};
                       final int[] couverturePremois = {0};
                       final int[] couvertureInsmois = {0};
                       long actual=System.currentTimeMillis()/1000;
                        refPre.whereEqualTo("parrain",user.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                for (QueryDocumentSnapshot insD : insDocs) {
                                    Preinscription pre = map_pre(insD);
                                    couverturePre[0]++;
                                    if (actual-pre.getCreation_date()<30*24*3600)
                                        couverturePremois[0]++;
                               }
                                refIns.whereEqualTo("parrain",user.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@javax.annotation.Nullable QuerySnapshot insDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                        for (QueryDocumentSnapshot insD : insDocs) {
                                            Inscription ins = map_ins(insD);
                                           if (ins.getParrain().equals(user.getId())){
                                               couvertureIns[0]++;
                                               if (actual-ins.getCreation_date()<30*24*3600)
                                                   couvertureInsmois[0]++;
                                           }
                                       }
                                       efforts_totaux.setText("Efforts globaux : " + ""+couverturePre[0]+"/"+couvertureIns[0]);
                                       efforts_mois.setText("Efforts des derniers 30 jours :" + ""+couverturePremois[0]+"/"+couvertureInsmois[0]);
                                       progressBar.setVisibility(View.GONE);
                                   }

                               });
                           }
                       });
                        progressBar.setVisibility(View.GONE);
                        break;

                    }

                }
            });

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edits.getVisibility()==View.VISIBLE){
                    edits.setVisibility(View.GONE);
                    edit.setImageResource(R.drawable.ic_mode_edit_white_24dp);
                    commune.setVisibility(View.VISIBLE);
                    poste.setVisibility(View.VISIBLE);
                }
                else
                    finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(edits.getVisibility()==View.VISIBLE){
            edits.setVisibility(View.GONE);
            edit.setImageResource(R.drawable.ic_mode_edit_white_24dp);
            commune.setVisibility(View.VISIBLE);
            poste.setVisibility(View.VISIBLE);
        }
        else
            finish();

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
        try{
            dis.last_message=discD.getString("last_message");
            dis.last_writer=discD.getString("last_writer");
            dis.last_date=discD.getLong("last_date");
            dis.initiateur=discD.getString("initiateur");
            dis.interlocuteur=discD.getString("interlocuteur");
        }
        catch (Exception e2){
        }

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
