package com.example.root.start;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class password_Activity extends AppCompatActivity {
    String email;
    EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_);

        pass = findViewById(R.id.password);

        Intent myintent = getIntent();
        if(myintent!=null){
            email = myintent.getStringExtra("email");
            Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
        }

    }

    public void goto_namepic_activity(View v){
        if(pass.getText().toString().length()>6){
            Intent myintent = new Intent(password_Activity.this,NameActivity.class);
            myintent.putExtra("email",email);
            myintent.putExtra("password",pass.getText().toString());
            startActivity(myintent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Password length should be more then 6 charachters",Toast.LENGTH_LONG).show();
        }
    }




}
