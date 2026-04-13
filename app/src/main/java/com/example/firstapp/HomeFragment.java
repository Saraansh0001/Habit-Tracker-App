package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private boolean isHabit1Done = true;
    private boolean isHabit2Done = false;
    private ImageView ivCheck1, ivCheck2;
    private ProgressBar pbMain;
    private TextView tvPercentage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ivCheck1 = view.findViewById(R.id.iv_check_1);
        ivCheck2 = view.findViewById(R.id.iv_check_2);
        pbMain = view.findViewById(R.id.pb_main_progress);
        tvPercentage = view.findViewById(R.id.tv_progress_percentage);

        ivCheck1.setOnClickListener(v -> {
            isHabit1Done = !isHabit1Done;
            updateHabitState(ivCheck1, isHabit1Done);
            updateOverallProgress();
        });

        ivCheck2.setOnClickListener(v -> {
            isHabit2Done = !isHabit2Done;
            updateHabitState(ivCheck2, isHabit2Done);
            updateOverallProgress();
        });

        return view;
    }

    private void updateHabitState(ImageView imageView, boolean isDone) {
        if (isDone) {
            imageView.setImageResource(R.drawable.ic_bolt);
            imageView.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        } else {
            imageView.setImageResource(R.drawable.circle_outline_grey);
            imageView.clearColorFilter();
        }
    }

    private void updateOverallProgress() {
        int progress = 0;
        if (isHabit1Done) progress += 50;
        if (isHabit2Done) progress += 50;

        pbMain.setProgress(progress);
        tvPercentage.setText(progress + "%");
    }
}