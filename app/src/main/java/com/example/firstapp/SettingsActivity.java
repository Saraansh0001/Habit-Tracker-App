package com.example.firstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        if (prefs.getBoolean("dark_mode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Update status bar icons based on theme
        if (prefs.getBoolean("dark_mode", false)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // Forces activity to restart to apply theme immediately
            recreate();
        });

        // Logout functionality removed as per request to remove auth screens
        View logoutBtn = findViewById(R.id.btn_logout);
        if (logoutBtn != null) {
            logoutBtn.setVisibility(View.GONE);
        }

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
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
