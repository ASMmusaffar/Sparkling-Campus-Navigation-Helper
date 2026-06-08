package com.example.campusnavigationhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {

    private ListView lvFavorites;        // Changed variable name
    private View tvEmptyMessage;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites); // Using new layout

        // Initialize views with correct IDs from new layout
        lvFavorites = findViewById(R.id.lvFavorites);        // Fixed: now using lvFavorites
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("CampusPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        loadFavorites();

        // Set click listener to open MapActivity
        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);

                // Get all location details
                int locationId = Integer.parseInt(item.get("id"));
                String name = item.get("name");
                String description = item.get("description");
                double latitude = Double.parseDouble(item.get("latitude"));
                double longitude = Double.parseDouble(item.get("longitude"));

                // Open Map Activity with all data
                Intent intent = new Intent(FavoritesActivity.this, MapActivity.class);
                intent.putExtra("location_id", locationId);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    private void loadFavorites() {
        ArrayList<Location> favorites = dbHelper.getUserFavorites(userId);

        if (favorites.isEmpty()) {
            // Show empty message, hide ListView
            lvFavorites.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            // Show ListView, hide empty message
            lvFavorites.setVisibility(View.VISIBLE);
            tvEmptyMessage.setVisibility(View.GONE);

            List<Map<String, String>> data = new ArrayList<>();

            for (Location loc : favorites) {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(loc.getId()));
                map.put("name", loc.getName());
                map.put("description", loc.getDescription());
                map.put("latitude", String.valueOf(loc.getLatitude()));
                map.put("longitude", String.valueOf(loc.getLongitude()));
                data.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    data,
                    android.R.layout.simple_list_item_2,
                    new String[]{"name", "description"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );

            lvFavorites.setAdapter(adapter);
        }
    }
}