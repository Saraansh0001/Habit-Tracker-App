package com.example.firstapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextView tab_login, tab_sign_up, tv_forgot_password;
    private TextInputEditText et_email, et_password;
    private View btn_login, btn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        init_views();
        setup_tab_toggle();
        setup_click_listeners();
    }

    private void init_views() {
        tab_login          = findViewById(R.id.tab_login);
        tab_sign_up        = findViewById(R.id.tab_sign_up);
        et_email           = findViewById(R.id.et_email);
        et_password        = findViewById(R.id.et_password);
        btn_login          = findViewById(R.id.btn_login);
        btn_google         = findViewById(R.id.btn_google);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
    }

    private void setup_tab_toggle() {
        tab_login.setOnClickListener(v -> {
            tab_login.setBackgroundResource(R.drawable.bg_tab_selected);
            tab_login.setTextColor(getColor(R.color.text_dark));
            tab_sign_up.setBackgroundColor(Color.TRANSPARENT);
            tab_sign_up.setTextColor(getColor(R.color.text_secondary));
        });

        tab_sign_up.setOnClickListener(v -> {
            tab_sign_up.setBackgroundResource(R.drawable.bg_tab_selected);
            tab_sign_up.setTextColor(getColor(R.color.text_dark));
            tab_login.setBackgroundColor(Color.TRANSPARENT);
            tab_login.setTextColor(getColor(R.color.text_secondary));
        });
    }

    private void setup_click_listeners() {
        btn_login.setOnClickListener(v -> {
            String email    = et_email.getText() != null ? et_email.getText().toString().trim() : "";
            String password = et_password.getText() != null ? et_password.getText().toString() : "";
            if (email.isEmpty() || password.isEmpty()) {
                return;
            }
            // TODO: add your auth logic here
        });

        btn_google.setOnClickListener(v -> {
            // TODO: Google Sign-In flow
        });

        tv_forgot_password.setOnClickListener(v -> {
            // TODO: navigate to forgot password screen
        });
    }
}