package com.example.root.start;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class service extends Service {
    public service() {
    }
    double lat,lng;
    public service(Long lat,Long lng){
        this.lat=lat;
        this.lng=lng;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void location(double lat,double lng)
    {
        this.lat=lat;
        this.lng = lng;
    }
}
