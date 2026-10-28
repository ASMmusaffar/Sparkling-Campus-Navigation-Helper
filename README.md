# 🗺️ Sparkling Campus Navigation Helper

**Sparkling Campus Navigation Helper** is an Android application that helps students and visitors explore a college campus. Registered users can sign in, browse campus locations in a list, save their favorite spots, and view locations on an interactive Google Map.

> Developed by **AS Mohammed Musaffar** · © 2026 · Part of the *Campus Navigation Helper* app family.

---

## ✨ Features

- 🔐 **User Authentication** — Register and log in with accounts stored in a local SQLite database.
- 🏛️ **Campus Location Browser** — Browse campus locations in a clean, scrollable `RecyclerView` list.
- ⭐ **Favorites** — Mark locations as favorites and revisit them any time from a dedicated screen.
- 🗺️ **Interactive Map** — Open a Google Map view for a campus location (with a graceful fallback screen if the map cannot load).
- 📍 **Location Permissions** — Uses `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` to support map positioning.

## 📱 Screens

| Activity | Purpose |
|----------|---------|
| `LoginActivity` | App entry point — sign in with existing credentials |
| `RegisterActivity` | Create a new user account |
| `MainActivity` | Home screen after login |
| `LocationListActivity` | Browse all campus locations (`RecyclerView`) |
| `FavoritesActivity` | View and manage favorite locations |
| `MapActivity` | Interactive Google Map view of the selected location |

## 🛠️ Tech Stack

- **Language:** Java
- **Platform:** Android (compileSdk 36, minSdk 24)
- **Storage:** SQLite via a custom `DatabaseHelper`
- **UI:** AndroidX, Material Components, `RecyclerView`, `ConstraintLayout`
- **Maps:** Google Play Services Maps (`com.google.android.gms:play-services-maps`)

## 📂 Project Structure

```
app/src/main/java/com/example/campusnavigationhelper/
├── DatabaseHelper.java       # SQLite helper — user accounts & app data
├── LoginActivity.java        # Sign-in screen (launcher)
├── RegisterActivity.java     # Account registration
├── MainActivity.java         # Home screen
├── LocationListActivity.java # Campus location browser (RecyclerView)
├── FavoritesActivity.java    # Saved locations
├── MapActivity.java          # Google Map view of a location
└── Location.java             # Model class for a campus location
```

```
app/src/main/res/layout/
├── activity_login.xml          ├── activity_location_list.xml
├── activity_register.xml       ├── activity_map_fallback.xml
├── activity_main.xml           └── activity_favorites.xml
└── activity_map.xml
```

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (with the Android SDK 36 platform installed)
- A **Google Maps API key** — create one from [Google Cloud Console](https://console.cloud.google.com/) with the *Maps SDK for Android* enabled

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/ASMmusaffar/Sparkling-Campus-Navigation-Helper.git
   ```
2. Open the project in Android Studio and let Gradle sync.
3. Add your Google Maps API key to `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```
4. Run the app on an emulator or physical device running **Android 7.0 (API 24)+**.

## 🧭 Roadmap

- [ ] Google Places integration for smart location search
- [ ] Campus-wide directions & waypoints
- [ ] Cloud sync for user accounts
- [ ] Dark mode polish

## 📄 License

© 2026 AS Mohammed Musaffar. All rights reserved.
