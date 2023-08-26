package com.example.derash2;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://derash-d0eb5-default-rtdb.firebaseio.com/");

    private Button button;
    TextView textView;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginbutton = findViewById(R.id.loginbutton);
        textView=(TextView) findViewById(R.id.txtView);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Insert Data", Toast.LENGTH_SHORT).show();
                }

                else if (!phoneTxt.matches("^09\\d{8}$")){
                    Toast.makeText(LoginActivity.this, "Please Enter A Valid Number", Toast.LENGTH_SHORT).show();

                }


                else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneTxt)){
                                DataSnapshot userSnapshot = snapshot.child(phoneTxt);
                                String storedPassword = userSnapshot.child("Password").getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(passwordTxt)){
                                    Toast.makeText(LoginActivity.this,"Logged in successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, com.example.derash2.STORING.class);
                                    intent.putExtra("phoneValue", phoneTxt);
                                    finish();
                                    startActivity(intent);

                                }


                                else {
                                    Toast.makeText(LoginActivity.this, "Wrong Entry", Toast.LENGTH_SHORT).show();
                                }



                            } else {
                                databaseReference.child("company").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(phoneTxt)){
                                           DataSnapshot userSnapshot = snapshot.child(phoneTxt);
                                            String storedPassword = userSnapshot.child("Password").getValue(String.class);
                                            if (storedPassword != null && storedPassword.equals(passwordTxt)){
                                                Toast.makeText(LoginActivity.this,"Logged in successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, com.example.derash2.STORING.class);
                                                intent.putExtra("phoneValue", phoneTxt);
                                                startActivity(intent);

                                                finish();
                                            }   else {
                                                Toast.makeText(LoginActivity.this, "Wrong Entry", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, com.example.derash2.SignUpChoice.class);
                startActivity(intent);

            }
        });

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDriverPage();

            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=password.getSelectionEnd();
                        if (passwordVisible){
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;

                        }else {

                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24,0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });


    }

    public void openDriverPage(){

        Intent intent = new Intent(this, com.example.derash2.DriverLogin.class);
        startActivity(intent);
    }



}