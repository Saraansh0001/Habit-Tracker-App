package com.example.firstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstapp.api.ApiClient;
import com.example.firstapp.api.ApiService;
import com.google.android.material.switchmaterial.SwitchMaterial;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView tvReminderTime;
    private SwitchMaterial switchNotifications;
    private SwitchMaterial switchDarkMode;
    private View toolbar;
    private ApiService apiService;
    private ApiService.UserData currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        apiService = ApiClient.getService(this);
        prefs = getSharedPreferences("HabitTrackerPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        
        // Update status bar icons based on theme
        if (isDarkMode) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        initViews();
        handleWindowInsets();
        setupListeners();
        loadSettings();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvReminderTime = findViewById(R.id.tv_reminder_time);
        switchNotifications = findViewById(R.id.switch_notifications);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
    }

    private void handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
    }

    private void setupListeners() {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentUser != null && currentUser.settings != null) {
                currentUser.settings.notificationsEnabled = isChecked;
                updateSettingsOnBackend();
            }
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
        });

        switchDarkMode.setOnClickListener(v -> {
            boolean isChecked = switchDarkMode.isChecked();
            if (currentUser != null && currentUser.settings != null) {
                currentUser.settings.darkMode = isChecked;
                updateSettingsOnBackend();
            }
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        tvReminderTime.setOnClickListener(v -> showTimePickerDialog());
    }

    private void showTimePickerDialog() {
        int hour = 8;
        int minute = 0;
        
        if (currentUser != null && currentUser.settings != null && currentUser.settings.reminderTime != null) {
            try {
                String[] parts = currentUser.settings.reminderTime.split(":");
                hour = Integer.parseInt(parts[0]);
                String[] minParts = parts[1].split(" ");
                minute = Integer.parseInt(minParts[0]);
                if (currentUser.settings.reminderTime.contains("PM") && hour < 12) hour += 12;
                if (currentUser.settings.reminderTime.contains("AM") && hour == 12) hour = 0;
            } catch (Exception ignored) {}
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String amPm = hourOfDay >= 12 ? "PM" : "AM";
            int displayHour = hourOfDay > 12 ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);
            String time = String.format("%02d:%02d %s", displayHour, minuteOfHour, amPm);
            
            tvReminderTime.setText(time);
            if (currentUser != null && currentUser.settings != null) {
                currentUser.settings.reminderTime = time;
                updateSettingsOnBackend();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void updateSettingsOnBackend() {
        if (currentUser == null) return;
        
        apiService.updateProfile(currentUser).enqueue(new Callback<ApiService.UserData>() {
            @Override
            public void onResponse(Call<ApiService.UserData> call, Response<ApiService.UserData> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Failed to sync settings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.UserData> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, "Network error: Failed to sync", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSettings() {
        // Load local cache first
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);

        boolean darkMode = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(darkMode);

        // Fetch from backend
        apiService.getProfile().enqueue(new Callback<ApiService.UserData>() {
            @Override
            public void onResponse(Call<ApiService.UserData> call, Response<ApiService.UserData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    updateUIWithUserData(currentUser);
                }
            }

            @Override
            public void onFailure(Call<ApiService.UserData> call, Throwable t) {
                // Keep local settings if fetch fails
            }
        });
    }

    private void updateUIWithUserData(ApiService.UserData user) {
        if (user.settings != null) {
            switchNotifications.setChecked(user.settings.notificationsEnabled);
            prefs.edit().putBoolean("notifications_enabled", user.settings.notificationsEnabled).apply();

            switchDarkMode.setChecked(user.settings.darkMode);
            prefs.edit().putBoolean("dark_mode", user.settings.darkMode).apply();
            
            if (user.settings.reminderTime != null) {
                tvReminderTime.setText(user.settings.reminderTime);
            }

            // Apply dark mode if it changed
            if (user.settings.darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    private void handleLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    // Clear preferences
                    prefs.edit().clear().apply();
                    
                    // Navigate to LoginActivity
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
