package com.davinci.etone.omc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Service_AddBv extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }
    public Service_AddBv() {
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Bv Registration", "Service started");
                        FirebaseDatabase Db=FirebaseDatabase.getInstance();
                            InputStreamReader is = null;
                            DatabaseReference refBv=Db.getReference().child("Bv");
                            ArrayList<String> registeredBv=new ArrayList<>();
                            try {
                                is = new InputStreamReader(getAssets()
                                        .open("rdc.csv"));
                                BufferedReader reader = new BufferedReader(is);
                                reader.readLine();
                                String line;
                                boolean end=false;
                                int time=0;
                                final boolean[] cont = {true};
                                while (end==false){
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if((line = reader.readLine()) == null)
                                        end=true;
                                    if ((line = reader.readLine()) != null & cont[0]){
                                        String[] cells = line.split(";");
                                         refBv.addValueEventListener(new ValueEventListener
                                                () {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                ArrayList<String> registeredBv_ant=new ArrayList<>();
                                                registeredBv_ant=registeredBv;
                                                cont[0] =false;
                                                registeredBv.clear();
                                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                    Bv bvi=snapshot.getValue(Bv.class);
                                                    registeredBv.add(bvi.getBv_name());
                                                }
                                                if(registeredBv.size()!=registeredBv_ant.size())
                                                    Log.i("Bv Registration", "Bv == " + registeredBv.size());
                                                    if(!verifyIn(cells[3],registeredBv) &
                                                            registeredBv.size()!=0){
                                                        Bv bv_t=new Bv();
                                                        bv_t.setBv_name(cells[3]);
                                                        bv_t.setBv_commune(cells[2]);
                                                        bv_t.setBv_dep(cells[1]);
                                                        bv_t.setBv_region(cells[0]);
                                                        String key=refBv.push().getKey();
                                                        cont[0] =false;
                                                        refBv.child(key).setValue(bv_t).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Log.i("Bv Registration","new Bv added");
                                                                cont[0] =true;

                                                            }
                                                        });
                                                    }
                                                    else{
                                                        cont[0] =true;
                                                    }
                                                }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String  CHANNEL_ID="OMC2022";
                            NotificationCompat.Builder builder = new NotificationCompat
                                    .Builder(Service_AddBv.this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.logo_omc2_50)
                                    .setContentTitle("Upload des BV")
                                    .setContentText("Upload des BV accompli")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        }

                    }

        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean verifyIn(String test, ArrayList<String> list){
        for (int i=0; i<list.size();i++){
            if (test.equals(list.get(i)))
                return true;
        }
        return false;
    }

}
