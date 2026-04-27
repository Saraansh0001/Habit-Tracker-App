package com.example.firstapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before super.onCreate
        SharedPreferences prefs = getSharedPreferences("HabitTrackerPrefs", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_home);

        View root = findViewById(R.id.main_content);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Apply window insets to handle status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            // Adjust BottomNav padding for navigation bar
            bottomNav.setPadding(0, 0, 0, systemBars.bottom);
            
            return windowInsets;
        });
        
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_search) {
                selectedFragment = new NavigationFragments.SearchFragment();
            } else if (itemId == R.id.navigation_arena) {
                selectedFragment = new ArenaFragment();
            } else if (itemId == R.id.navigation_analytics) {
                selectedFragment = new AnalyticsFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    public void navigateToTab(int itemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(itemId);
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}