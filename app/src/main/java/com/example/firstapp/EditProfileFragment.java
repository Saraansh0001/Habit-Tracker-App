package com.example.firstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        String name = prefs.getString("profile_name", getString(R.string.user_name_default));
        String rank = prefs.getString("profile_rank", getString(R.string.user_rank));
        String email = prefs.getString("profile_email", getString(R.string.email_hint));

        if (etName != null) etName.setText(name);
        if (etRank != null) etRank.setText(rank);
        if (etEmail != null) etEmail.setText(email);
    }

    private void saveProfileData() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String rank = etRank.getText() != null ? etRank.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";

        if (name.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        prefs.edit()
                .putString("profile_name", name)
                .putString("profile_rank", rank)
                .putString("profile_email", email)
                .apply();

        Toast.makeText(getContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
        
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
