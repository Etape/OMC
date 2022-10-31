package com.davinci.etone.omc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_menu extends AppCompatActivity {
    TextView addEvent,account,help,preferences,apropos,add_event,add_info,add_action,apropos_txt;
    LinearLayout moreEvents;
    ImageView back;
    User user=new User();
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
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

        back=findViewById(R.id.back);
        moreEvents=findViewById(R.id.more_events);

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
}
