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

import com.example.firstapp.data.ChallengeRepository;
import com.example.firstapp.models.Challenge;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.UUID;

public class CreateChallengeFragment extends Fragment {
    
    private ChallengeRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);
        
        repository = new ChallengeRepository(requireContext());

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        Button btnCreate = view.findViewById(R.id.btn_create_final);
        btnCreate.setOnClickListener(v -> {
            EditText etName = view.findViewById(R.id.et_challenge_name);
            EditText etParticipants = view.findViewById(R.id.et_participants);
            ChipGroup cgHabitType = view.findViewById(R.id.cg_habit_type);
            ChipGroup cgDuration = view.findViewById(R.id.cg_duration);

            String name = etName.getText().toString();
            String participantsStr = etParticipants.getText().toString();
            
            int checkedTypeChipId = cgHabitType.getCheckedChipId();
            int checkedDurationChipId = cgDuration.getCheckedChipId();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), R.string.error_challenge_name, Toast.LENGTH_SHORT).show();
                return;
            }

            int participants = participantsStr.isEmpty() ? 50 : Integer.parseInt(participantsStr);
            
            String type = "Custom";
            if (checkedTypeChipId != View.NO_ID) {
                Chip chip = cgHabitType.findViewById(checkedTypeChipId);
                type = chip.getText().toString();
            }

            String duration = "7 days";
            if (checkedDurationChipId != View.NO_ID) {
                Chip chip = cgDuration.findViewById(checkedDurationChipId);
                duration = chip.getText().toString();
            }

            // Determine Icon and Color based on type
            int iconRes = R.drawable.ic_nav_arena;
            String color = "#6B3FD4";
            
            if (type.equals("Fitness")) { iconRes = R.drawable.ic_workout; color = "#EF4444"; }
            else if (type.equals("Meditation")) { iconRes = R.drawable.ic_meditation; color = "#6B3FD4"; }
            else if (type.equals("Study")) { iconRes = R.drawable.ic_reading; color = "#34D399"; }
            else if (type.equals("Health")) { iconRes = R.drawable.ic_health; color = "#818CF8"; }

            Challenge newChallenge = new Challenge(
                UUID.randomUUID().toString(),
                name,
                participants,
                duration,
                0,
                false,
                iconRes,
                type,
                color
            );

            repository.addChallenge(newChallenge);

            Toast.makeText(getContext(), getString(R.string.challenge_created, name), Toast.LENGTH_LONG).show();
            if (getActivity() != null) getActivity().onBackPressed();
        });

        return view;
    }
}
