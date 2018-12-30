package com.example.root.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.core.view.View;

public class ResetPasswordActivity extends AppCompatActivity {
    TextView resetEmail,resetButton;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetEmail = findViewById(R.id.reset_email);
        resetButton = findViewById(R.id.reset);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        resetButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                sendResetEmail();
            }
        });

    }



    public void sendResetEmail(){


        if (resetEmail.getText().toString().equals(""))
        {

            Toast.makeText(getApplicationContext(),"Enter Valid Email",Toast.LENGTH_LONG).show();
        }
        else
        {
            dialog.setMessage("Sending Password Reset Email...!");
            dialog.show();
            auth.fetchProvidersForEmail(resetEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if(task.isSuccessful()){

                                boolean check  = !task.getResult().getProviders().isEmpty();
                                if(!check){
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Enter Valid Email",Toast.LENGTH_LONG).show();

                                }
                                else{

                                       auth.sendPasswordResetEmail(resetEmail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Check your Email Address for Reset link",Toast.LENGTH_LONG).show();
                                            Intent myintent = new Intent(ResetPasswordActivity.this,step1.class);
                                            startActivity(myintent);
                                        }
                                        else
                                        {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Error Occured", Toast.LENGTH_LONG).show();
                                        }
                        }
                    });



                                }
                            }
                        }
                    });


        }

    }
}
