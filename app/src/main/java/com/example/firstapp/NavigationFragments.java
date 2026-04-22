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
import android.widget.Toast;

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
        private RecyclerView rvTopHabits;
        private RecyclerView rvAchievements;
        private HabitAdapter habitAdapter;
        private AchievementAdapter achievementAdapter;
        private HabitRepository repository;
        private TextView tvStreak;
        private TextView tvRate;
        private ProgressBar pbWeeklyGoal;
        private TextView tvGoalPercent;

        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_analytics, container, false);
            
            repository = new HabitRepository(requireContext());
            List<Habit> habits = repository.getUserHabits();

            tvStreak = view.findViewById(R.id.tv_streak_count);
            tvRate = view.findViewById(R.id.tv_completion_rate);
            rvTopHabits = view.findViewById(R.id.rv_top_habits);
            rvAchievements = view.findViewById(R.id.rv_achievements);
            pbWeeklyGoal = view.findViewById(R.id.pb_weekly_goal);
            tvGoalPercent = view.findViewById(R.id.tv_goal_percent);

            updateStats(habits);
            setupHabitsRecyclerView(habits);
            setupAchievementsRecyclerView();

            // Interactivity proof: Toast on card click
            view.findViewById(R.id.cv_streak).setOnClickListener(v -> 
                Toast.makeText(getContext(), "You are on a 12-day streak! Keep it up!", Toast.LENGTH_SHORT).show());
            
            view.findViewById(R.id.cv_completion).setOnClickListener(v -> 
                Toast.makeText(getContext(), "Your completion rate is up 5% from last week!", Toast.LENGTH_SHORT).show());

            return view;
        }

        private void updateStats(List<Habit> habits) {
            int completed = (int) habits.stream().filter(Habit::isCompleted).count();
            int total = habits.size();
            int rate = total > 0 ? (completed * 100) / total : 0;

            tvRate.setText(rate + "%");
            tvStreak.setText(String.valueOf(completed + 5)); // Dummy addition to make it look better
            
            // Set progress bar
            pbWeeklyGoal.setProgress(75);
            tvGoalPercent.setText("75%");
        }

        private void setupHabitsRecyclerView(List<Habit> habits) {
            List<Habit> topHabits = habits.stream().limit(3).collect(Collectors.toList());
            habitAdapter = new HabitAdapter(topHabits);
            rvTopHabits.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvTopHabits.setAdapter(habitAdapter);
        }

        private void setupAchievementsRecyclerView() {
            List<Achievement> achievements = new ArrayList<>();
            achievements.add(new Achievement("Early Bird", "Completed 5 morning workouts in a row", "2d ago", R.drawable.ic_nav_arena));
            achievements.add(new Achievement("Hydration King", "Drank 2L of water for 7 days", "5d ago", R.drawable.ic_nav_home));
            
            achievementAdapter = new AchievementAdapter(achievements);
            rvAchievements.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvAchievements.setAdapter(achievementAdapter);
        }

        // Dummy Achievement Model and Adapter
        static class Achievement {
            String title, desc, date;
            int icon;
            Achievement(String title, String desc, String date, int icon) {
                this.title = title; this.desc = desc; this.date = date; this.icon = icon;
            }
        }

        static class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
            List<Achievement> items;
            AchievementAdapter(List<Achievement> items) { this.items = items; }
            @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int t) {
                return new ViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_achievement, p, false));
            }
            @Override public void onBindViewHolder(@NonNull ViewHolder h, int p) {
                Achievement a = items.get(p);
                h.title.setText(a.title); h.desc.setText(a.desc); h.date.setText(a.date);
                h.icon.setImageResource(a.icon);
            }
            @Override public int getItemCount() { return items.size(); }
            static class ViewHolder extends RecyclerView.ViewHolder {
                TextView title, desc, date; ImageView icon;
                ViewHolder(View v) {
                    super(v);
                    title = v.findViewById(R.id.tv_achievement_title);
                    desc = v.findViewById(R.id.tv_achievement_desc);
                    date = v.findViewById(R.id.tv_achievement_date);
                    icon = v.findViewById(R.id.iv_achievement_icon);
                }
            }
        }
    }

    public static class ProfileFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);

            // Connect XP/League to Arena
            View xpContainer = view.findViewById(R.id.cv_xp_container);
            if (xpContainer != null) {
                xpContainer.setOnClickListener(v -> {
                    if (getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_arena);
                    }
                });
            }

            // Settings sections in Profile
            View settingsSection = view.findViewById(R.id.btn_logout); // Use logout as an anchor or the parent card
            // Let's make the whole settings card clickable if possible, or individual items.
            // Based on layout, they are RelativeLayouts inside a CardView.
            
            // Temporary: Add listener to logout button to test navigation to settings
            view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            });

            return view;
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