package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class Terms_Conditions extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        button = (Button) findViewById(R.id.agreebtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = findViewById(R.id.checkBox);
               boolean termsChecked = checkBox.isChecked();
               if (!termsChecked){
                   Toast.makeText(Terms_Conditions.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();

               } else {
                   Intent intent = new Intent(Terms_Conditions.this, LoginActivity.class);

                   startActivity(intent);
                   finish();

               }


            }
        });
    }


}