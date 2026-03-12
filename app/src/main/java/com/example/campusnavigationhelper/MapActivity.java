package com.example.campusnavigationhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String locationName;
    private String locationDescription;
    private int locationId;

    private TextView tvLocationName, tvDescription, tvCoordinates;
    private Button btnFavorite, btnBack;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int userId;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data from intent
        locationId = getIntent().getIntExtra("location_id", -1);
        locationName = getIntent().getStringExtra("name");
        locationDescription = getIntent().getStringExtra("description");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        // Initialize database and preferences
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("CampusPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        // Check if Google Play Services is available
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            // Google Play Services is available - show Map layout
            setContentView(R.layout.activity_map);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        } else {
            // Google Play Services not available - show fallback layout
            setContentView(R.layout.activity_map_fallback);
            initFallbackViews();
            
            Toast.makeText(this,
                "Google Maps not available on this device.\nShowing location details instead.",
                Toast.LENGTH_LONG).show();
        }
    }

    private void initFallbackViews() {
        tvLocationName = findViewById(R.id.tvLocationName);
        tvDescription = findViewById(R.id.tvDescription);
        tvCoordinates = findViewById(R.id.tvCoordinates);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);

        // Set location details
        tvLocationName.setText(locationName);
        tvDescription.setText(locationDescription);
        tvCoordinates.setText(String.format("📍 %.6f, %.6f", latitude, longitude));

        // Check if this location is favorite
        if (userId != -1) {
            isFavorite = dbHelper.isFavorite(userId, locationId);
            updateFavoriteButton();
        }

        // Favorite button click listener
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1) {
                    Toast.makeText(MapActivity.this, "Please login to add favorites", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isFavorite) {
                    dbHelper.removeFavorite(userId, locationId);
                    isFavorite = false;
                    Toast.makeText(MapActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addFavorite(userId, locationId);
                    isFavorite = true;
                    Toast.makeText(MapActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
                updateFavoriteButton();
            }
        });

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateFavoriteButton() {
        if (btnFavorite == null) return;
        if (isFavorite) {
            btnFavorite.setText("❤️ Remove from Favorites");
            btnFavorite.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            btnFavorite.setText("🤍 Add to Favorites");
            btnFavorite.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        
        // Add a marker in the campus location and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(locationName).snippet(locationDescription));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
        
        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
