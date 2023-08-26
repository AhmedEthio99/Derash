package com.example.derash2;

import android.content.Intent;
import android.os.*;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }


       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startActivity(new Intent(MainActivity.this, com.example.derash2.LoginActivity.class));
               finish();


           }

        },2800);



    }


}