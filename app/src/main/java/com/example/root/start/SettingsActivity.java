package com.example.root.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {



    TextView change;
    CardView changeLanguage,changeTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        changeLanguage=findViewById(R.id.change_language);
       // changeTheme = findViewById(R.id.change_theme);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

//        changeTheme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showThemeDialog();
//            }
//        });
    }


    ///language processs
    public void showChangeLanguageDialog()
    {
        final String[] listitems = {"English","اردو"};
        AlertDialog.Builder mBuilder =  new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    setLocale("en");
                    Intent myintent = new Intent(SettingsActivity.this,UserLocationMainActivity.class);
                    startActivity(myintent);
                    recreate();


                }
                else  if (which==1){
                    setLocale("ur");
                    Intent myintent = new Intent(SettingsActivity.this,UserLocationMainActivity.class);
                    startActivity(myintent);
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }



    public void setLocale(String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",language);
        editor.apply();


    }


    public void loadLocale()
    {
        SharedPreferences preferences = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang","");
        setLocale(language);
    }
///////////////////////////////////////////////////


    //goto change password activity

    public void gotoChangePasswordActivity(View v)
    {
        Intent myintent = new Intent(SettingsActivity.this,changePasswordActivity.class);
        startActivity(myintent);
    }



/////////////change theme process

    public void showThemeDialog()
    {
        final String[] listitems = {"Main","Dark","Light"};
        AlertDialog.Builder mBuilder =  new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose Theme");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    setTheme(R.style.AppTheme);
                    Intent myintent = new Intent(SettingsActivity.this,UserLocationMainActivity.class);
                    startActivity(myintent);
                    recreate();


                }
                else  if (which==1){
                    setTheme(R.style.AppTheme2);
                    //setContentView(R.layout.activity_settings);
                 Intent myintent = new Intent(SettingsActivity.this,UserLocationMainActivity.class);
                  startActivity(myintent);
                   recreate();
                } else  if (which==2){
                    setTheme(R.style.AppTheme3);
                   Intent myintent = new Intent(SettingsActivity.this,UserLocationMainActivity.class);
                   // setContentView(R.layout.activity_settings);
                    startActivity(myintent);
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }





}
