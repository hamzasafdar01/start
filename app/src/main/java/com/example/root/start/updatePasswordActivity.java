package com.example.root.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

public class updatePasswordActivity extends AppCompatActivity {
        TextView new_pass,new_confirm_pass,update_pass;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    AuthCredential credential;
    String email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        new_pass = findViewById(R.id.new_pass);
        new_confirm_pass = findViewById(R.id.new_confrm_pass);
        update_pass = findViewById(R.id.update_pass);

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



    update_pass.setOnClickListener(new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            updatepassword();
        }
    });
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



    public void updatepassword()
    {

        if(new_pass.getText().toString().equals(new_confirm_pass.getText().toString()))
        {
            credential = EmailAuthProvider.getCredential(email,pass);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                user.updatePassword(new_pass.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_LONG).show();
                                                    Intent myintent = new Intent(updatePasswordActivity.this,UserLocationMainActivity.class);
                                                    startActivity(myintent);
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),"Error..!Password not updated",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }

    }



}
