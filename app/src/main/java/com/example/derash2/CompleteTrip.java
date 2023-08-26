package com.example.derash2;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class CompleteTrip extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_trip);

        // Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button doneButton = findViewById(R.id.button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                EditText hospitalEditText = findViewById(R.id.hospital);
                EditText driverNameEditText = findViewById(R.id.drivername);
                EditText usernameEditText = findViewById(R.id.username);
                EditText phoneNumberEditText = findViewById(R.id.phoneNumber);
                EditText arrivalDateEditText = findViewById(R.id.editTextDOC);
                EditText platenumberEditText = findViewById(R.id.platenumber);
                EditText typeEditText = findViewById(R.id.type);

                // Get the values from EditTexts
                String hospital = hospitalEditText.getText().toString();
                String driverName = driverNameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String arrivalDate = arrivalDateEditText.getText().toString();
                String platenumber = platenumberEditText.getText().toString();
                String type = typeEditText.getText().toString();

                if (hospital.isEmpty() || driverName.isEmpty() || username.isEmpty() ||
                        phoneNumber.isEmpty() || arrivalDate.isEmpty() || platenumber.isEmpty() || type.isEmpty()) {
                    Toast.makeText(CompleteTrip.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new entry in the Firebase Realtime Database
                DatabaseReference tripCompleteRef = mDatabase.child("TripComplete").push();
                tripCompleteRef.child("hospital").setValue(hospital);
                tripCompleteRef.child("driver Name").setValue(driverName);
                tripCompleteRef.child("username").setValue(username);
                tripCompleteRef.child("phone Number").setValue(phoneNumber);
                tripCompleteRef.child("arrival Date").setValue(arrivalDate);
                tripCompleteRef.child("plate Number").setValue(platenumber);
                tripCompleteRef.child("Ambulance Type").setValue(type);

                Toast.makeText(CompleteTrip.this, "THANK YOU FOR YOUR SERVICE", Toast.LENGTH_SHORT).show();

                // Clear EditText fields after storing the data
                hospitalEditText.setText("");
                driverNameEditText.setText("");
                usernameEditText.setText("");
                phoneNumberEditText.setText("");
                arrivalDateEditText.setText("");
                platenumberEditText.setText("");
                typeEditText.setText("");
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}

