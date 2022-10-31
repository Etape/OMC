package com.davinci.etone.omc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Service_SendSms extends Service {
    public Service_SendSms() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
