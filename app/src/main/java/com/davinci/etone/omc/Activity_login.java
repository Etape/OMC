package com.davinci.etone.omc;

import android.content.Intent;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Activity_login extends AppCompatActivity {
    EditText email, password;
    Button connexion;
    ImageView google_button;
    TextView problem,inscription,forgotten;
    ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    FirebaseAuth auth;
    private static final String TAG = "login";
    String password_txt;
    String email_txt;
    User   User=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();
        db.setFirestoreSettings(settings);
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null) {
            updateUI(currentUser);
        }
        email = findViewById(R.id.email_txt);
        password = findViewById(R.id.pwd_txt);
        google_button = findViewById(R.id.google_but);
        progressBar = findViewById(R.id.progressBar);
        problem = findViewById(R.id.error_txt);
        inscription = findViewById(R.id.Incription_txt);
        forgotten = findViewById(R.id.forgotten_txt);
        connexion = findViewById(R.id.but_con);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_txt=password.getText().toString();
                email_txt=email.getText().toString();
                if (TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt)){
                    problem.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    problem.setVisibility(View.INVISIBLE);
                    auth.signInWithEmailAndPassword(email_txt.trim(), password_txt)
                            .addOnCompleteListener(Activity_login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "SignInUserWithEmail:success");
                                        User.setEmail(email_txt);
                                        User.setPassword(password_txt);
                                        FirebaseUser user1 = auth.getCurrentUser();
                                        updateUI(user1);
                                        Log.i("Database", "User email " + email_txt + " found in Firebase");
                                    } else {
                                        problem.setText("Connexion impossible, Veuillez verifier " + "vos identifiants");
                                        problem.setVisibility(View.VISIBLE);
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "SignInUserWithEmail: failure", task.getException());
                                        Toast.makeText(Activity_login.this, "Authentication " +
                                                        "failed.",Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                }
                            });
                }
            }
        });
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_login.this,Activity_createAccount.class);
                startActivity(intent);
            }
        });

        forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Activity_login.this,Activity_createAccount.class);
                startActivity(intent);
            }
        });
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            //Toast.makeText(this,"connexion reussie",Toast.LENGTH_LONG).show();
            Intent intent= new Intent(this,Activity_dashboard.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this,"utilisateur inconnu",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
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
}
