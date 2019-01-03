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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    FirebaseAuth auth;

    EditText email,pass;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }





    public void login(View v){
        if (email.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter Valid Email",Toast.LENGTH_LONG).show();
        }
        else if (pass.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_LONG).show();
        }
        else{
            dialog.setMessage("Please wait.");
            dialog.show();
            auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                FirebaseUser user =auth.getCurrentUser();
                                if (user.isEmailVerified()){
                                    dialog.dismiss();
                                    Intent myintent = new Intent(login.this,UserLocationMainActivity.class);
                                    startActivity(myintent);
                                    finish();
                                }
                                else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Email Not varified",Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Wrong Email or Password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void gotoSignupActivity(View v)
    {
        Intent myintent = new Intent(login.this,SignUp.class);
        startActivity(myintent);
    }

    public void gotoForgetPasswordActivity(View v){
        Intent myintent = new Intent(login.this,ResetPasswordActivity.class);
        startActivity(myintent);

    }
}
