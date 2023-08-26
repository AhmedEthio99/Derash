//LocationManager
package com.example.derash2;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.*;

public class STORING extends AppCompatActivity implements OnMapReadyCallback {


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private DatabaseReference databaseReference;
    private EditText editTextTextPersonName4;
    private Button button_store;
    private String phoneValue;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double currentLatitude;
    private double currentLongitude;
    private GoogleMap googleMap;
    private RadioGroup typesRadioGroup;
    private String selectedType;
    private Button button_delete;
    private FirebaseMessaging firebaseMessaging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storing);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/").getReference();

        editTextTextPersonName4 = findViewById(R.id.editTextTextPersonName4);
        editTextTextPersonName4.setFocusable(false);
        editTextTextPersonName4.setFocusableInTouchMode(false);
        editTextTextPersonName4.setClickable(false);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            phoneValue = extras.getString("phoneValue");
            editTextTextPersonName4.setText(phoneValue);
        }




        button_store = findViewById(R.id.button_store);
        button_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeValues();
            }
        });

        typesRadioGroup = findViewById(R.id.TypesRadioGroup);
        typesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedType = radioButton.getText().toString();
            }
        });

        // Set up location tracking
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Set up location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Store the current latitude and longitude in Firebase database
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                // Update the location in Firebase database
                databaseReference.child("users").child(phoneValue).child("latitude").setValue(currentLatitude);
                databaseReference.child("users").child(phoneValue).child("longitude").setValue(currentLongitude);
                LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);

                // Update the map marker
                updateMapMarker(new LatLng(currentLatitude, currentLongitude));

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        // Check for permission to access fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    locationListener);
        } else {
            // Request permission to access fine location
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null){
                    String fcmToken = task.getResult();
                    Log.d("FCM Token",fcmToken);
                }
            });


        // Set up the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        button_delete = findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }
        });
    }


    private void deleteRequest() {
        DatabaseReference requestRef = databaseReference.child("request").child(phoneValue);
        requestRef.removeValue();
        Toast.makeText(STORING.this, "Request Has Been Canceled", Toast.LENGTH_SHORT).show();
    }

    private void updateMapMarker(LatLng latLng) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    private void storeValues() {
        // Store phone number and company name in Firebase database
        databaseReference.child("users").child(phoneValue).child("phone number").setValue(phoneValue);
        databaseReference.child("company").child(phoneValue).child("company phone number").setValue(phoneValue);

        databaseReference.child("users").child(phoneValue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the specific value "name" from the HashMap
                Log.d("STORING", "onDataChange" + dataSnapshot);
                String value = dataSnapshot.child("First Name").getValue(String.class);
                String phoneNumber = dataSnapshot.child("phone number").getValue(String.class);

                // Add the retrieved value to the "request" table
                DatabaseReference requestRef = databaseReference.child("request").child(phoneNumber);
                requestRef.child("First Name").setValue(value);
                requestRef.child("phone number").setValue(phoneNumber);
                requestRef.child("type").setValue(selectedType);
                requestRef.child("latitude").setValue(currentLatitude);
                requestRef.child("longitude").setValue(currentLongitude);




                Toast.makeText(STORING.this, "REQUEST HAS BEEN ACCEPTED, DRIVER WILL BE CONNECTED SHORTLY", Toast.LENGTH_SHORT).show();
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential error
            }
        });

        databaseReference.child("company").child(phoneValue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the specific value "name" from the HashMap
                Log.d("STORING", "onDataChange" + dataSnapshot);
                String value = dataSnapshot.child("Company Name").getValue(String.class);
                String CophoneNumber = dataSnapshot.child("company phone number").getValue(String.class);

                // Add the retrieved value to the "request" table
                DatabaseReference requestRef = databaseReference.child("request").child(CophoneNumber);
                requestRef.child("Company Name").setValue(value);
                requestRef.child("phone number").setValue(CophoneNumber);
                requestRef.child("type").setValue(selectedType);
                requestRef.child("latitude").setValue(currentLatitude);
                requestRef.child("longitude").setValue(currentLongitude);



                Toast.makeText(STORING.this, "REQUEST HAS BEEN ACCEPTED, DRIVER WILL BE CONNECTED SHORTLY", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential error
            }
        });
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Request location updates
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                        locationListener);
            } else {
                // Permission denied, do nothing
            }
        }
    }

    // Function to get the complete address string from latitude and longitude values
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Log.d("STORING", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("STORING", "Can't get Address!");
        }
        return strAdd;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //remove the back button
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.Ambtypes) {
            Toast.makeText(this, "Ambulance Descriptions", Toast.LENGTH_SHORT).show();
            // Perform the activity you want to associate with this menu option
            Intent intent = new Intent(STORING.this, Description.class);
            startActivity(intent);
        } else if (item_id == R.id.update) {
            // Perform the activity you want to associate with this menu option
            Intent intent = new Intent(STORING.this, com.example.derash2.Update.class);

            intent.putExtra("phoneValue", phoneValue);
            startActivity(intent);
        } else if (item_id == R.id.delete) {
            Intent intent = new Intent(STORING.this, com.example.derash2.DeleteAccount.class);

            intent.putExtra("phoneValue", phoneValue);
            startActivity(intent);
        } else if (item_id == R.id.Trip) {
            Intent intent = new Intent(STORING.this, com.example.derash2.TripHistory.class);
            intent.putExtra("phoneValue", phoneValue);

            startActivity(intent);
        }
        else if (item_id == R.id.supp_feed) {
            Intent intent = new Intent(STORING.this, com.example.derash2.SupportFeedback.class);
            startActivity(intent);
        } else if (item_id == R.id.logout) {
            Intent intent = new Intent(STORING.this, com.example.derash2.Logout.class);
            intent.putExtra("phoneValue", phoneValue);

            startActivity(intent);        }
        return true;
    }


}
