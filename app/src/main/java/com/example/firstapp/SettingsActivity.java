package com.example.firstapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView tvUserName;
    private SwitchMaterial switchNotifications;
    private SwitchMaterial switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("HabitTrackerPrefs", MODE_PRIVATE);
        
        // Let the system handle the theme via AppCompatDelegate.setDefaultNightMode
        // called from the switch or HomeActivity. 
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Standardize status bar behavior
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        initViews();
        setupListeners();
        loadSettings();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_settings_user_name);
        switchNotifications = findViewById(R.id.switch_notifications);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        findViewById(R.id.rl_edit_profile).setOnClickListener(v -> showEditNameDialog());

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
            Toast.makeText(this, isChecked ? "Notifications Enabled" : "Notifications Disabled", Toast.LENGTH_SHORT).show();
        });

        // Use ClickListener for the theme switch to avoid recursive recreation loops
        switchDarkMode.setOnClickListener(v -> {
            boolean isChecked = switchDarkMode.isChecked();
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            
            AppCompatDelegate.setDefaultNightMode(isChecked ? 
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            
            Toast.makeText(this, "Theme updated", Toast.LENGTH_SHORT).show();
            // Note: setDefaultNightMode will automatically recreate the activity stack.
        });

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadSettings() {
        String userName = prefs.getString("user_name", "Team MAD");
        tvUserName.setText(userName);

        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);

        boolean darkMode = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(darkMode);
    }

    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Name");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_name, null);
        final EditText input = viewInflated.findViewById(R.id.et_new_name);
        input.setText(tvUserName.getText().toString());
        builder.setView(viewInflated);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                prefs.edit().putString("user_name", newName).apply();
                tvUserName.setText(newName);
                Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
