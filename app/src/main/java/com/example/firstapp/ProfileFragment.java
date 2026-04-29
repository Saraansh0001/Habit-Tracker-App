package com.example.firstapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        
        view.findViewById(R.id.tv_see_all_features).setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new FeaturesFragment());
            }
        });

        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
            
        return view;
    }

    private void setupActions(View view, LayoutInflater inflater) {
        LinearLayout actionsContainer = view.findViewById(R.id.actions_container);
        if (actionsContainer == null) return;

        String[] titles = {"Edit Profile", "Settings", "Help & Support", "About Discipline Arena"};
        int[] icons = {R.drawable.ic_edit_profile, R.drawable.ic_info, R.drawable.ic_info, R.drawable.ic_info};

        for (int i = 0; i < titles.length; i++) {
            final String titleText = titles[i];
            View itemView = inflater.inflate(R.layout.item_profile_action, actionsContainer, false);
            
            TextView title = itemView.findViewById(R.id.tv_action_title);
            ImageView icon = itemView.findViewById(R.id.iv_action_icon);

            title.setText(titleText);
            icon.setImageResource(icons[i]);

            itemView.setOnClickListener(v -> {
                if (titleText.equals("Settings")) {
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                } else {
                    Toast.makeText(getContext(), titleText + " clicked", Toast.LENGTH_SHORT).show();
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

    private void setupFeatures(View view, LayoutInflater inflater) {
        ViewGroup featuresGrid = view.findViewById(R.id.features_grid);
        if (featuresGrid == null) return;

        List<ProfileFeature> features = new ArrayList<>();
        features.add(new ProfileFeature("Streak Calendar", "Visual habit history", R.drawable.ic_bolt, "#F5F3FF"));
        features.add(new ProfileFeature("Focus Timer", "Deep work session", R.drawable.ic_meditation, "#FFF7ED"));
        features.add(new ProfileFeature("Weekly Goals", "Set habit target", R.drawable.ic_nav_arena, "#ECFDF5"));
        features.add(new ProfileFeature("Daily Journal", "Reflect & grow", R.drawable.ic_edit, "#EEF2FF"));
        features.add(new ProfileFeature("Mood Tracker", "Track how you feel", R.drawable.ic_nav_analytics, "#FFF1F2"));
        features.add(new ProfileFeature("Friends", "Social accountability", R.drawable.ic_social, "#F0FDFA"));
        features.add(new ProfileFeature("Rewards", "Badges & milestone", R.drawable.ic_badge_1, "#FFFBEB"));
        features.add(new ProfileFeature("Weekly Report", "Sunday summary", R.drawable.ic_nav_analytics, "#FDF2F8"));

        for (ProfileFeature feature : features) {
            View itemView = inflater.inflate(R.layout.item_profile_feature, featuresGrid, false);
            
            TextView title = itemView.findViewById(R.id.tv_feature_title);
            TextView desc = itemView.findViewById(R.id.tv_feature_desc);
            ImageView icon = itemView.findViewById(R.id.iv_feature_icon);
            CardView iconContainer = itemView.findViewById(R.id.cv_feature_icon_container);

            title.setText(feature.getTitle());
            desc.setText(feature.getDescription());
            icon.setImageResource(feature.getIconRes());
            iconContainer.setCardBackgroundColor(Color.parseColor(feature.getBackgroundColor()));
            
            // Generate a darker shade for the icon tint based on background
            int bgColor = Color.parseColor(feature.getBackgroundColor());
            float[] hsv = new float[3];
            Color.colorToHSV(bgColor, hsv);
            hsv[2] *= 0.6f; // Darken
            hsv[1] = Math.min(1.0f, hsv[1] * 1.5f); // Saturate
            icon.setImageTintList(ColorStateList.valueOf(Color.HSVToColor(hsv)));

            itemView.setOnClickListener(v -> {
                Fragment targetFragment = null;
                switch (feature.getTitle()) {
                    case "Streak Calendar":
                        targetFragment = new StreakCalendarFragment();
                        break;
                    case "Focus Timer":
                        targetFragment = new FocusTimerFragment();
                        break;
                    case "Weekly Goals":
                        targetFragment = new WeeklyGoalsFragment();
                        break;
                    case "Daily Journal":
                        targetFragment = new DailyJournalFragment();
                        break;
                    case "Mood Tracker":
                        targetFragment = new AnalyticsFragment();
                        break;
                    default:
                        Toast.makeText(getContext(), feature.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                
                if (targetFragment != null && getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).loadFragment(targetFragment);
                }
            });
            featuresGrid.addView(itemView);
        }
    }

    private void setupStats(View view, LayoutInflater inflater) {
        LinearLayout statsContainer = view.findViewById(R.id.stats_container);
        if (statsContainer == null) return;

        List<ProfileStat> stats = new ArrayList<>();
        stats.add(new ProfileStat("Best Streak", "18 days", R.drawable.ic_bolt));
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

}