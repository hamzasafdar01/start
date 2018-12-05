package com.example.root.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class SignUp extends AppCompatActivity {

    EditText email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        email = findViewById(R.id.register_email);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

    }


    public void goto_password_activity(View v){
        dialog.setMessage("Checking Email address");
        dialog.show();
        auth.fetchProvidersForEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            boolean check  = !task.getResult().getProviders().isEmpty();
                            if(!check){
                                //Email dose not exist
                                Intent myintent = new Intent(SignUp.this,password_Activity.class);
                                myintent.putExtra("email", email.getText().toString());
                                startActivity(myintent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Email already exist",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    }
                });

    }
}
