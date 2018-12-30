package com.example.root.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changePasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView old_password;
    DatabaseReference databaseReference;
    String email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        old_password = findViewById(R.id.old_password);
//        new_password = findViewById(R.id.new_pass);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                email = dataSnapshot.child(user.getUid()).child("email").getValue().toString();
                pass = dataSnapshot.child(user.getUid()).child("password").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }




    public void confirmOldPassword(View v){
        if (old_password.getText().toString().equals(pass)){
            Toast.makeText(getApplicationContext(),"Matched",Toast.LENGTH_LONG).show();
            Intent myintent = new Intent(changePasswordActivity.this,updatePasswordActivity.class);
            startActivity(myintent);
            
//            new_password.setEnabled(true);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Didnt Matched",Toast.LENGTH_LONG).show();
        }


    }
}
