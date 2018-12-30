package com.example.root.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {
    Pinview pinview;
    DatabaseReference reference,currentReference;
    FirebaseAuth auth;
    FirebaseUser user;
    String current_user_id,join_user_id;
    DatabaseReference circleReference;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        submit = findViewById(R.id.submit);
        pinview = (Pinview)findViewById(R.id.pinview);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        current_user_id = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtononClick();
            }
        });


    }


   public void submitButtononClick()
   {
       //joining circle by add id to another node
       Query query = reference.orderByChild("code").equalTo(pinview.getValue());

       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists())
               {
                   CreateUser createUser = null;
                           for (DataSnapshot childDss : dataSnapshot.getChildren())
                           {
                                createUser = childDss.getValue(CreateUser.class);
                                //may be to write below code outside of the for loop
                                join_user_id = createUser.userId;


//                                circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                        .child(join_user_id).child("CircleMembers"); //creating node named Circle Members
                               circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(user.getUid()).child("CircleMembers"); //creating node named Circle Members
//
//                               currentReference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                       .child(user.getUid()).child("Joined Circle"); //creating node named joined circle



                               currentReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                       .child(join_user_id).child("Joined Circle"); //creating node named joined circle

                               final CircleJoin circleJoin1 = new CircleJoin(current_user_id);
                                 CircleJoin circleJoin2 = new CircleJoin(join_user_id);

                             //   circleReference.child(user.getUid()).setValue(circleJoin1)
                               circleReference.child(join_user_id).setValue(circleJoin2)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    currentReference.child(user.getUid()).setValue(circleJoin1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                   // Toast.makeText(getApplicationContext(),"ho gya",Toast.LENGTH_LONG).show();
                                                                }
                                                            });

                                                    Toast.makeText(getApplicationContext(),"Successfully joined Circle",Toast.LENGTH_SHORT).show();
                                                    Intent myintent = new Intent(JoinCircleActivity.this,UserLocationMainActivity.class);
                                                }
                                            }
                                        });

                           }

               }
               else
               {
                   Toast.makeText(getApplicationContext(),"Circle Code not valid",Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }


}
