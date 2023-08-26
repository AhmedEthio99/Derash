package com.example.derash2;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class logoutdriver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logoutdriver);

        Button logoutButton = findViewById(R.id.buttonLogoutDriver);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session and log out
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Start Login activity
                Intent intent = new Intent(logoutdriver.this, LoginActivity.class);
                startActivity(intent);
                finish(); // close the current activity
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}