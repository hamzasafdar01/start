package com.example.root.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {
    String name,email,password,date,isharing,code;
    String userId;
    Uri imageUri;
    ProgressDialog dialog;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;
    StorageReference sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 = findViewById(R.id.code);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_images");
        Intent myintent = getIntent();

        if (myintent!=null){
            name = myintent.getStringExtra("name");
            email  = myintent.getStringExtra("email");
            password = myintent.getStringExtra("password");
            date = myintent.getStringExtra("date");
            isharing = myintent.getStringExtra("isSharing");
            code = myintent.getStringExtra("code");
            imageUri = myintent.getParcelableExtra("imageUri");


        }
        t1.setText(code);

    }



    public void registerUser(View v) {
        dialog.setMessage("Creating Account...! Please wait");
        dialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //insert value in real database

                            user = auth.getCurrentUser();
                            userId = user.getUid();
                            CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na","na",userId);
                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // sendVarificationEmail();
                                                uploadImage();


                                            }
                                            else{
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"User Didnt Registered Successfully",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }
                    }
                });
    }




//
//    public void uploadImage(){
//
//        //save image to firebase database
//
//       sr = storageReference.child(userId + ".jpg");
//        sr.putFile(imageUri)
//                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful())
//                        {
//
//                        // String download_image_path = sr.getDownloadUrl().toString();
////                            String download_image_path = task.getResult().getMetadata().getContentDisposition();
//                            String download_image_path = task.getResult().getStorage().getDownloadUrl().toString();
//
//                            Log.d("hamza", "onComplete: " + download_image_path);
//
////                         String download_image_path = task.getResult().getDownloadUrl().toString();
//                            reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()){
//                                                   dialog.dismiss();
//                                                sendVarificationEmail();
////                                                                                                Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
////                                                                                                finish();
////                                                                                                Intent myintent = new Intent(InviteCodeActivity.this,UserLocationMainActivity.class);
////                                                                                                startActivity(myintent);
//                                            }
//                                            else
//                                            {
//                                                dialog.dismiss();
//                                                Toast.makeText(getApplicationContext(),"User Didnt Registered Successfully",Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),"Didnt able to upload image",Toast.LENGTH_SHORT).show();
//                sendVarificationEmail();
//            }
//        });
//
//
//    }

/////testing///

//
    public void uploadImage(){

        //save image to firebase database

        final StorageReference sr = storageReference.child(userId + ".jpg");
        //add file on Firebase and got Download Link
        sr.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return sr.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downUri = task.getResult();
                    Log.d("hamza", "onComplete: Url: "+ downUri.toString());



                    reference.child(user.getUid()).child("imageUrl").setValue(downUri.toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        sendVarificationEmail();

                                    }
                                    else
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"User Diddnt Register Succesully",Toast.LENGTH_LONG).show();
                                    }





                                }
                            });






                }
            }
        });











//               .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                   @Override
//                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                       String download_image_path =taskSnapshot.getStorage().getDownloadUrl().toString();
//                       Toast.makeText(getApplicationContext(),download_image_path,Toast.LENGTH_LONG).show();
//                       Log.d("hamza", "onComplete: " + download_image_path);
//                   }
//               });

    }









    ///////////////////////////////////////


    public void sendVarificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Email Sent For Varification",Toast.LENGTH_SHORT).show();

                            //finish();
                            auth.signOut();
                            finish();
                            Intent myintent = new Intent(InviteCodeActivity.this,step1.class);
                            startActivity(myintent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Email cannot be sent",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
