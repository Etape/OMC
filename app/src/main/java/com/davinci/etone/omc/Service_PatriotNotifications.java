package com.davinci.etone.omc;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Service_PatriotNotifications extends Service {
    User user;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference refPat = db.collection("Patriot_messages");
    CollectionReference refUser = db.collection("User");

    ArrayList<Patriot_message> patriot_messages=new ArrayList();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread watching=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (int i=0; i<patriot_messages.size();i++){
                        if (check_printable(patriot_messages.get(i), patriot_messages.get(i).start_date)){
                            patriot_messages.get(i).setStart_date(System.currentTimeMillis()/1000);
                            String NOTIFICATION_CHANNEL_ID = "OMC_id_01";
                            NotificationCompat.Builder b = new NotificationCompat.Builder(Service_PatriotNotifications.this, NOTIFICATION_CHANNEL_ID);
                            Intent intent=new Intent(Service_PatriotNotifications.this,Activity_dashboard.class);
                            PendingIntent pendingIntent=PendingIntent.getActivity(Service_PatriotNotifications.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            b.setAutoCancel(true)
                                    .setSmallIcon(R.drawable.logo_omc2_50)
                                    .setContentTitle("OMC Patriot Notification")
                                    .setPriority(Notification.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                    .setContentText(patriot_messages.get(i).getMessage());
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Service_PatriotNotifications.this);
                            notificationManager.notify(1, b.build());
                            Log.i("Notification"," Notification Played");
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        refUser.whereEqualTo("email", Ui.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot userD:queryDocumentSnapshots){
                    user=map_user(userD);
                    refPat.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot patDocs, @Nullable FirebaseFirestoreException e) {
                            patriot_messages.clear();
                            watching.stop();
                            for (QueryDocumentSnapshot patD:patDocs){
                                Patriot_message patm=new Patriot_message();
                                patm.message=patD.getString("message");
                                patm.frequency=patD.getLong("frequency");
                                patm.hour=patD.getLong("hour");
                                patm.start_date=patD.getLong("start_date");
                                patriot_messages.add(patm);
                            }
                            watching.start();
                        }
                    });
                }
            }
        });
        return START_STICKY;
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
    public boolean check_printable(Patriot_message pat,long last){
        long actual=System.currentTimeMillis()/1000;
        int actualhour=getDate(actual);
        if (actualhour==pat.hour && actual-last>=pat.frequency)
            return true;
        return false;
    }
    private int getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("kk:mm", cal).toString();
        int hour=Integer.parseInt(date.split(":")[0]);
        return hour;
    }

}
