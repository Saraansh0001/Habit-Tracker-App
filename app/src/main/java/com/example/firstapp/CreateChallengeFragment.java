package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateChallengeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);
        
        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        Button btnCreate = view.findViewById(R.id.btn_create_final);
        btnCreate.setOnClickListener(v -> {
            EditText etName = view.findViewById(R.id.et_challenge_name);
            String name = etName.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(getContext(), R.string.error_challenge_name, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.challenge_created, name), Toast.LENGTH_LONG).show();
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });

        return view;
    }
}
