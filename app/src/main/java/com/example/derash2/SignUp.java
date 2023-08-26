package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;


public class SignUp extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://derash-d0eb5-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
            final EditText editTextFirstName = findViewById(R.id.editTextFirstName);
            final EditText editTextMidName = findViewById(R.id.editTextMidName);
            final EditText editTextLastName = findViewById(R.id.editTextLastName);
            final EditText editTextEmail1 = findViewById(R.id.editTextEmail1);
            final EditText editTextPhonenum = findViewById(R.id.editTextPhonenum);
            final EditText password1 = findViewById(R.id.password1);
            final EditText password2 = findViewById(R.id.password2);
            final Button button3 = findViewById(R.id.button3);

            button3.setOnClickListener(view -> {
                final String firstNameTxt = editTextFirstName.getText().toString();
                final String middleNameTxt = editTextMidName.getText().toString();
                final String lastNameTxt = editTextLastName.getText().toString();
                final String phoneTxt = editTextPhonenum.getText().toString();
                final String email1Txt = editTextEmail1.getText().toString();
                final String password1Txt = password1.getText().toString();
                final String password2Txt = password2.getText().toString();


                if (firstNameTxt.isEmpty() || middleNameTxt.isEmpty() || lastNameTxt.isEmpty() || phoneTxt.isEmpty() || password1Txt.isEmpty() || password2Txt.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password1Txt.equals(password2Txt)) {
                    Toast.makeText(SignUp.this, "Password given is not the same", Toast.LENGTH_SHORT).show();
                } else if (!firstNameTxt.matches("[a-zA-Z]+")) {
                    Toast.makeText(SignUp.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(email1Txt)) {
                    editTextEmail1.setError("Please enter email");
                    editTextEmail1.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email1Txt).matches()) {
                    Toast.makeText(SignUp.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();
                    editTextEmail1.requestFocus();
                } else if (!middleNameTxt.matches("[a-zA-Z]+")) {
                    Toast.makeText(SignUp.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();

                } else if (!lastNameTxt.matches("^[a-zA-Z]+$")) {
                    Toast.makeText(SignUp.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();

                } else if (!phoneTxt.matches("^09\\d{8}$")) {
                    Toast.makeText(SignUp.this, "Please enter a valid Number", Toast.LENGTH_SHORT).show();

                } else if (password1Txt.length() <= 5) {
                    Toast.makeText(SignUp.this, "Minimum 6 Character Required", Toast.LENGTH_SHORT).show();

                } else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(phoneTxt)) {

                                Toast.makeText(SignUp.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                            } else {


                                databaseReference.child("users").child(phoneTxt).child("Email").setValue(email1Txt);
                                databaseReference.child("users").child(phoneTxt).child("First Name").setValue(firstNameTxt);
                                databaseReference.child("users").child(phoneTxt).child("Middle Name").setValue(middleNameTxt);
                                databaseReference.child("users").child(phoneTxt).child("Last Name").setValue(lastNameTxt);
                                databaseReference.child("users").child(phoneTxt).child("Password").setValue(password1Txt);
                                Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, com.example.derash2.Terms_Conditions.class);
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            });

        }

    }

