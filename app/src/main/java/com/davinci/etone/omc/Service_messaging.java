package com.davinci.etone.omc;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Service_messaging extends Service {

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser Ui=auth.getCurrentUser();
    ArrayList<Message> allMessages=new ArrayList<>();
    ArrayList<Discussion> allDiscs=new ArrayList<>();
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();
    DatabaseReference refUser=Db.getReference().child("User");
    User user;
    DatabaseReference refDisc=Db.getReference().child("Discussion");
    DatabaseReference refMes=Db.getReference().child("Message");
    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User usr = postSnapshot.getValue(User.class);
                    if (usr.getEmail().equals(Ui.getEmail()))
                    {
                        user = usr;

                        refDisc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                allDiscs.clear();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Discussion disc = postSnapshot.getValue(Discussion.class);
                                    allDiscs.add(disc);
                                }

                                refMes.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        allMessages.clear();
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Message message = postSnapshot.getValue(Message.class);
                                            allMessages.add(message);
                                        }

                                        for(int i=0;i<allMessages.size();i++){
                                            Message mes=allMessages.get(i);
                                            if(mes.getRecepteur().equals(user.getId()) & mes.getEtat().equals("non lu")){
                                                for (int j=0;j<allDiscs.size();j++){
                                                    if (allDiscs.get(j).getId().equals(mes.getDisc_id())){
                                                        PushNotification(Service_messaging.this,allDiscs.get(j));
                                                        break;
                                                    }
                                                }
                                            }
                                        }


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

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
