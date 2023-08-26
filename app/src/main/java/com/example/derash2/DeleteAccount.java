package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class DeleteAccount extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String phoneValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/").getReference();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            phoneValue = extras.getString("phoneValue");
        }

        Button buttonDeleteAcc = findViewById(R.id.buttonDeleteAcc);
        buttonDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void deleteAccount() {
        DatabaseReference userRef = databaseReference.child("users").child(phoneValue);
        DatabaseReference companyRef = databaseReference.child("company").child(phoneValue);

        // Remove user data
        userRef.removeValue();

        // Remove company data
        companyRef.removeValue();

        Toast.makeText(DeleteAccount.this, "Account has been deleted", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(DeleteAccount.this, LoginActivity.class);
        startActivity(intent);        // Finish the activity and go back to the previous screen
        finish();
    }
}
