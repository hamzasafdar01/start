package com.example.root.start;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCircleActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    FirebaseUser user;
    MembersAdapter adapter;
    CreateUser createUser;
    ArrayList<CreateUser> namelist;
    DatabaseReference reference,userReference;
    String circlememberid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);

        recyclerView = findViewById(R.id.recyclerview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        namelist = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MembersAdapter(namelist,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        //testing

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("CircleMembers");

        Log.i("Jaleel",reference.getKey());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namelist.clear();
                Log.i("Jaleel"," Clearing List....");
                Log.i("Jaleel"," datasnapshot count "+dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists())
                {
                    Log.i("Jaleel",dataSnapshot.getChildrenCount()+"  ........");


                    for (DataSnapshot dss: dataSnapshot.getChildren())
                    {



                        Log.i("Jaleel",dss.getKey());
                        circlememberid = dss.child("circleMemberId").getKey();
                        Log.i("Jaleel",circlememberid.toString());

                        userReference.child(dss.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.i("Jaleel",dataSnapshot.toString());
                                        Log.d("Jaleel", "onDataChange: Creating user and adding to list");


                                        namelist.add(dataSnapshot.getValue(CreateUser.class));


                                        if (namelist.get(0) == null)
                                            Log.d("Jaleel", "onDataChange: This is ulln");
                                        Log.i("Jaleel",namelist.get(0)+" working....."+ dataSnapshot.getValue());
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        adapter = new MembersAdapter(namelist,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();






    }
}
