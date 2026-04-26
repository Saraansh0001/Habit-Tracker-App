package com.example.firstapp;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firstapp.models.ProfileFeature;
import com.example.firstapp.models.ProfileStat;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        if (getContext() != null) {
            prefs = getContext().getSharedPreferences("HabitTrackerPrefs", Context.MODE_PRIVATE);
        }

        setupProfilePic(view);
        setupBadges(view);
        setupFeatures(view, inflater);
        setupStats(view, inflater);
        setupActions(view, inflater);
        
        View logoutBtn = view.findViewById(R.id.btn_logout);
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Logging out...", Toast.LENGTH_SHORT).show());
        }
            
        return view;
    }

    private void setupProfilePic(View view) {
        ImageView profilePic = view.findViewById(R.id.iv_profile_pic);
        if (profilePic != null) {
            profilePic.setImageResource(R.drawable.user_avatar_placeholder);
            profilePic.setBackgroundColor(Color.parseColor("#6B3FD4")); 
        }
    }

    private void setupBadges(View view) {
        View seeAll = view.findViewById(R.id.tv_see_all_badges);
        if (seeAll != null) {
            seeAll.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Opening all badges...", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupFeatures(View view, LayoutInflater inflater) {
        ViewGroup featuresGrid = view.findViewById(R.id.features_grid);
        if (featuresGrid == null) return;
        featuresGrid.removeAllViews();
        
        if (featuresGrid instanceof android.widget.GridLayout) {
            ((android.widget.GridLayout) featuresGrid).setColumnCount(1);
        }

        View allFeatures = view.findViewById(R.id.tv_all_features);
        if (allFeatures != null) {
            allFeatures.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Opening all features...", Toast.LENGTH_SHORT).show());
        }

        List<ProfileFeature> features = new ArrayList<>();
        features.add(new ProfileFeature("Streak Calendar", "Visual habit history", R.drawable.ic_nav_home, "#F3F0FF"));
        features.add(new ProfileFeature("Focus Timer", "Deep focus on one habit at a time", R.drawable.ic_nav_analytics, "#FFF7ED"));
        features.add(new ProfileFeature("Weekly Goals", "Set habit target", R.drawable.ic_workout, "#F0FDF4"));
        features.add(new ProfileFeature("Daily Journal", "Reflect & grow", R.drawable.ic_edit, "#EEF2FF"));
        features.add(new ProfileFeature("Mood Tracker", "Track how you feel", R.drawable.ic_meditation, "#FFF1F2"));
        features.add(new ProfileFeature("Friends", "Social accountability", R.drawable.ic_social, "#F0FDFA"));
        features.add(new ProfileFeature("Rewards", "Badges & milestone", R.drawable.ic_badge_1, "#FEFCE8"));
        features.add(new ProfileFeature("Weekly Report", "Sunday summary", R.drawable.ic_nav_analytics, "#FDF2F8"));

        for (ProfileFeature feature : features) {
            View itemView = inflater.inflate(R.layout.item_profile_feature, featuresGrid, false);
            
            TextView title = itemView.findViewById(R.id.tv_feature_title);
            TextView desc = itemView.findViewById(R.id.tv_feature_desc);
            ImageView icon = itemView.findViewById(R.id.iv_feature_icon);
            CardView iconContainer = itemView.findViewById(R.id.cv_feature_icon_container);

            if (title != null) title.setText(feature.getTitle());
            if (desc != null) desc.setText(feature.getDescription());
            if (icon != null) icon.setImageResource(feature.getIconRes());
            
            int bgColor = Color.parseColor(feature.getBackgroundColor());
            if (iconContainer != null) iconContainer.setCardBackgroundColor(bgColor);

            if (icon != null) {
                float[] hsv = new float[3];
                Color.colorToHSV(bgColor, hsv);
                hsv[2] *= 0.5f; 
                hsv[1] = Math.min(1.0f, hsv[1] * 2.0f); 
                icon.setImageTintList(ColorStateList.valueOf(Color.HSVToColor(hsv)));
            }

            itemView.setOnClickListener(v -> {
                if ("Streak Calendar".equals(feature.getTitle())) {
                    loadFragment(new StreakCalendarFragment());
                } else if ("Focus Timer".equals(feature.getTitle())) {
                    loadFragment(new FocusTimerFragment());
                } else if ("Daily Journal".equals(feature.getTitle())) {
                    loadFragment(new DailyJournalFragment());
                } else {
                    Toast.makeText(getContext(), feature.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                }
            });
            featuresGrid.addView(itemView);
        }
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).loadFragment(fragment);
        }
    }

    private void setupStats(View view, LayoutInflater inflater) {
        LinearLayout statsContainer = view.findViewById(R.id.stats_container);
        if (statsContainer == null) return;
        
        int childCount = statsContainer.getChildCount();
        if (childCount > 1) {
            statsContainer.removeViews(1, childCount - 1);
        }

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

            if (label != null) label.setText(stat.getLabel());
            if (value != null) value.setText(stat.getValue());
            if (icon != null) {
                icon.setImageResource(stat.getIconRes());
                icon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#6B3FD4")));
            }

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
        actionsContainer.removeAllViews();

        View darkModeItem = inflater.inflate(R.layout.item_profile_switch, actionsContainer, false);
        TextView dmTitle = darkModeItem.findViewById(R.id.tv_switch_title);
        ImageView dmIcon = darkModeItem.findViewById(R.id.iv_switch_icon);
        SwitchMaterial dmSwitch = darkModeItem.findViewById(R.id.switch_action);
        
        if (dmTitle != null) dmTitle.setText(R.string.dark_mode);
        if (dmIcon != null) {
            dmIcon.setImageResource(R.drawable.ic_meditation);
            dmIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#94A3B8")));
        }
        
        if (prefs != null && dmSwitch != null) {
            dmSwitch.setChecked(prefs.getBoolean("dark_mode", false));
        }
        
        if (dmSwitch != null) {
            dmSwitch.setOnClickListener(v -> {
                boolean isChecked = dmSwitch.isChecked();
                if (prefs != null) {
                    prefs.edit().putBoolean("dark_mode", isChecked).apply();
                }
                
                AppCompatDelegate.setDefaultNightMode(isChecked ? 
                        AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                
                Context context = getContext();
                if (context != null) {
                    Toast.makeText(context, isChecked ? "Dark Mode Enabled" : "Dark Mode Disabled", Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        actionsContainer.addView(darkModeItem);
        addDivider(actionsContainer);

        String[] titles = {getString(R.string.edit_profile), getString(R.string.notifications), getString(R.string.privacy_security), getString(R.string.about_app)};
        int[] icons = {R.drawable.ic_edit_profile, R.drawable.ic_info, R.drawable.ic_info, R.drawable.ic_info};

        for (int i = 0; i < titles.length; i++) {
            final String titleText = titles[i];
            View itemView = inflater.inflate(R.layout.item_profile_action, actionsContainer, false);
            
            TextView title = itemView.findViewById(R.id.tv_action_title);
            ImageView icon = itemView.findViewById(R.id.iv_action_icon);

            if (title != null) title.setText(titleText);
            if (icon != null) {
                icon.setImageResource(icons[i]);
                icon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#94A3B8")));
            }

            itemView.setOnClickListener(v -> Toast.makeText(getContext(), titleText + " clicked", Toast.LENGTH_SHORT).show());
            actionsContainer.addView(itemView);
            
            if (i < titles.length - 1) {
                addDivider(actionsContainer);
            }
        }
    }
    
    private void addDivider(LinearLayout container) {
        View divider = new View(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(140, 0, 0, 0);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(Color.parseColor("#F1F5F9"));
        container.addView(divider);
    }
}
