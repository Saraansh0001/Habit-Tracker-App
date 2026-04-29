package com.example.firstapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.Habit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationFragments {

    public static class AnalyticsFragment extends Fragment {
        private HabitRepository repository;

<<<<<<< HEAD
    public static class CreateChallengeFragment extends Fragment {
        // Redundant - replaced by standalone CreateChallengeFragment.java
    }

    public static class LeaderboardFragment extends Fragment {
        // Redundant - replaced by standalone LeaderboardFragment.java
    }

    public static class FeaturesFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_features, container, false);
            
            View btnBack = view.findViewById(R.id.btn_back);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    if (getActivity() != null) getActivity().onBackPressed();
=======
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_analytics, container, false);
            
            repository = new HabitRepository(requireContext());
            
            // Mood tracker is currently a static UI demo as per Figma
            view.findViewById(R.id.iv_back_mood).setOnClickListener(v -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_home);
                }
            });

            return view;
        }
    }

    public static class ProfileFragment extends Fragment {
        private com.google.android.material.switchmaterial.SwitchMaterial switchNotifications;
        private com.google.android.material.switchmaterial.SwitchMaterial switchDarkMode;
        private android.content.SharedPreferences prefs;

        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            prefs = requireContext().getSharedPreferences("HabitTrackerPrefs", android.content.Context.MODE_PRIVATE);

            // Connect XP/League to Arena
            View xpContainer = view.findViewById(R.id.cv_xp_container);
            if (xpContainer != null) {
                xpContainer.setOnClickListener(v -> {
                    if (getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_arena);
                    }
>>>>>>> origin/Lakshya-branch
                });
            }

            // Link the Account Settings RelativeLayout to SettingsActivity
            View accountSettings = view.findViewById(R.id.rl_profile_account_settings);
            if (accountSettings != null) {
                accountSettings.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                });
            }

            // Initialize Switches
            switchNotifications = view.findViewById(R.id.switch_notifications_profile);
            switchDarkMode = view.findViewById(R.id.switch_dark_mode_profile);

            loadSettings();
            setupListeners(view);

            return view;
        }

        private void loadSettings() {
            if (switchNotifications != null) {
                switchNotifications.setChecked(prefs.getBoolean("notifications_enabled", true));
            }
            if (switchDarkMode != null) {
                switchDarkMode.setChecked(prefs.getBoolean("dark_mode", false));
            }
        }

        private void setupListeners(View view) {
            if (switchNotifications != null) {
                switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
                });
            }

            if (switchDarkMode != null) {
                switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    prefs.edit().putBoolean("dark_mode", isChecked).apply();
                    if (isChecked) {
                        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
                    }
<<<<<<< HEAD

                    itemView.setOnClickListener(v -> {
                        if ("Streak Calendar".equals(feature.getTitle())) {
                            loadFragment(new StreakCalendarFragment());
                        } else if ("Focus Timer".equals(feature.getTitle())) {
                            loadFragment(new FocusTimerFragment());
                        } else if ("Daily Journal".equals(feature.getTitle())) {
                            loadFragment(new DailyJournalFragment());
                        } else if ("Weekly Goals".equals(feature.getTitle())) {
                            loadFragment(new WeeklyGoalsFragment());
                        } else {
                            // Handle other feature clicks
                        }
                    });
                    container.addView(itemView);
                    
                    // Add a small spacer
                    View spacer = new View(getContext());
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 16));
                    container.addView(spacer);
                } catch (Exception e) {}
=======
                    if (getActivity() != null) {
                        getActivity().recreate();
                    }
                });
>>>>>>> origin/Lakshya-branch
            }

            View logoutBtn = view.findViewById(R.id.btn_logout);
            if (logoutBtn != null) {
                logoutBtn.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                });
            }
        }
    }

    // --- Adapter for Discover Page ---
    public static class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
        private List<Habit> habits;

        public HabitAdapter(List<Habit> habits) { this.habits = habits; }

        public void updateList(List<Habit> newList) {
            this.habits = newList;
            notifyDataSetChanged();
        }

        @NonNull @Override
        public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit_discover, parent, false);
            return new HabitViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
            Habit h = habits.get(position);
            holder.tvTitle.setText(h.getTitle());
            holder.tvCategory.setText(h.getCategory());
            holder.tvDifficulty.setText(h.getDifficulty());
            holder.ivIcon.setImageResource(h.getIconRes() != 0 ? h.getIconRes() : R.drawable.ic_nav_home);

            int color = Color.parseColor(h.getColor());
            holder.ivIcon.setImageTintList(ColorStateList.valueOf(color));
            holder.cvIconContainer.setCardBackgroundColor(Color.argb(30, Color.red(color), Color.green(color), Color.blue(color)));

            // Style Difficulty Badge
            int diffBg, diffText;
            switch (h.getDifficulty()) {
                case "Easy":
                    diffBg = holder.itemView.getContext().getColor(R.color.difficulty_easy_bg);
                    diffText = holder.itemView.getContext().getColor(R.color.difficulty_easy_text);
                    break;
                case "Hard":
                    diffBg = holder.itemView.getContext().getColor(R.color.difficulty_hard_bg);
                    diffText = holder.itemView.getContext().getColor(R.color.difficulty_hard_text);
                    break;
                default: // Medium
                    diffBg = holder.itemView.getContext().getColor(R.color.difficulty_medium_bg);
                    diffText = holder.itemView.getContext().getColor(R.color.difficulty_medium_text);
                    break;
            }
            holder.tvDifficulty.setBackgroundTintList(ColorStateList.valueOf(diffBg));
            holder.tvDifficulty.setTextColor(diffText);

            holder.btnAdd.setOnClickListener(v -> {
                HabitRepository repository = new HabitRepository(v.getContext());
                repository.addHabit(new Habit(h.getTitle(), h.getCategory(), h.getDifficulty(), h.getColor(), h.getIconRes()));
                Toast.makeText(v.getContext(), v.getContext().getString(R.string.habit_added, h.getTitle()), Toast.LENGTH_SHORT).show();
            });
        }

        @Override public int getItemCount() { return habits.size(); }

        static class HabitViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvCategory, tvDifficulty;
            ImageView ivIcon;
            CardView cvIconContainer;
            Button btnAdd;
            HabitViewHolder(View v) {
                super(v);
                tvTitle = v.findViewById(R.id.tv_habit_title);
                tvCategory = v.findViewById(R.id.tv_habit_category);
                tvDifficulty = v.findViewById(R.id.tv_difficulty);
                ivIcon = v.findViewById(R.id.iv_habit_icon);
                cvIconContainer = v.findViewById(R.id.cv_icon_container);
                btnAdd = v.findViewById(R.id.btn_add_habit);
            }
        }
    }
}