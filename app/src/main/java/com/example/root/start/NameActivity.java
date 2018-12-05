package com.example.root.start;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity {
    String email,password;
    EditText name;
    CircleImageView circleImageView;
    Uri uriresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        name = findViewById(R.id.name);
        circleImageView = findViewById(R.id.circleImageView);


        Intent myintent =getIntent();
        if(myintent!=null){
            email = myintent.getStringExtra("email");
            password = myintent.getStringExtra("password");
        }
    }



    public void generateCode(View v){
        Date mydate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String date =format1.format(mydate);
        Random r =new Random();

        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);

        if(uriresult!=null){
            Intent myintent  = new Intent(NameActivity.this,InviteCodeActivity.class);
            myintent.putExtra("name",name.getText().toString());
            myintent.putExtra("email",email);
            myintent.putExtra("password",password);
            myintent.putExtra("date",date);
            myintent.putExtra("code",code);
            myintent.putExtra("isSharing","false");
            myintent.putExtra("imageUri",uriresult);
            startActivity(myintent);
            finish();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Choose and Image",Toast.LENGTH_LONG).show();
        }
    }


    public void selectImage(View v){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_OPEN_DOCUMENT);
        i.setType("image/*");
        startActivityForResult(i,12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==12 && resultCode==RESULT_OK && data!=null){
            uriresult = data.getData();
            circleImageView.setImageURI(uriresult);
        }
    }



}
