package com.example.derash2;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class SupportFeedback extends AppCompatActivity {

    private EditText editTextFeedback;
    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_feedback);

        editTextFeedback = findViewById(R.id.editTextFeedback); // Updated ID here
        Button btnSubmit = findViewById(R.id.buttonFeedback);

        // Get a reference to the "feedback" table in your Firebase Realtime Database
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedback");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String feedbackMessage = editTextFeedback.getText().toString().trim();

        if (!feedbackMessage.isEmpty()) {
            // Generate a new unique key for the feedback entry
            String feedbackId = feedbackRef.push().getKey();

            // Store the feedback message under the generated key
            feedbackRef.child(feedbackId).child("message").setValue(feedbackMessage);

            // Clear the input field
            editTextFeedback.setText("");
        }
    }
}
