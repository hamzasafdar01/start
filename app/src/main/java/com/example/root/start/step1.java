package com.example.root.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class step1 extends AppCompatActivity {
    TextView t1;
    Button get_started;
    FirebaseAuth auth;
    FirebaseUser user;

    PermissionManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            setContentView(R.layout.activity_step1);

            manager = new PermissionManager() {
            };

            manager.checkAndRequestPermissions(this);

        }
        else{
            Intent myintent = new Intent(step1.this,UserLocationMainActivity.class);
            startActivity(myintent);
            finish();
        }





        t1 = findViewById(R.id.textView2);
        get_started = findViewById(R.id.get_started);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode,permissions,grantResults);

        ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;

        if (denied_permissions.isEmpty()){
            Toast.makeText(getApplicationContext(),"Permissions Enabled",Toast.LENGTH_SHORT).show();
        }





    }

    public void goto_login_page(View v){

             Intent myintent = new Intent(step1.this,login.class);
             startActivity(myintent);
    }


    public void goto_signup_page(View v){
        Intent myintent = new Intent(step1.this,SignUp.class);
        startActivity(myintent);
    }

}
