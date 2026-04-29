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

        loadProfileData();

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_save_profile).setOnClickListener(v -> {
            saveProfileData();
        });

        return view;
    }

    private void loadProfileData() {
        String name = prefs.getString("profile_name", "Team MAD");
        String rank = prefs.getString("profile_rank", "Warrior 🎖️");
        String email = prefs.getString("profile_email", "you@email.com");

        if (etName != null) etName.setText(name);
        if (etRank != null) etRank.setText(rank);
        if (etEmail != null) etEmail.setText(email);
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String rank = etRank.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty()) {
            return;
        }

        prefs.edit()
                .putString("profile_name", name)
                .putString("profile_rank", rank)
                .putString("profile_email", email)
                .apply();

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
