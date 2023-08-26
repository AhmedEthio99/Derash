package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class DriverLogin extends AppCompatActivity {
    EditText passwordDriver;
    boolean passwordVisible;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        passwordDriver = findViewById(R.id.passwordDriver);
        passwordDriver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= passwordDriver.getRight() - passwordDriver.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = passwordDriver.getSelectionEnd();
                        if (passwordVisible) {
                            passwordDriver.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off2_24, 0);
                            passwordDriver.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            passwordDriver.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility2_24, 0);
                            passwordDriver.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        passwordDriver.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        // Firebase Realtime Database reference to "Ambulance" table
        databaseReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/").getReference().child("Ambulance");

        Button signInButton = findViewById(R.id.button4);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        // Retrieve input from EditText fields
        String enteredPhone = ((EditText) findViewById(R.id.editTextDriverPhoneNum)).getText().toString();
        String enteredPlateNumber = ((EditText) findViewById(R.id.editTextPlateNum)).getText().toString();
        String enteredPassword = passwordDriver.getText().toString();

        // Firebase Realtime Database query
        Query query = databaseReference.orderByChild("Phone").equalTo(enteredPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean signInSuccessful = false;
                for (DataSnapshot ambulanceSnapshot : dataSnapshot.getChildren()) {
                    String plateNumber = ambulanceSnapshot.child("plateNumber").getValue(String.class);
                    String password = ambulanceSnapshot.child("Password").getValue(String.class);

                    // Perform the comparison with retrieved data
                    if (plateNumber != null && password != null && plateNumber.equals(enteredPlateNumber) && password.equals(enteredPassword)) {
                        // Sign in successful
                        signInSuccessful = true;
                        break;
                    }
                }

                if (signInSuccessful) {
                    // Sign in successful, do something
                    // For example, start a new activity
                    Toast.makeText(DriverLogin.this, "LOGGED IN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DriverLogin.this, com.example.derash2.DriverHomepage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("Phone",enteredPhone);
                    intent.putExtra("Plate",enteredPlateNumber);


                    startActivity(intent);
                    finish(); // Optional: Finish the current activity
                } else {
                    // Sign in failed, show an error message or handle the scenario
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur while retrieving data
            }
        });
    }


}
