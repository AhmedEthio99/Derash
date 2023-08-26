package com.example.derash2;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.*;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.*;
import com.google.maps.model.*;

import java.util.*;

public class DriverHomepage extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private LocationManager locationManager;
    private DatabaseReference databaseReference;
    private DatabaseReference tokensReference;

    private String phone;
    private String plateNumber;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private MapView mapView;
    private GoogleMap googleMap;



    private EditText userLocationEditText;
    private Button getLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_homepage);

        Intent intent = getIntent();
        phone = intent.getStringExtra("Phone");
        plateNumber = intent.getStringExtra("Plate");

        handleNotificationData(intent);

        EditText editTextDriver = findViewById(R.id.editTextDriver);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        databaseReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/")
                .getReference()
                .child("AmbulanceLocation")
                .child(phone);

        tokensReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/")
                .getReference()
                .child("tokens");

        mapView = findViewById(R.id.mapDriverFragment);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button readyButton = findViewById(R.id.button_ready);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String fcmToken = task.getResult();
                        Log.d("FCM Token", fcmToken);

                        saveTokenToDatabase(fcmToken);
                    }
                });
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }

        Button cancelButton = findViewById(R.id.buttonCancelDriv);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDriverInfo();
            }
        });

        userLocationEditText = findViewById(R.id.userLocation);
        getLocationButton = findViewById(R.id.button_get);

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveAndDisplayLocation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {

        storeCurrentLocation();


        updateMapMarker(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // No implementation needed
    }

    @Override
    public void onProviderEnabled(String provider) {
        // No implementation needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // No implementation needed
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng initialLatLng = new LatLng(0, 0);
        googleMap.addMarker(new MarkerOptions().position(initialLatLng).title("Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 12));
    }

    private void storeCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();

            HashMap<String, Object> driverInfo = new HashMap<>();
            driverInfo.put("phone", phone);
            driverInfo.put("plateNumber", plateNumber);
            driverInfo.put("latitude", latitude);
            driverInfo.put("longitude", longitude);

            DatabaseReference ambulanceReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/")
                    .getReference()
                    .child("Ambulance");
            Query query = ambulanceReference.orderByChild("Phone").equalTo(phone);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ambulanceSnapshot : dataSnapshot.getChildren()) {
                        String username = ambulanceSnapshot.child("Username").getValue(String.class);
                        if (username != null) {
                            driverInfo.put("username", username);
                        }
                        String type = ambulanceSnapshot.child("type").getValue(String.class);
                        if (type != null) {
                            driverInfo.put("type", type);
                        }
                    }
                    databaseReference.updateChildren(driverInfo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Database Error", databaseError.getMessage());
                }
            });
        }
    }

    private void deleteDriverInfo() {
        databaseReference.removeValue();
        tokensReference.child(phone).removeValue();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(phone);

        Intent intent = new Intent(DriverHomepage.this, DriverLogin.class);
        startActivity(intent);
        finish();
    }

    private void updateMapMarker(Location location) {
        if (googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");

            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        }
    }

    private void saveTokenToDatabase(String token) {
        DatabaseReference userTokenReference = tokensReference.child(phone);
        userTokenReference.child("token").setValue(token);
        userTokenReference.child("driverphone").setValue(phone);
    }

    private void retrieveAndDisplayLocation() {
        String userTokens = userLocationEditText.getText().toString().trim();
        if (userTokens.isEmpty()) {
            Toast.makeText(DriverHomepage.this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userLocationReference = FirebaseDatabase.getInstance("https://derash-d0eb5-default-rtdb.firebaseio.com/")
                .getReference()
                .child("tokens")
                .child(userTokens);

        userLocationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String latitudeStr = dataSnapshot.child("latitude").getValue(String.class);
                    String longitudeStr = dataSnapshot.child("longitude").getValue(String.class);
                    if (latitudeStr != null && longitudeStr != null) {
                        try {
                            double latitude = Double.parseDouble(latitudeStr);
                            double longitude = Double.parseDouble(longitudeStr);
                            LatLng userLatLng = new LatLng(latitude, longitude);

                            // Retrieve current location
                            if (ActivityCompat.checkSelfPermission(DriverHomepage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(DriverHomepage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (currentLocation != null) {
                                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                                // Add markers for current location and user location
                                googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                                googleMap.addMarker(new MarkerOptions().position(userLatLng).title("User Location"));

                                // Move camera to show both markers
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(currentLatLng);
                                builder.include(userLatLng);
                                LatLngBounds bounds = builder.build();
                                int padding = 100; // Padding in pixels
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                googleMap.moveCamera(cameraUpdate);

                                // Add route polyline
                                drawRoute(currentLatLng, userLatLng);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(DriverHomepage.this, "Invalid location data.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DriverHomepage.this, "Invalid location data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DriverHomepage.this, "User location not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database Error", databaseError.getMessage());
            }
        });
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        // Create a new instance of Google Directions API client
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyDqy2VbwJW0iIAPFxswgYi6X7V3dFTUwNg")
                .build();

        // Make a directions API request
        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .mode(TravelMode.DRIVING);

        request.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    List<LatLng> routePoints = new ArrayList<>();

                    // Extract polyline points from the route
                    if (route.legs != null && route.legs.length > 0) {
                        DirectionsLeg leg = route.legs[0];
                        if (leg.steps != null) {
                            for (DirectionsStep step : leg.steps) {
                                EncodedPolyline encodedPolyline = step.polyline;
                                List<com.google.maps.model.LatLng> decodedPath = encodedPolyline.decodePath();
                                for (com.google.maps.model.LatLng decodedLatLng : decodedPath) {
                                    LatLng latLng = new LatLng(decodedLatLng.lat, decodedLatLng.lng);
                                    routePoints.add(latLng);
                                }
                            }
                        }
                    }

                    // Draw polyline on the map
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(routePoints)
                            .color(Color.BLUE)
                            .width(8f);
                    googleMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("Directions API", "Error: " + e.getMessage());
            }
        });
    }



    private void handleNotificationData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String title = bundle.getString("title");
            String message = bundle.getString("message");

            if (title != null && message != null) {
                displayNotificationDialog(title, message);
            }
        }
    }

    private void displayNotificationDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomepage.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_itemdriver, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //remove the back button
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.CompleteTrip) {
            Toast.makeText(this, "Complete Trip", Toast.LENGTH_SHORT).show();
            // Perform the activity you want to associate with this menu option
            Intent intent = new Intent(DriverHomepage.this, CompleteTrip.class);
            startActivity(intent);
        }

        if (item_id == R.id.logoutdriver) {
            // Perform the activity you want to associate with this menu option
            Intent intent = new Intent(DriverHomepage.this, CompleteTrip.class);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
                }
            } else {
                Toast.makeText(DriverHomepage.this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
