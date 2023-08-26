package com.example.derash2;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class TripHistory extends AppCompatActivity {

    private TextView hospitalTextView;
    private TextView dateTextView;
    private TextView typeTextView;
    private TextView driverTextView;

    private DatabaseReference tripCompleteRef;
    private String phoneValue; // Added phoneValue variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        hospitalTextView = findViewById(R.id.hospitalret);
        dateTextView = findViewById(R.id.dateret);
        typeTextView = findViewById(R.id.typeret);
        driverTextView = findViewById(R.id.driverret);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneValue = extras.getString("phoneValue");
        }

        Button historyButton = findViewById(R.id.buttonHistory);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveTripHistory();
            }
        });
    }

    private void retrieveTripHistory() {
        tripCompleteRef = FirebaseDatabase.getInstance().getReference().child("TripComplete");
        Query query = tripCompleteRef.orderByChild("phone Number").equalTo(phoneValue);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String hospital = snapshot.child("hospital").getValue(String.class);
                        String arrivalDate = snapshot.child("arrival Date").getValue(String.class);
                        String ambulanceType = snapshot.child("Ambulance Type").getValue(String.class);
                        String driverName = snapshot.child("driver Name").getValue(String.class);

                        // Set the retrieved values to the TextViews
                        hospitalTextView.setText(hospital);
                        dateTextView.setText(arrivalDate);
                        typeTextView.setText(ambulanceType);
                        driverTextView.setText(driverName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}