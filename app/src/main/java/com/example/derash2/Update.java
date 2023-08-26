package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class Update extends AppCompatActivity {
     TextView textView2;
    private EditText editTextPhonenumUpdate;
    private EditText password1Update;
    private EditText editTextFirstNameUpdate;
    private EditText editTextMidNameUpdate;
    private EditText editTextLastNameUpdate;
    private Button buttonUpdate;
    private DatabaseReference databaseReference;
    private String phoneValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        databaseReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/").getReference();

        editTextPhonenumUpdate = findViewById(R.id.editTextPhonenumUpdate);
        password1Update = findViewById(R.id.password1Update);
        editTextFirstNameUpdate = findViewById(R.id.editTextFirstNameUpdate);
        editTextMidNameUpdate = findViewById(R.id.editTextMidNameUpdate);
        editTextLastNameUpdate = findViewById(R.id.editTextLastNameUpdate);
        textView2 = (TextView) findViewById(R.id.textView2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneValue = extras.getString("phoneValue");
        }

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Update.this, com.example.derash2.UpdateCompany.class);
                intent.putExtra("phoneValue",phoneValue);
                startActivity(intent);

            }
        });

        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void updateUserInfo() {
        String newPhoneNumber = editTextPhonenumUpdate.getText().toString();
        String password = password1Update.getText().toString();
        String firstName = editTextFirstNameUpdate.getText().toString();
        String middleName = editTextMidNameUpdate.getText().toString();
        String lastName = editTextLastNameUpdate.getText().toString();

        if (TextUtils.isEmpty(newPhoneNumber)) {
            editTextPhonenumUpdate.setError("Please enter phone number");
            editTextPhonenumUpdate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            password1Update.setError("Please enter password");
            password1Update.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            editTextFirstNameUpdate.setError("Please enter first name");
            editTextFirstNameUpdate.requestFocus();
            return;
        }

        // Update the user's information in the Firebase database
        DatabaseReference userRef = databaseReference.child("users").child(phoneValue);

        // Update the phone number as the new key
        if (!phoneValue.equals(newPhoneNumber)) {
            // Create a new node with the new phone number and copy the data
            DatabaseReference newPhoneRef = databaseReference.child("users").child(newPhoneNumber);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    newPhoneRef.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                // Delete the old node with the old phone number
                                userRef.removeValue();
                                // Update the phoneValue to the new phone number
                                phoneValue = newPhoneNumber;
                                Toast.makeText(Update.this, "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Update.this, "Failed to update phone number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Update.this, "Failed to update phone number", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Update other user information
        userRef.child("Password").setValue(password);
        userRef.child("First Name").setValue(firstName);
        userRef.child("Middle Name").setValue(middleName);
        userRef.child("Last Name").setValue(lastName);

        Toast.makeText(this, "User information updated successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Update.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



}