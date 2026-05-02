// DEV MODE: Auth bypassed, remove before production
package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firstapp.network.ApiClient;
import com.example.firstapp.network.AuthRequest;
import com.example.firstapp.network.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    private TextView tvEmailError;
    private TextView tvPasswordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register);
        tvEmailError = findViewById(R.id.tv_email_error);
        tvPasswordError = findViewById(R.id.tv_password_error);

        btnLogin.setOnClickListener(v -> {
            // Reset errors
            tvEmailError.setVisibility(View.GONE);
            tvPasswordError.setVisibility(View.GONE);
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthRequest request = new AuthRequest(email, password);
            ApiClient.getService(this).login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SharedPreferences prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("token", response.body().token).apply();
                        
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        try {
                            String errorJson = response.errorBody().string();
                            JsonObject errorObj = new Gson().fromJson(errorJson, JsonObject.class);
                            String message = errorObj.get("message").getAsString();

                            if (message.contains("User not found")) {
                                tvEmailError.setVisibility(View.VISIBLE);
                            } else if (message.contains("Incorrect password")) {
                                tvPasswordError.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Cannot reach server. Check connection and try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }
}
