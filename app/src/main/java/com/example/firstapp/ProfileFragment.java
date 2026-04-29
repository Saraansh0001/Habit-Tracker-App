package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firstapp.models.ProfileFeature;
import com.example.firstapp.models.ProfileStat;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        setupFeatures(view, inflater);
        setupStats(view, inflater);
        setupActions(view, inflater);
        
        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(getContext(), MainActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void setupFeatures(View view, LayoutInflater inflater) {
        // Feature grid removed or replaced with stats/actions
    }

    private void setupStats(View view, LayoutInflater inflater) {
        LinearLayout statsContainer = view.findViewById(R.id.stats_container);
        if (statsContainer == null) return;

        List<ProfileStat> stats = new ArrayList<>();
        stats.add(new ProfileStat("Best Streak", "18 days", R.drawable.ic_nav_arena));
        stats.add(new ProfileStat("Total XP Earned", "8,240 XP", R.drawable.ic_badge_1));
        stats.add(new ProfileStat("Challenges Won", "7", R.drawable.ic_nav_arena));
        stats.add(new ProfileStat("Focus Minutes", "80 min", R.drawable.ic_nav_analytics));
        stats.add(new ProfileStat("Badges Earned", "4", R.drawable.ic_badge_1));

        for (int i = 0; i < stats.size(); i++) {
            ProfileStat stat = stats.get(i);
            View itemView = inflater.inflate(R.layout.item_profile_stat, statsContainer, false);
            
            TextView label = itemView.findViewById(R.id.tv_stat_label);
            TextView value = itemView.findViewById(R.id.tv_stat_value);
            ImageView icon = itemView.findViewById(R.id.iv_stat_icon);

            label.setText(stat.getLabel());
            value.setText(stat.getValue());
            icon.setImageResource(stat.getIconRes());

            statsContainer.addView(itemView);
            
            if (i < stats.size() - 1) {
                View divider = new View(getContext());
                divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(Color.parseColor("#F1F5F9"));
                statsContainer.addView(divider);
            }
        }
    }

    private void setupActions(View view, LayoutInflater inflater) {
        LinearLayout actionsContainer = view.findViewById(R.id.actions_container);
        if (actionsContainer == null) return;

        String[] titles = {"Edit Profile", "Notifications", "Privacy & Security", "About Discipline Arena"};
        int[] icons = {R.drawable.ic_edit_profile, R.drawable.ic_info, R.drawable.ic_info, R.drawable.ic_info};

        for (int i = 0; i < titles.length; i++) {
            final String titleText = titles[i];
            View itemView = inflater.inflate(R.layout.item_profile_action, actionsContainer, false);
            
            TextView title = itemView.findViewById(R.id.tv_action_title);
            ImageView icon = itemView.findViewById(R.id.iv_action_icon);

            title.setText(titleText);
            icon.setImageResource(icons[i]);

            itemView.setOnClickListener(v -> {
                if (titleText.equals("Edit Profile")) {
                    if (getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).loadFragment(new EditProfileFragment());
                    }
                } else if (titleText.equals("Notifications") || titleText.equals("Privacy & Security") || titleText.equals("About Discipline Arena")) {
                    // Navigate to settings or specific detail
                    android.content.Intent intent = new android.content.Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
            actionsContainer.addView(itemView);
            
            if (i < titles.length - 1) {
                View divider = new View(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                lp.setMargins(48, 0, 0, 0);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(Color.parseColor("#F1F5F9"));
                actionsContainer.addView(divider);
            }
        }
    }
}