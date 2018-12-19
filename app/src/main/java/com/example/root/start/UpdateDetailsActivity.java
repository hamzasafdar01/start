package com.example.root.start;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDetailsActivity extends AppCompatActivity {
    TextView name,password,confirmPassword,e_login;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    AuthCredential credential;
    String email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        name= findViewById(R.id.e_name);
        password = findViewById(R.id.e_password);
        confirmPassword = findViewById(R.id.e_c_password);
        e_login = findViewById(R.id.e_login);

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        e_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    email = dataSnapshot.child(user.getUid()).child("email").getValue().toString();
                    pass = dataSnapshot.child(user.getUid()).child("password").getValue().toString();
               //name.setText(dataSnapshot.child(user.getUid()).child("name").getValue().toString());
//                email.setText(dataSnapshot.child(user.getUid()).child("email").getValue().toString());
//                password.setText(dataSnapshot.child(user.getUid()).child("password").getValue().toString());
//                name.setTag(name.getKeyListener());
//                name.setKeyListener(null);
//                name.setKeyListener((KeyListener) name.getTag());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void updatepassword()
    {

        if(password.getText().toString().equals(confirmPassword.getText().toString()))
        {
            credential = EmailAuthProvider.getCredential(email,pass);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                user.updatePassword(password.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_LONG).show();
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
