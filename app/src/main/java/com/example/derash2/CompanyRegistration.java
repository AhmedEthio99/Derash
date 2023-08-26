package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class CompanyRegistration extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://derash-d0eb5-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        final EditText editTextFullName = findViewById(R.id.editTextFullName);
        final EditText editTextCompanyName = findViewById(R.id.editTextCompanyName);
        final EditText editTextCompanyRegisterNum = findViewById(R.id.editTextCompanyRegisterNum);
        final EditText editTextCompanyPhoneNum = findViewById(R.id.editTextCompanyPhoneNum);
        final EditText editTextCompanyAddress = findViewById(R.id.editTextCompanyAddress);
        final EditText editTextCompanyEmail1 = findViewById(R.id.editTextCompanyEmail1);
        final EditText compassword = findViewById(R.id.compassword);
        final EditText compassword2 = findViewById(R.id.compassword2);
        final Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullNameTxt = editTextFullName.getText().toString();
                final String CompanyNameTxt = editTextCompanyName.getText().toString();
                final String CompanyRegisterNumTxt = editTextCompanyRegisterNum.getText().toString();
                final String CompanyPhoneNumTxt = editTextCompanyPhoneNum.getText().toString();
                final String CompanyAddressTxt = editTextCompanyAddress.getText().toString();
                final String CompanyEmail1Txt = editTextCompanyEmail1.getText().toString();
                final String compasswordTxt = compassword.getText().toString();
                final String compassword2Txt = compassword2.getText().toString();
                if (fullNameTxt.isEmpty() || CompanyNameTxt.isEmpty() || CompanyRegisterNumTxt.isEmpty() || CompanyAddressTxt.isEmpty() || compasswordTxt.isEmpty() || compassword2Txt.isEmpty()){
                    Toast.makeText(CompanyRegistration.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
                else if (!compasswordTxt.equals(compassword2Txt)){
                    Toast.makeText(CompanyRegistration.this, "Password Given Is Not The Same", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(CompanyEmail1Txt)){
                    editTextCompanyEmail1.setError("Please enter email");
                    editTextCompanyEmail1.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(CompanyEmail1Txt).matches()){
                    Toast.makeText(CompanyRegistration.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();
                    editTextCompanyEmail1.requestFocus();
                }


                else if (!CompanyAddressTxt.matches("[a-zA-Z]+")){
                    Toast.makeText(CompanyRegistration.this, "Please Enter A Valid Address", Toast.LENGTH_SHORT).show();

                }
                else if (compasswordTxt.length()<=5){
                    Toast.makeText(CompanyRegistration.this, "Minimum 6 Character Required", Toast.LENGTH_SHORT).show();

                }
                else {
                    databaseReference.child("company").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(CompanyPhoneNumTxt)) {

                                Toast.makeText(CompanyRegistration.this, "Company Email Is Already Registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("company").child(CompanyPhoneNumTxt).child("Full Name").setValue(fullNameTxt);
                                databaseReference.child("company").child(CompanyPhoneNumTxt).child("Company Name").setValue(CompanyNameTxt);
                                databaseReference.child("company").child(CompanyPhoneNumTxt).child("Company Registration Number").setValue(CompanyRegisterNumTxt);
                                databaseReference.child("company").child(CompanyPhoneNumTxt).child("Company Address").setValue(CompanyAddressTxt);
                                databaseReference.child("company").child(CompanyPhoneNumTxt).child("Company Email").setValue(CompanyEmail1Txt);
                                databaseReference.child("company").child(CompanyPhoneNumTxt).child("Password").setValue(compasswordTxt);
                                Toast.makeText(CompanyRegistration.this, "Successfully Registered",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CompanyRegistration.this, Terms_Conditions.class);
                                startActivity(intent);

                                startActivity(intent);

                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });
    }
}