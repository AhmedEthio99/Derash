package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class UpdateCompany extends AppCompatActivity {
    private EditText editTextFullNameUpdate;
    private EditText editTextCompanyRegisterNumUpdate;
    private EditText editTextCompanyPhoneNumUpdate;
    private EditText editTextCompanyPasswordUpdate;
    private EditText editTextCompanyNameUpdate;
    private EditText editTextCompanyAddressUpdate;
    private Button buttonUpdateCompany;
    private DatabaseReference databaseReference;
    private String phoneValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_company);

        databaseReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/").getReference();

        editTextFullNameUpdate = findViewById(R.id.editTextFullNameUpdate);
        editTextCompanyPhoneNumUpdate = findViewById(R.id.editTextCompanyPhoneNumUpdate);
        editTextCompanyNameUpdate = findViewById(R.id.editTextCompanyNameUpdate);
        editTextCompanyAddressUpdate = findViewById(R.id.editTextCompanyAddressUpdate);
        editTextCompanyRegisterNumUpdate = findViewById(R.id.editTextCompanyRegisterNumUpdate);
        editTextCompanyPasswordUpdate = findViewById(R.id.editTextCompanyPasswordUpdate);
        Bundle extras = getIntent().getExtras();
        phoneValue = getIntent().getStringExtra("phoneValue");

        buttonUpdateCompany = findViewById(R.id.buttonCompUpdate);
        buttonUpdateCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCompanyInfo();
            }
        });
    }

    private void updateCompanyInfo() {
        String fullName = editTextFullNameUpdate.getText().toString();
        String companyPhone = editTextCompanyPhoneNumUpdate.getText().toString();
        String companyName = editTextCompanyNameUpdate.getText().toString();
        String companyAddress = editTextCompanyAddressUpdate.getText().toString();
        String companyRegNum = editTextCompanyRegisterNumUpdate.getText().toString();
        String companyPassword = editTextCompanyPasswordUpdate.getText().toString();


        if (TextUtils.isEmpty(companyPhone)) {
            editTextCompanyPhoneNumUpdate.setError("Please enter company phone number");
            editTextCompanyPhoneNumUpdate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(companyName)) {
            editTextCompanyNameUpdate.setError("Please enter company name");
            editTextCompanyNameUpdate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(companyAddress)) {
            editTextCompanyAddressUpdate.setError("Please enter company address");
            editTextCompanyAddressUpdate.requestFocus();
            return;
        }

        // Update the company information in the Firebase database
        if (phoneValue != null) {
            DatabaseReference companyRef = databaseReference.child("company").child(phoneValue);

            // Update the company phone number as the new key
            if (!phoneValue.equals(companyPhone)) {
                // Create a new node with the new company phone number and copy the data
                DatabaseReference newPhoneRef = databaseReference.child("company").child(companyPhone);
                companyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        newPhoneRef.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    // Delete the old node with the old company phone number
                                    companyRef.removeValue();
                                    // Update the phoneValue to the new company phone number
                                    phoneValue = companyPhone;
                                    Toast.makeText(UpdateCompany.this, "Company phone number updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateCompany.this, "Failed to update company phone number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UpdateCompany.this, "Failed to update company phone number", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Update other company information
            companyRef.child("Company Name").setValue(companyName);
            companyRef.child("Company Address").setValue(companyAddress);
            companyRef.child("Full Name").setValue(fullName);
            companyRef.child("Password").setValue(companyPassword);
            companyRef.child("Company Registration Number").setValue(companyRegNum);

            Toast.makeText(this, "Company information updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateCompany.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid phone value", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}