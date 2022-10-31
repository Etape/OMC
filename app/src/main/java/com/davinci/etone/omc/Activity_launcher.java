package com.davinci.etone.omc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Activity_launcher extends AppCompatActivity {
    ProgressBar progressBar;
    private static int SPLASH_SCREEN = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(Activity_launcher.this,Activity_login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN );
    }

}
