package com.davinci.etone.omc;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.util.Date;
import java.util.Locale;

public class Activity_tchat extends AppCompatActivity {
    User user=new User();
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    RelativeLayout main;
    EditText message;
    ImageView plus,send,tchat_menu,back;
    RelativeLayout write_bar;
    ScrollView scroll_forum,scroll_sms,scroll_settings;
    Button confirm_send,ok_button;
    MultiAutoCompleteTextView destinaires;
    EditText entete,bas_message;
    RecyclerView messages_recyclerview;
    TextView initials,disc_title,message_content,text_mois;
    FirebaseDatabase Db=FirebaseDatabase.getInstance();

    ArrayList<String> communes=new ArrayList<>();
    ArrayList<String> regions=new ArrayList<>();
    ArrayList<String> sexe=new ArrayList<>();
    ArrayList<String[]> rdc=new ArrayList<>();
    ArrayList<String> departements=new ArrayList<>();
    ArrayList<String> postes=new ArrayList<>();
    ArrayList<String> bv=new ArrayList<>();
    ArrayList<Message> messages=new ArrayList<>();
    ViewHolderMessages viewHolderMessages;
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

    String destinataire;
    String entete_msg;
    String bas_msg;
    String disc_id,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchat);

        main=findViewById(R.id.main);
        plus=findViewById(R.id.plus);
        send=findViewById(R.id.send);
        tchat_menu=findViewById(R.id.tchat_menu);
        messages_recyclerview=findViewById(R.id.messages_recyclerview);
        scroll_forum=findViewById(R.id.messages_scroll);
        scroll_sms=findViewById(R.id.messages_validation_scroll);

        scroll_settings=findViewById(R.id.scroll_settings);
        ok_button=findViewById(R.id.ok_button);
        destinaires=findViewById(R.id.destinataires);
        entete=findViewById(R.id.entete);
        bas_message=findViewById(R.id.bas_message);
        message=findViewById(R.id.message);
        write_bar=findViewById(R.id.write_bar);

         destinataire="";
         entete_msg=entete.getText().toString();
         bas_msg=bas_message.getText().toString();

        initials=findViewById(R.id.initials);
        disc_title=findViewById(R.id.disc_title);
        back=findViewById(R.id.back);
        confirm_send=findViewById(R.id.confirm_send);
        message_content=findViewById(R.id.message_content);
        text_mois=findViewById(R.id.text_mois);

        messages_recyclerview.setHasFixedSize(true);
        messages_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        destinataire="";
        entete_msg=entete.getText().toString();
        bas_msg=bas_message.getText().toString();
        type=getIntent().getStringExtra("type");
        disc_id=getIntent().getStringExtra("Id_disc");
        final String user_id=getIntent().getStringExtra("user_id");
        final String disc_tit=getIntent().getStringExtra("disc_title");

        if(type!=null & (type.equals("forum") | type.equals("tchat"))) {
            tchat_menu.setVisibility(View.GONE);
        }
        Log.i("Firebage","disc_id ="+disc_id);

        text_mois.setVisibility(View.GONE);
        disc_title.setText(disc_tit.split(" ")[0]);
        String initial=""+disc_tit.charAt(0)+""+disc_tit.charAt(1);
        initials.setText(initial.toUpperCase());
        viewHolderMessages =new ViewHolderMessages(this,messages,type,user_id);
        messages_recyclerview.setAdapter(viewHolderMessages);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinataire=destinaires.getText().toString();
                entete_msg=entete.getText().toString();
                bas_msg=bas_message.getText().toString();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message.getText().toString().isEmpty()){
                    if(type.equals("sms")){
                        scroll_sms.setVisibility(View.VISIBLE);
                        scroll_forum.setVisibility(View.GONE);
                        String message_contain= entete_msg+"\n"+message.getText().toString()+"\n"+bas_msg;
                        message_content.setText(message_contain);

                    }
                    else{
                        Message mes=new Message();
                        mes.setContenu(message.getText().toString());
                        mes.setEmetteur(user_id);
                        mes.setDisc_id(disc_id);
                        mes.setDate_envoi(System.currentTimeMillis()/1000);
                        DatabaseReference refDisc=Db.getReference().child("Discussion");
                        DatabaseReference refMes=Db.getReference().child("Message");
                        String key=refMes.push().getKey();
                        mes.setId(key);
                        refMes.child(key).setValue(mes);
                        refDisc.child(disc_id).child("last_message").setValue(mes.getContenu());
                        refDisc.child(disc_id).child("last_writer").setValue(user_id);
                        refDisc.child(disc_id).child("last_date").setValue(mes.getDate_envoi());
                        message.setText("");
                    }
                }
            }
        });
        tchat_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("sms")){
                    write_bar.setVisibility(View.GONE);
                    scroll_sms.setVisibility(View.GONE);
                    scroll_forum.setVisibility(View.GONE);
                    text_mois.setVisibility(View.GONE);
                    scroll_settings.setVisibility(View.VISIBLE);
                }
            }
        });
        confirm_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message mes=new Message();
                mes.setContenu(message_content.getText().toString());
                mes.setEmetteur(user.getId());
                mes.setDisc_id(disc_id);
                mes.setRecepteur(destinataire);
                mes.setDate_envoi(System.currentTimeMillis()/1000);
                DatabaseReference refDisc=Db.getReference().child("Discussion");
                DatabaseReference refMes=Db.getReference().child("Message");
                String key=refMes.push().getKey();
                mes.setId(key);
                refMes.child(key).setValue(mes);
                refDisc.child(disc_id).child("last_message").setValue(mes.getContenu());
                refDisc.child(disc_id).child("last_writer").setValue(user_id);
                refDisc.child(disc_id).child("last_date").setValue(mes.getDate_envoi());
                message.setText("");
                scroll_sms.setVisibility(View.GONE);
                scroll_forum.setVisibility(View.VISIBLE);
            }
        });
        if (type.equals("sms")){
            main.setBackgroundResource(R.drawable.discussion_wallpaper1);
        }
        else if(type.equals("forum")){
            main.setBackgroundResource(R.drawable.discussion_wallpaper4);
        }
        DatabaseReference refMessages= Db.getReference().child("Message");
        refMessages.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Message mes=postSnapshot.getValue(Message.class);
                    if(mes.getDisc_id().equals(disc_id)){
                        messages.add(mes);
                    }
                }
                viewHolderMessages.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scroll_settings.getVisibility()==View.VISIBLE){
                    write_bar.setVisibility(View.VISIBLE);
                    if(message.getText().toString().isEmpty()){
                        scroll_forum.setVisibility(View.VISIBLE);
                        scroll_sms.setVisibility(View.GONE);
                        text_mois.setVisibility(View.GONE);
                    }
                    else{
                        scroll_sms.setVisibility(View.VISIBLE);
                        scroll_forum.setVisibility(View.GONE);
                        text_mois.setVisibility(View.GONE);
                    }
                    scroll_settings.setVisibility(View.GONE);
                }
                else if(scroll_sms.getVisibility()==View.VISIBLE){
                    scroll_forum.setVisibility(View.VISIBLE);
                    scroll_sms.setVisibility(View.GONE);
                    text_mois.setVisibility(View.VISIBLE);
                }
            }
        });

        messages_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) messages_recyclerview.getLayoutManager();
                    int position = linearLayoutManager.findFirstVisibleItemPosition();
                    text_mois.setVisibility(View.VISIBLE);
                    text_mois.setText(getDate(messages.get(position).getDate_envoi()));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (scroll_settings.getVisibility()==View.VISIBLE){
            write_bar.setVisibility(View.VISIBLE);
            if(message.getText().toString().isEmpty()){
                scroll_forum.setVisibility(View.VISIBLE);
                scroll_sms.setVisibility(View.GONE);
                text_mois.setVisibility(View.GONE);
            }
            else{
                scroll_sms.setVisibility(View.VISIBLE);
                scroll_forum.setVisibility(View.GONE);
                text_mois.setVisibility(View.GONE);
            }
            scroll_settings.setVisibility(View.GONE);
        }
        else if(scroll_sms.getVisibility()==View.VISIBLE){
            scroll_forum.setVisibility(View.VISIBLE);
            scroll_sms.setVisibility(View.GONE);
            text_mois.setVisibility(View.VISIBLE);
        }
        if(messages.size()==0 ){
            DatabaseReference refdisc=Db.getReference().child("Discussion");
            if(disc_id!=null & disc_id.length()>2){
                refdisc.child(disc_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        }
    }

    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd/MM", cal).toString();
        String monthStr = "";
        Log.i("Firebase","monthStr :"+date.substring(3,5));
        switch(date.substring(3,5)) {
            case "01":
                monthStr = "Janvier";
                break;
            case "02":
                monthStr = "Fevrier";
                break;
            case "03":
                monthStr = "Mars";
                break;
            case "04":
                monthStr = "Avril";
                break;
            case "05":
                monthStr = "Mai";
                break;
            case "06":
                monthStr = "Juin";
                break;
            case "07":
                monthStr = "Juillet";
                break;
            case "08":
                monthStr = "Aout";
                break;
            case "09":
                monthStr = "Septembre";
                break;
            case "10":
                monthStr = "Octobre";
                break;
            case "11":
                monthStr = "Novembre";
                break;
            case "12":
                monthStr = "Decembre";
                break;
        }
        date=date.substring(0,2)+" "+monthStr;

        return date;
    }
}
