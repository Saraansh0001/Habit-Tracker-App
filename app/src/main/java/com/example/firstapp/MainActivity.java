package com.example.firstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextView tab_login, tab_sign_up, tv_forgot_password;
    private TextInputEditText et_email, et_password, et_name;
    private View btn_google;
    private Button btn_submit;
    private LinearLayout ll_full_name;
    private boolean isLoginMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        init_views();
        setup_tab_toggle();
        setup_click_listeners();
        updateUI();
    }

    private void init_views() {
        tab_login          = findViewById(R.id.tab_login);
        tab_sign_up        = findViewById(R.id.tab_sign_up);
        et_email           = findViewById(R.id.et_email);
        et_password        = findViewById(R.id.et_password);
        et_name            = findViewById(R.id.et_name);
        btn_submit         = findViewById(R.id.btn_submit);
        btn_google         = findViewById(R.id.btn_google);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        ll_full_name       = findViewById(R.id.ll_full_name);
    }

    private void setup_tab_toggle() {
        tab_login.setOnClickListener(v -> {
            if (isLoginMode) return;
            isLoginMode = true;
            updateUI();
        });

        tab_sign_up.setOnClickListener(v -> {
            if (!isLoginMode) return;
            isLoginMode = false;
            updateUI();
        });
    }

    private void updateUI() {
        if (isLoginMode) {
            tab_login.setBackgroundResource(R.drawable.bg_tab_selected);
            tab_login.setTextColor(getColor(R.color.text_dark));
            tab_sign_up.setBackgroundColor(Color.TRANSPARENT);
            tab_sign_up.setTextColor(getColor(R.color.text_secondary));
            
            ll_full_name.setVisibility(View.GONE);
            tv_forgot_password.setVisibility(View.VISIBLE);
            btn_submit.setText("Log In");
        } else {
            tab_sign_up.setBackgroundResource(R.drawable.bg_tab_selected);
            tab_sign_up.setTextColor(getColor(R.color.text_dark));
            tab_login.setBackgroundColor(Color.TRANSPARENT);
            tab_login.setTextColor(getColor(R.color.text_secondary));
            
            ll_full_name.setVisibility(View.VISIBLE);
            tv_forgot_password.setVisibility(View.GONE);
            btn_submit.setText("Create Account");
        }
    }

    private void setup_click_listeners() {
        btn_submit.setOnClickListener(v -> {
            String email    = et_email.getText() != null ? et_email.getText().toString().trim() : "";
            String password = et_password.getText() != null ? et_password.getText().toString() : "";
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isLoginMode) {
                String name = et_name.getText() != null ? et_name.getText().toString().trim() : "";
                if (name.isEmpty()) {
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        btn_google.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Google Accounts...", Toast.LENGTH_SHORT).show();
        });

        tv_forgot_password.setOnClickListener(v -> {
            Toast.makeText(this, "Reset password link sent", Toast.LENGTH_SHORT).show();
        });
    }
}