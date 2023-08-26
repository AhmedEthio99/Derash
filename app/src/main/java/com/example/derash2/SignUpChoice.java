package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpChoice extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choice);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        ImageButton individualButton = (ImageButton) findViewById(R.id.imageButton);

        individualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpChoice.this, SignUp.class);
                startActivity(intent);
            }
        });

        ImageButton companyButton = (ImageButton) findViewById(R.id.imageButton2);
        companyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpChoice.this, com.example.derash2.CompanyRegistration.class);
                startActivity(intent);
            }
        });
    }

}