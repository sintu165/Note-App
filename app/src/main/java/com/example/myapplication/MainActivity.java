package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences =getSharedPreferences("login",MODE_PRIVATE);
                Boolean check=preferences.getBoolean("flag",false);
                Intent intent;
                if(check){
                    intent =new Intent(MainActivity.this,NoteDetailsActivity.class);
                }
                else{
                    intent =new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();



            }
        },3000);





    }
}