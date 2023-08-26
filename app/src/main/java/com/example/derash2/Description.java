package com.example.derash2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.*;

public class Description extends AppCompatActivity {

    Button Englishbtn,Amharicbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Button Englishbtn = findViewById(R.id.Englishbtn);
        Button Amharicbtn = findViewById(R.id.Amharicbtn);

        Englishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new EnglishFragment());
            }
        });
            Amharicbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(new AmharicFragment());
                }
            });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame2,fragment);
        fragmentTransaction.commit();
    }
}