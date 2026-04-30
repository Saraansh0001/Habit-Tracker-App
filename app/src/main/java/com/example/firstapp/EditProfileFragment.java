package com.example.firstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.example.firstapp.network.ApiClient;
import com.example.firstapp.network.ApiService;
import com.example.firstapp.network.UserProfileResponse;
import com.example.firstapp.network.UpdateProfileRequest;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private SharedPreferences prefs;
    private TextInputEditText etName, etRank, etEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        prefs = requireContext().getSharedPreferences("HabitTrackerPrefs", Context.MODE_PRIVATE);

        etName = view.findViewById(R.id.et_profile_name);
        etRank = view.findViewById(R.id.et_profile_rank);
        etEmail = view.findViewById(R.id.et_profile_email);

        loadProfileDataFromBackend();

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_save_profile).setOnClickListener(v -> {
            saveProfileData();
        });

        return view;
    }

    private void loadProfileDataFromBackend() {
        // Load cached data first for immediate visibility
        if (etName != null) etName.setText(prefs.getString("profile_name", ""));
        if (etEmail != null) etEmail.setText(prefs.getString("profile_email", ""));
        if (etRank != null) etRank.setText(prefs.getString("profile_rank", ""));

        ApiClient.getService(getContext()).getProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    UserProfileResponse profile = response.body();
                    if (etName != null) etName.setText(profile.name);
                    if (etEmail != null) etEmail.setText(profile.email);
                    if (etRank != null) etRank.setText(profile.rank);
                    
                    // Update cache
                    prefs.edit()
                        .putString("profile_name", profile.name)
                        .putString("profile_email", profile.email)
                        .putString("profile_rank", profile.rank)
                        .apply();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Fail silently, user sees cached data
            }
        });
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String rank = etRank.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(name, "", rank); // avatarUrl empty for now
        
        ApiClient.getService(getContext()).updateProfile(request).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    // Also save to prefs for offline/quick access
                    prefs.edit()
                            .putString("profile_name", name)
                            .putString("profile_rank", rank)
                            .putString("profile_email", email)
                            .apply();
                    
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
