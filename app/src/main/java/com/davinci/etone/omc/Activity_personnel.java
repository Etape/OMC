package com.davinci.etone.omc;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
    DatabaseReference refUser = Db.getReference().child("User");
    ArrayList<String> postes=new ArrayList<>();
    ArrayList<String> communes=new ArrayList<>();
    ArrayList<String> regions=new ArrayList<>();
    ArrayList<String[]> rdc=new ArrayList<>();

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

            if(Ui.getEmail().contains("admin"))
                postes.add("Admin");
            postes.add("Militant");
            postes.add("DEC");
            postes.add("DED");
            postes.add("DER");
            postes.add("DEP");
            postes.add("DEPays");
            postes.add("DECon");
            postes.add("SG");
            postes.add("Mandataire");
            postes.add("SG CA");
            postes.add("Resp. Lobbying");
            postes.add("Resp. Communication");
            postes.add("SG 1");
            postes.add("SG 2");
            postes.add("SG 3");
            postes.add("Point Focal Region");
            postes.add("SG Dept");
            postes.add("Delegue Region");

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
            refUser.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    email.setText("Email : "+user.getEmail());
                    id.setText("Id : "+user.getId());
                    commune.setText("Militant Commune : "+user.getCommune());
                    poste.setText("Poste : "+user.getType());
                    telephone.setText("Telephone : "+user.getTelephone());
                    complete_name.setText(user.getNom()+" "+ user.getPrenom());
                    round_letter.setText(""+user.getNom().toUpperCase().charAt(0));
                    big_initials.setText(user.getNom().substring(0,2).toUpperCase());
                    nom_titre.setText(user.getNom().split(" ")[0]);
                    sexe.setText("Sexe : " +user.getSexe());
                    age.setText("Age : "+calculateAge(Long.valueOf(user.getDate_naissance())));
                    DatabaseReference refBv=Db.getReference().child("Preinscription");
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
                                  refUser.child(user.getId()).child("region").setValue
                                          (getRegion(edit_commune.getText().toString()));
                                  refUser.child(user.getId()).child("departement").setValue
                                          (getDepartement(edit_commune.getText().toString()));
                                  refUser.child(user.getId()).child("commune").setValue(edit_commune.getText().toString());
                                  refUser.child(user.getId()).child("type").setValue(user_id);
                              }
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                edit.setImageResource(R.drawable.ic_check_black_24dp);
                                edits.setVisibility(View.VISIBLE);
                                edit_commune.setText(user.getCommune());
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
                            DatabaseReference refDisc=Db.getReference().child("Discussion");
                            DatabaseReference refu=Db.getReference().child("User");
                            refu.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        User usr = postSnapshot.getValue(User.class);
                                        if (usr.getEmail().equals(Ui.getEmail())) {
                                            refDisc.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    int i=0;
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                        i++;
                                                        Discussion ds=postSnapshot.getValue(Discussion.class);
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
                                                        else if(i==dataSnapshot.getChildrenCount()){
                                                            Discussion dsc= new Discussion();
                                                            dsc.setTitle(user.getNom());
                                                            dsc.setType("tchat");
                                                            dsc.setInitiateur(usr.getId());
                                                            dsc.setInterlocuteur(user_id);
                                                            String key = refDisc.push().getKey();
                                                            dsc.setId(key);
                                                            refDisc.child(key).setValue(dsc).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    });
                    refBv.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Preinscription pre=postSnapshot.getValue(Preinscription.class);
                                if (pre.getParrain().equals(user.getId())){
                                    couverturePre[0]++;
                                    if (actual-pre.getCreation_date()<30*24*3600)
                                        couverturePremois[0]++;

                                }
                            }
                            Db.getReference().child("Inscription").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Inscription ins=postSnapshot.getValue(Inscription.class);
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

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            message.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren() ){
                       User usr = postSnapshot.getValue(User.class);
                       if (usr.getEmail().equals(Ui.getEmail())) {
                            user=dataSnapshot.getValue(User.class);
                           email.setText("Email : "+usr.getEmail());
                           id.setText("Id : "+usr.getId());
                           commune.setText("Militant Commune : "+usr.getCommune());
                           poste.setText("Poste : "+usr.getType());
                           telephone.setText("Telephone : "+usr.getTelephone());
                           complete_name.setText(usr.getNom()+" "+ usr.getPrenom());
                           round_letter.setText(""+usr.getNom().toUpperCase().charAt(0));
                           big_initials.setText(usr.getNom().substring(0,2).toUpperCase());
                           nom_titre.setText(usr.getNom());
                           sexe.setText("Sexe : "+usr.getSexe());
                           age.setText("Age : "+calculateAge(Long.valueOf(usr.getDate_naissance())));
                           DatabaseReference refBv=Db.getReference().child("Preinscription");
                           final int[] couverturePre = {0};
                           final int[] couvertureIns = {0};
                           final int[] couverturePremois = {0};
                           final int[] couvertureInsmois = {0};
                           long actual=System.currentTimeMillis()/1000;
                           refBv.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                       Preinscription pre=postSnapshot.getValue(Preinscription.class);
                                       if (pre.getParrain().equals(user.getId())){
                                           couverturePre[0]++;
                                           if (actual-pre.getCreation_date()<30*24*3600)
                                               couverturePremois[0]++;

                                       }
                                   }
                                   Db.getReference().child("Inscription").addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                               Inscription ins=postSnapshot.getValue(Inscription.class);
                                               if (ins.getParrain().equals(user.getId())){
                                                   couvertureIns[0]++;
                                                   if (actual-ins.getCreation_date()<30*24*3600)
                                                       couvertureInsmois[0]++;
                                               }
                                           }
                                           efforts_totaux.setText("Efforts globaux : " +
                                                   ""+couverturePre[0]+"/"+couvertureIns[0]);
                                           efforts_mois.setText("Efforts des derniers 30 jours :" +
                                                   ""+couverturePremois[0]+"/"+couvertureInsmois[0]);
                                           progressBar.setVisibility(View.GONE);
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                       }
                                   });
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                            progressBar.setVisibility(View.GONE);
                            break;
                       }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
