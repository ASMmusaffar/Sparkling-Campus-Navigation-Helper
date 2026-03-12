package com.example.campusnavigationhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusDB.db";
    private static final int DATABASE_VERSION = 1;  // Keep at 1 since first run

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Locations table with coordinates
    private static final String TABLE_LOCATIONS = "locations";
    private static final String COLUMN_LOCATION_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    // Favorites table
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_FAV_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_LOCATION_ID_FK = "location_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create locations table with coordinates
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + COLUMN_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL)";
        db.execSQL(CREATE_LOCATIONS_TABLE);

        // Create favorites table with foreign keys
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID_FK + " INTEGER,"
                + COLUMN_LOCATION_ID_FK + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_LOCATION_ID_FK + ") REFERENCES " + TABLE_LOCATIONS + "(" + COLUMN_LOCATION_ID + "))";
        db.execSQL(CREATE_FAVORITES_TABLE);

        // Insert all campus locations with coordinates
        insertAllLocations(db);
    }

    private void insertAllLocations(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Array of locations [latitude, longitude, name, description]
        Object[][] locations = {
                {8.353031262783384, 80.50245591091979, "Faculty Building", "Main faculty building with lecture halls"},
                {8.352624127387903, 80.5026982670269, "ICT Laboratory", "Computer labs and IT facilities"},
                {8.352716544662181, 80.50312743929992, "Applied Canteen", "Student canteen serving meals"},
                {8.352716544662181, 80.50312743929992, "Applied Vela", "Food court with multiple options"},
                {8.353240967606363, 80.5029757790587, "Applied Science Library", "Library for applied sciences"},
                {8.35281333223767, 80.50216494688374, "Health Promotion Department", "Health services and wellness center"},
                {8.353734887236309, 80.5018297782165, "Department of Physical Science", "Physics and chemistry departments"},
                {8.353789136610915, 80.50233102438996, "Administration Building", "University administrative offices"},
                {8.353543887411732, 80.5031139630727, "Water Tank", "Campus water supply"},
                {8.35435393067063, 80.50249971056093, "Microbiology Laboratory", "Microbiology research labs"},
                {8.354586971265366, 80.50262868568015, "Mahasena Hostel", "Male student hostel"},
                {8.354311219255113, 80.50366583461236, "Technology Faculty Milk Bar", "Snack bar at Technology Faculty"},
                {8.354791696577037, 80.50247183305278, "Dutugamunu Boy's Canteen", "Canteen for male students"},
                {8.354835685957084, 80.50339718252471, "Viharamaha Devi Hostel", "Female student hostel"},
                {8.354067607010174, 80.5037305646384, "Faculty Canteen & Studying Hall", "Canteen with study area"},
                {8.3620056880466, 80.50258669715741, "Administration Building", "Main admin office"},
                {8.362125741473319, 80.5026641892159, "Main Library", "Central university library"},
                {8.362175192948898, 80.50400126705377, "Career Guidance Unit", "Career counseling services"},
                {8.36263048337684, 80.50356634877676, "Milk Bar", "Refreshment kiosk"},
                {8.362611469123744, 80.5036682324845, "Department of Business Information Technology", "BIT department"},
                {8.365287528987544, 80.50407027557756, "Medical Center", "University health clinic"},
                {8.365927740067484, 80.5060594673889, "Social Science & Humanities", "Social sciences faculty"},
                {8.366429577338321, 80.50302383816333, "Swimming Pool", "University swimming pool"},
                {8.367426317665903, 80.50762726187332, "Playground", "Sports ground"},
                {8.358625972158674, 80.50493373925163, "Faculty of Technology Stage2", "Technology faculty building"}
        };

        // Insert each location
        for (Object[] loc : locations) {
            values.clear();
            values.put(COLUMN_NAME, (String) loc[2]);
            values.put(COLUMN_DESCRIPTION, (String) loc[3]);
            values.put(COLUMN_LATITUDE, (Double) loc[0]);
            values.put(COLUMN_LONGITUDE, (Double) loc[1]);

            db.insert(TABLE_LOCATIONS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // USER METHODS
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String hashedPassword = String.valueOf(password.hashCode());

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = String.valueOf(password.hashCode());

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ? AND " +
                COLUMN_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username, hashedPassword});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    // LOCATION METHODS with coordinates
    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_LOCATIONS;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOCATION_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));

            Location location = new Location(id, name, description, latitude, longitude);
            locations.add(location);
        }

        cursor.close();
        db.close();
        return locations;
    }

    // FAVORITE METHODS
    public void addFavorite(int userId, int locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_LOCATION_ID_FK, locationId);
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public void removeFavorite(int userId, int locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES,
                COLUMN_USER_ID_FK + " = ? AND " + COLUMN_LOCATION_ID_FK + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(locationId)});
        db.close();
    }

    public boolean isFavorite(int userId, int locationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FAVORITES +
                " WHERE " + COLUMN_USER_ID_FK + " = ? AND " +
                COLUMN_LOCATION_ID_FK + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(locationId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public ArrayList<Location> getUserFavorites(int userId) {
        ArrayList<Location> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT l.* FROM " + TABLE_LOCATIONS + " l " +
                "INNER JOIN " + TABLE_FAVORITES + " f " +
                "ON l." + COLUMN_LOCATION_ID + " = f." + COLUMN_LOCATION_ID_FK +
                " WHERE f." + COLUMN_USER_ID_FK + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOCATION_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));

            Location location = new Location(id, name, description, latitude, longitude);
            favorites.add(location);
        }

        cursor.close();
        db.close();
        return favorites;
    }
}