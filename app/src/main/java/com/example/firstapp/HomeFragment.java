package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private boolean isHabit1Done = true;
    private boolean isHabit2Done = false;
    private boolean isHabit3Done = false;

    private ImageView ivCheck1, ivCheck2, ivCheck3;
    private ProgressBar pbMain;
    private TextView tvPercentage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupListeners(view);
        
        // Set initial state
        updateHabitState(ivCheck1, isHabit1Done);
        updateHabitState(ivCheck2, isHabit2Done);
        updateHabitState(ivCheck3, isHabit3Done);
        updateOverallProgress();

        return view;
    }

    private void initViews(View view) {
        ivCheck1 = view.findViewById(R.id.iv_check_1);
        ivCheck2 = view.findViewById(R.id.iv_check_2);
        ivCheck3 = view.findViewById(R.id.iv_check_3);
        pbMain = view.findViewById(R.id.pb_main_progress);
        tvPercentage = view.findViewById(R.id.tv_progress_percentage);
    }

    private void setupListeners(View view) {
        ivCheck1.setOnClickListener(v -> {
            isHabit1Done = !isHabit1Done;
            updateHabitState(ivCheck1, isHabit1Done);
            updateOverallProgress();
            showProgressToast(isHabit1Done);
        });

        ivCheck2.setOnClickListener(v -> {
            isHabit2Done = !isHabit2Done;
            updateHabitState(ivCheck2, isHabit2Done);
            updateOverallProgress();
            showProgressToast(isHabit2Done);
        });

        if (ivCheck3 != null) {
            ivCheck3.setOnClickListener(v -> {
                isHabit3Done = !isHabit3Done;
                updateHabitState(ivCheck3, isHabit3Done);
                updateOverallProgress();
                showProgressToast(isHabit3Done);
            });
        }

        FloatingActionButton fab = view.findViewById(R.id.fab_add_habit);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).loadFragment(new NavigationFragments.CreateChallengeFragment());
                }
            });
        }

        View streakVal = view.findViewById(R.id.tv_streak_val);
        if (streakVal != null) {
            streakVal.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Streak details coming soon!", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateHabitState(ImageView imageView, boolean isDone) {
        if (imageView == null) return;
        if (isDone) {
            imageView.setImageResource(R.drawable.ic_bolt);
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
        } else {
            imageView.setImageResource(R.drawable.circle_outline_grey);
            imageView.clearColorFilter();
        }
    }

    private void updateOverallProgress() {
        int doneCount = 0;
        if (isHabit1Done) doneCount++;
        if (isHabit2Done) doneCount++;
        if (isHabit3Done) doneCount++;

        int progress = (int) ((doneCount / 3.0) * 100);

        if (pbMain != null) pbMain.setProgress(progress);
        if (tvPercentage != null) tvPercentage.setText(progress + "%");
    }

    private void showProgressToast(boolean isDone) {
        String msg = isDone ? "Habit completed! Keep it up!" : "Habit marked as incomplete";
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}