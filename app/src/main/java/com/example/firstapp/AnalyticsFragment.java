package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnalyticsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        
        setupClickListeners(view);
        
        return view;
    }

    private void setupClickListeners(View view) {
        View cardStreak = view.findViewById(R.id.card_streak);
        if (cardStreak != null) {
            cardStreak.setOnClickListener(v -> 
                showToast("Streak details coming soon!"));
        }
        
        TextView tvThisWeek = view.findViewById(R.id.tv_this_week);
        if (tvThisWeek != null) {
            tvThisWeek.setOnClickListener(v -> 
                showToast("Weekly range selector"));
        }

        // Add more click listeners as needed for mock items
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}