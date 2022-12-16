package com.davinci.etone.omc;

import android.content.Intent;
import android.content.IntentSender;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activity_login extends AppCompatActivity {
    EditText email, password;
    Button connexion;
    ImageView google_button;
    TextView problem,inscription,forgotten;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseDatabase Db=FirebaseDatabase.getInstance();
    private static final String TAG = "login";
    String password_txt;
    String email_txt;
    User   User=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();

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
            Toast.makeText(this,"connexion reussie",Toast.LENGTH_LONG).show();
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
}
