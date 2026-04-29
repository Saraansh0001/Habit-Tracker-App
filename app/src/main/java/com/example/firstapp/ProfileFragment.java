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
        ImageView profilePic = view.findViewById(R.id.iv_profile_avatar);
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
            try {
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
            } catch (Exception e) {}
        }
    }

    private void setupActions(View view, LayoutInflater inflater) {
        LinearLayout actionsContainer = view.findViewById(R.id.actions_container);
        if (actionsContainer == null) return;
        actionsContainer.removeAllViews();

        try {
            View darkModeItem = inflater.inflate(R.layout.item_profile_switch, actionsContainer, false);
            TextView dmTitle = darkModeItem.findViewById(R.id.tv_switch_title);
            ImageView dmIcon = darkModeItem.findViewById(R.id.iv_switch_icon);
            SwitchMaterial dmSwitch = darkModeItem.findViewById(R.id.switch_action);
            
            if (dmTitle != null) dmTitle.setText(R.string.dark_mode);
            if (dmIcon != null) {
                dmIcon.setImageResource(R.drawable.ic_bolt);
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
        } catch (Exception e) {}

        String[] titles = {getString(R.string.edit_profile), getString(R.string.notifications), getString(R.string.privacy_security), getString(R.string.about_app)};
        int[] icons = {R.drawable.ic_bolt, R.drawable.ic_bolt, R.drawable.ic_bolt, R.drawable.ic_bolt};

        for (int i = 0; i < titles.length; i++) {
            final String titleText = titles[i];
            try {
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
            } catch (Exception e) {}
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

    private void loadFragment(Fragment fragment) {
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).loadFragment(fragment);
        }
    }
}