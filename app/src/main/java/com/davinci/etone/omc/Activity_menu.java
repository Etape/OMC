package com.davinci.etone.omc;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Activity_menu extends AppCompatActivity {
    TextView addEvent,account,help,preferences,apropos,add_event,add_info,add_action,apropos_txt,deconnexion;
    LinearLayout moreEvents;
    ImageView back;
    User user;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();

    FirebaseDatabase Db=FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference refPre = db.collection("Preinscription");
    CollectionReference refIns = db.collection("Inscription");
    CollectionReference refUser = db.collection("User");
    CollectionReference refDisc = db.collection("Discussion");
    CollectionReference refMes = db.collection("Message");
    CollectionReference refBv = db.collection("Bv");
    boolean see_events=false,see_apropos=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        addEvent=findViewById(R.id.addevent);
        preferences=findViewById(R.id.preferences);
        apropos=findViewById(R.id.apropos);
        apropos_txt=findViewById(R.id.version_text);
        account=findViewById(R.id.compte);
        help=findViewById(R.id.aide);
        add_event=findViewById(R.id.add_event);
        add_action=findViewById(R.id.add_action);
        add_info=findViewById(R.id.add_info);
        deconnexion=findViewById(R.id.deconnexion);

        back=findViewById(R.id.back);
        moreEvents=findViewById(R.id.more_events);

        refUser.whereEqualTo("email", Ui.getEmail()).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot userD : userDocs) {
                    User user = map_user(userD);
                    Postes poste= new Postes(user.getType());
                    Log.i("Firebase user type",user.getType());
                    Log.i("Firebase categorie",poste.getCategorie());
                    if (poste.getCategorie().equals("C") | poste.getCategorie().equals("D")){
                        addEvent.setVisibility(View.GONE);
                    }

                }
            }

        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                see_events=!see_events;
                if(see_events){
                    moreEvents.setVisibility(View.VISIBLE);
                }
                else {
                    moreEvents.setVisibility(View.GONE);
                }
            }
        });
        apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                see_apropos=!see_apropos;
                if(see_apropos){
                    apropos_txt.setVisibility(View.VISIBLE);
                }
                else {
                    apropos_txt.setVisibility(View.GONE);
                }
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_menu.this,Activity_personnel.class);
                startActivity(intent);
            }
        });
        add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_menu.this,Activity_Add_info.class);
                intent.putExtra("type","info");
                startActivity(intent);
            }
        });
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_menu.this,Activity_login.class);
                auth.signOut();
                startActivity(intent);
            }
        });
        add_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_menu.this,Activity_Add_info.class);
                intent.putExtra("type","action");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
    public User map_user(QueryDocumentSnapshot docU){
        User user1=new User();
        try{
            String test=docU.getString("email");

            user1.id=docU.getString("id");
            user1.cni=docU.getString("cni");
            try {
                user1.activite = Math.toIntExact(docU.getLong("activite"));
            }
            catch (Exception e1){
                user1.activite=0;
            }
            user1.code=docU.getString("code");
            user1.commune=docU.getString("commune");
            user1.comite_base=docU.getString("comite_base");
            try {
                user1.creation_date=docU.getLong("creation_date");
            }
            catch (Exception e1){

            }
            try {
                user1.date_naissance = docU.getString("date_naissance");
            }
            catch (Exception e3){
                user1.date_naissance = ""+docU.getLong("date_naissance");
            }
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

        }
        catch (Exception e4){
            user1.prenom=docU.getString("prenom");
            user1.nom=docU.getString("nom");
            user1.id=docU.getString("id");
            user1.type=docU.getString("type");

        }
        return user1;
    }
}
