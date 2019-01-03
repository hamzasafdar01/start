package com.example.root.start;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.root.start.App.CHANNEL_1_ID;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference dbRef,setagainref;
    String name;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    public void onCreate() {

        super.onCreate();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Toast.makeText(getApplicationContext(),"Service Created",Toast.LENGTH_LONG).show();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        FirebaseDatabase fDatabase =FirebaseDatabase.getInstance();
        DatabaseReference dReferance = fDatabase.getReference();
        setagainref = FirebaseDatabase.getInstance().getReference().child("Users");
        dReferance.child("Users").child(user.getUid()).child("Joined Circle")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        CreateUser cu = null;
//                        String test;
//                        ArrayList<String> alist = new ArrayList<>();
                        Iterable<DataSnapshot> childern = dataSnapshot.getChildren();

                        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");






                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                name = dataSnapshot.child(user.getUid()).child("recentAddedBy").getValue(String.class);
                                if(!name.equals("na"))
                                {

                                    Toast.makeText(getApplicationContext(),name+" Added You to see your location",Toast.LENGTH_LONG).show();


                                    sendOnChannel1(name + " Added you to see your location");





                                    setagainref.child(user.getUid()).child("recentAddedBy").setValue("na");
                                }
                            }

                             CreateUser cu = null;@Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });








                        for (DataSnapshot child:childern)
                        {
//                            Log.d("hamza", "onDataChange: "+dataSnapshot.getValue());

//                            alist.add(child.child("circleMemberId").getValue().toString());

//                            String test1 =child.child("circlememberid").getValue().toString();
//                            alist.add(test1);
                            // cu = dataSnapshot.getValue(CreateUser.class);

//                            if (cu.issharing.equals("true"))
//                            {
//                                Toast.makeText(getApplicationContext(),cu.name,Toast.LENGTH_LONG).show();
//                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void sendOnChannel1(String message)
    {
        Intent intent = new Intent(this, JoinedCircleActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationCompat = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.message)
                .setContentTitle("Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();

        notificationManagerCompat.notify(1,notificationCompat);
    }
}
