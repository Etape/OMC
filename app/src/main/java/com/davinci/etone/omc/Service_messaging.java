package com.davinci.etone.omc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

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

import java.util.ArrayList;

public class Service_messaging extends Service {

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    ArrayList<Message> allMessages=new ArrayList<>();
    ArrayList<Discussion> allDiscs=new ArrayList<>();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference refPre = db.collection("Preinscription");
    CollectionReference refIns = db.collection("Inscription");
    CollectionReference refUser = db.collection("User");
    CollectionReference refDisc = db.collection("Discussion");
    CollectionReference refMes = db.collection("Message");
    CollectionReference refBv = db.collection("Bv");
    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        Log.i("Notification played"," Service started");
        refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot userDocs, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("firebase ","Error:"+e.getMessage());
                }
                else {
                for (QueryDocumentSnapshot userD : userDocs) {
                    if (map_user(userD).getEmail().equals(Ui.getEmail())){
                    User user = map_user(userD);
                    refDisc.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot discDocs, @Nullable FirebaseFirestoreException e) {
                            allDiscs.clear();
                            for (QueryDocumentSnapshot discD:discDocs) {
                                    Discussion dis=map_disc(discD);
                                    allDiscs.add(dis);
                                }
                                refMes.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot mesDocs, @Nullable FirebaseFirestoreException e) {
                                        allMessages.clear();
                                        for (QueryDocumentSnapshot mesD : mesDocs) {
                                            Message mes=new Message();
                                            mes.emetteur=mesD.getString("emetteur");
                                            mes.recepteur=mesD.getString("recepteur");
                                            mes.contenu=mesD.getString("contenu");
                                            mes.disc_id=mesD.getString("disc_id");
                                            mes.etat=mesD.getString("etat");
                                            mes.id=mesD.getId();
                                            mes.date_envoi=mesD.getLong("date_envoi");
                                            allMessages.add(mes);
                                        }

                                        for(int i=0;i<allMessages.size();i++){
                                            Message mes=allMessages.get(i);
                                            if(mes.getRecepteur().equals(user.getId()) & mes.getEtat().equals("non lu")){
                                                Log.i("Notification played"," Unread message found "+mes.getContenu());
                                                for (int j=0;j<allDiscs.size();j++){
                                                    if (allDiscs.get(j).getId().equals(mes.getDisc_id())){
                                                        Discussion disc=allDiscs.get(j);
                                                        String NOTIFICATION_CHANNEL_ID = "OMC_id_01";
                                                        NotificationCompat.Builder b = new NotificationCompat.Builder(Service_messaging.this, NOTIFICATION_CHANNEL_ID);
                                                        Intent intent=new Intent(Service_messaging.this,Activity_tchat.class);
                                                        intent.putExtra("Id_disc",allDiscs.get(j).getId());
                                                        intent.putExtra("user_id",user.getId());
                                                        String title=allDiscs.get(j).getTitle();
                                                        if(disc.getType().toUpperCase().equals("TCHAT") & !disc.getInterlocuteur().equals(user.getId()) & disc.getInitiateur().equals(user.getId()))
                                                            intent.putExtra("inter_id",disc.getInterlocuteur());
                                                        else if(disc.getType().toUpperCase().equals("TCHAT") & disc.getInterlocuteur().equals(user.getId()) & !disc.getInitiateur().equals(user.getId()))
                                                            intent.putExtra("inter_id",disc.getInitiateur());
                                                        if (title.equals(user.getNom())){
                                                            refUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onEvent(@javax.annotation.Nullable QuerySnapshot userDocs, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                                                    for (QueryDocumentSnapshot userD : userDocs) {
                                                                        User usr2 = map_user(userD);
                                                                        if ((usr2.getId().equals(disc.getInterlocuteur()) & user.getId().equals(disc
                                                                                .getInitiateur())) | (user.getId().equals(disc.getInterlocuteur())
                                                                                & usr2.getId().equals(disc.getInitiateur())) )
                                                                        {
                                                                            intent.putExtra("disc_title",usr2.getNom());
                                                                            intent.putExtra("type",disc.getType());
                                                                            PendingIntent pendingIntent=PendingIntent.getActivity(Service_messaging.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                                                                            b.setAutoCancel(true)
                                                                                    .setSmallIcon(R.drawable.logo_omc2_50)
                                                                                    .setContentTitle(usr2.getNom())
                                                                                    .setPriority(Notification.PRIORITY_HIGH)
                                                                                    .setContentIntent(pendingIntent)
                                                                                    .setContentText(mes.getContenu())
                                                                                    .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);
                                                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Service_messaging.this);
                                                                            notificationManager.notify(1, b.build());
                                                                            Log.i("Notification"," Notification Played");
                                                                            break;
                                                                        }
                                                                    }
                                                                }

                                                            });

                                                        }
                                                        else {
                                                        intent.putExtra("disc_title",title);
                                                        intent.putExtra("type",disc.getType());
                                                        PendingIntent pendingIntent=PendingIntent.getActivity(Service_messaging.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                                                        b.setAutoCancel(true)
                                                                .setSmallIcon(R.drawable.logo_omc2_50)
                                                                .setContentTitle(allDiscs.get(j).getTitle())
                                                                .setPriority(Notification.PRIORITY_HIGH)
                                                                .setContentIntent(pendingIntent)
                                                                .setContentText(mes.getContenu())
                                                                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);
                                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Service_messaging.this);
                                                        notificationManager.notify(1, b.build());
                                                        Log.i("Notification"," Notification Played");
                                                        break;
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                    }

                                });
                            }

                        });
                    break;
                    }
                }

                }
            }
        });
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    public void PushNotification(Context context,Discussion disc)
    {
        String NOTIFICATION_CHANNEL_ID = "OMC_id_01";
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        CharSequence name = "test";
        String description = "OMC_Notification";
        Intent intent=new Intent(context,Activity_tchat.class);
        intent.putExtra("disc_id",disc.getId());
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel mChannel;
        b.setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_omc2_50)
                .setContentTitle(disc.getTitle())
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentText(disc.getLast_message())
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, b.build());
        Log.i("Notification"," Notification Played");
    }
    public User map_user(QueryDocumentSnapshot docU){
        User user1=new User();
        user1.id=docU.getString("id");
        user1.cni=docU.getString("cni");
        try {
            user1.activite = Math.toIntExact(docU.getLong("activite"));
        }
        catch (Exception e){
            user1.activite=0;
        }
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
