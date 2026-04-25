package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;
import java.util.stream.Collectors;

public class NavigationFragments {

    public static class AnalyticsFragment extends Fragment {
        private RecyclerView rvTopHabits;
        private HabitAdapter adapter;
        private HabitRepository repository;
        private TextView tvStreak;
        private TextView tvRate;

        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_analytics, container, false);
            
            repository = new HabitRepository(requireContext());
            List<Habit> habits = repository.getUserHabits();

            tvStreak = view.findViewById(R.id.tv_streak_count);
            tvRate = view.findViewById(R.id.tv_completion_rate);
            rvTopHabits = view.findViewById(R.id.rv_top_habits);

            updateStats(habits);
            setupRecyclerView(habits);

            view.findViewById(R.id.tv_streak_count).setOnClickListener(v -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_home);
                }
            });

            view.findViewById(R.id.tv_completion_rate).setOnClickListener(v -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_home);
                }
            });

            return view;
        }

        private void updateStats(List<Habit> habits) {
            int completed = (int) habits.stream().filter(Habit::isCompleted).count();
            int total = habits.size();
            int rate = total > 0 ? (completed * 100) / total : 0;

            tvRate.setText(getString(R.string.percentage_format, rate));
            tvStreak.setText(String.valueOf(completed)); // Simple proxy for now
        }

        private void setupRecyclerView(List<Habit> habits) {
            List<Habit> topHabits = habits.stream().limit(3).collect(Collectors.toList());
            adapter = new HabitAdapter(topHabits);
            rvTopHabits.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvTopHabits.setAdapter(adapter);
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