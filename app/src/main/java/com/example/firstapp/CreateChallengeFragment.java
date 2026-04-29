package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        EditText etName = view.findViewById(R.id.et_challenge_name);
        EditText etDescription = view.findViewById(R.id.et_description);
        EditText etParticipants = view.findViewById(R.id.et_participants);
        ChipGroup cgHabitType = view.findViewById(R.id.cg_habit_type);
        ChipGroup cgDuration = view.findViewById(R.id.cg_duration);
        Button btnCreate = view.findViewById(R.id.btn_create_final);

        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String participantsStr = etParticipants.getText().toString();
            
            if (name.isEmpty()) {
                return;
            }

            int participants = participantsStr.isEmpty() ? 50 : Integer.parseInt(participantsStr);
            
            // Get selected habit type
            int typeId = cgHabitType.getCheckedChipId();
            String type = "Custom";
            if (typeId != View.NO_ID) {
                Chip chip = cgHabitType.findViewById(typeId);
                type = chip.getText().toString();
            }

            // Get selected duration
            int durationId = cgDuration.getCheckedChipId();
            String duration = "30d";
            if (durationId != View.NO_ID) {
                Chip chip = cgDuration.findViewById(durationId);
                duration = chip.getText().toString();
            }

            // Determine Icon and Color based on type
            int iconRes = getIconForType(type);
            String color = getColorForType(type);

            Challenge newChallenge = new Challenge(
                    UUID.randomUUID().toString(),
                    name,
                    participants,
                    duration + " left",
                    0,
                    false, 
                    iconRes,
                    type,
                    color
            );

            repository.addChallenge(newChallenge);
            
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private int getIconForType(String type) {
        switch (type) {
            case "Fitness": return R.drawable.ic_workout;
            case "Meditation": return R.drawable.ic_meditation;
            case "Study": return R.drawable.ic_reading;
            case "Health": return R.drawable.ic_health;
            case "Sleep": return R.drawable.ic_sleep;
            default: return R.drawable.ic_nav_arena;
        }
    }

    private String getColorForType(String type) {
        switch (type) {
            case "Fitness": return "#EF4444";
            case "Meditation": return "#6B3FD4";
            case "Study": return "#34D399";
            case "Health": return "#818CF8";
            case "Sleep": return "#F59E0B";
            case "Social": return "#38BDF8";
            default: return "#6B3FD4";
        }
    }
}
