package com.example.campusnavigationhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;

public class LocationListActivity extends AppCompatActivity {

    private ListView lvLocations;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        lvLocations = findViewById(R.id.lvLocations);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("CampusPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        loadLocations();

        lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);

                // Get all location details
                int locationId = Integer.parseInt(item.get("id"));
                String name = item.get("name");
                String description = item.get("description");
                double latitude = Double.parseDouble(item.get("latitude"));
                double longitude = Double.parseDouble(item.get("longitude"));

                // Open Map Activity with all data
                Intent intent = new Intent(LocationListActivity.this, MapActivity.class);
                intent.putExtra("location_id", locationId);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    private void loadLocations() {
        ArrayList<Location> locations = dbHelper.getAllLocations();

        // Create list of maps for SimpleAdapter
        List<Map<String, String>> data = new ArrayList<>();

        for (Location loc : locations) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(loc.getId()));
            map.put("name", loc.getName());
            map.put("description", loc.getDescription());
            map.put("latitude", String.valueOf(loc.getLatitude()));
            map.put("longitude", String.valueOf(loc.getLongitude()));
            data.add(map);
        }

        // Simple adapter for ListView - shows name and description
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "description"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        lvLocations.setAdapter(adapter);
    }
}