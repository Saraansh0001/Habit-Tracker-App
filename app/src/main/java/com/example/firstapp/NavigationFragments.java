package com.example.firstapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.models.Habit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationFragments {

    private static View createPlaceholder(LayoutInflater inflater, ViewGroup container, String text) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        TextView title = view.findViewById(R.id.placeholder_text);
        if (title != null) title.setText(text);
        return view;
    }



    public static class LeaderboardFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
            
            view.findViewById(R.id.btn_back).setOnClickListener(v -> {
                if (getActivity() != null) getActivity().onBackPressed();
            });

            RecyclerView rv = view.findViewById(R.id.rv_leaderboard);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(new LeaderboardAdapter(getMockLeaderboard()));

            return view;
        }

        private List<LeaderboardUser> getMockLeaderboard() {
            List<LeaderboardUser> users = new ArrayList<>();
            users.add(new LeaderboardUser("4", "Aayush Rathore", "3890 XP", "🔥 28"));
            users.add(new LeaderboardUser("5", "Shejal Kushwaha", "3650 XP", "🔥 25"));
            users.add(new LeaderboardUser("6", "Priya Das", "3420 XP", "🔥 22"));
            users.add(new LeaderboardUser("7", "Romi", "3200 XP", "🔥 20"));
            users.add(new LeaderboardUser("8", "Sachin Singh", "2980 XP", "🔥 18"));
            users.add(new LeaderboardUser("9", "Aryan", "2750 XP", "🔥 15"));
            users.add(new LeaderboardUser("10", "Zoya Ansari", "2600 XP", "🔥 14"));
            return users;
        }

        static class LeaderboardUser {
            String rank, name, xp, streak;
            LeaderboardUser(String r, String n, String x, String s) {
                rank = r; name = n; xp = x; streak = s;
            }
        }

        static class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.VH> {
            List<LeaderboardUser> users;
            LeaderboardAdapter(List<LeaderboardUser> u) { users = u; }
            @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
                return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_leaderboard_user, p, false));
            }
            @Override public void onBindViewHolder(@NonNull VH h, int p) {
                LeaderboardUser u = users.get(p);
                h.rank.setText(u.rank);
                h.name.setText(u.name);
                h.xp.setText(u.xp);
                h.streak.setText(u.streak);
            }
            @Override public int getItemCount() { return users.size(); }
            static class VH extends RecyclerView.ViewHolder {
                TextView rank, name, xp, streak;
                VH(View v) {
                    super(v);
                    rank = v.findViewById(R.id.tv_rank);
                    name = v.findViewById(R.id.tv_name);
                    xp = v.findViewById(R.id.tv_xp);
                    streak = v.findViewById(R.id.tv_streak);
                }
            }
        }
    }


    public static class SearchFragment extends Fragment {
        private HabitAdapter adapter;
        private List<Habit> allHabits;
        private String currentCategory = "All";

        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_discover, container, false);
            
            allHabits = getMockHabits();
            RecyclerView rvPopular = view.findViewById(R.id.rv_popular_habits);
            rvPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));
            
            adapter = new HabitAdapter(new ArrayList<>(allHabits));
            rvPopular.setAdapter(adapter);

            setupFeatures(view);
            
            return view;
        }

        private void setupFeatures(View view) {
            // Search Feature
            EditText etSearch = view.findViewById(R.id.et_search);
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterHabits(s.toString());
                }
                @Override public void afterTextChanged(Editable s) {}
            });

            // Category Selection
            view.findViewById(R.id.cat_fitness).setOnClickListener(v -> toggleCategory("Fitness"));
            view.findViewById(R.id.cat_study).setOnClickListener(v -> toggleCategory("Study"));
            view.findViewById(R.id.cat_meditation).setOnClickListener(v -> toggleCategory("Meditation"));

            // View All
            view.findViewById(R.id.tv_view_all).setOnClickListener(v -> {
                currentCategory = "All";
                etSearch.setText("");
                adapter.updateList(allHabits);
                Toast.makeText(getContext(), "Showing all habits", Toast.LENGTH_SHORT).show();
            });
        }

        private void toggleCategory(String category) {
            if (currentCategory.equals(category)) {
                currentCategory = "All";
            } else {
                currentCategory = category;
            }
            filterHabits("");
            Toast.makeText(getContext(), "Filter: " + currentCategory, Toast.LENGTH_SHORT).show();
        }

        private void filterHabits(String query) {
            List<Habit> filtered = allHabits.stream()
                .filter(h -> (currentCategory.equals("All") || h.getCategory().equalsIgnoreCase(currentCategory)))
                .filter(h -> h.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
            adapter.updateList(filtered);
        }

        private List<Habit> getMockHabits() {
            List<Habit> habits = new ArrayList<>();
            habits.add(new Habit("1", "Morning Run", "Fitness", "Medium", android.R.drawable.ic_menu_compass, "#6366F1"));
            habits.add(new Habit("2", "Read Books", "Study", "Easy", android.R.drawable.ic_menu_edit, "#8B5CF6"));
            habits.add(new Habit("3", "Meditate", "Meditation", "Easy", android.R.drawable.ic_menu_info_details, "#06B6D4"));
            habits.add(new Habit("4", "Cold Shower", "Health", "Hard", android.R.drawable.btn_star_big_on, "#10B981"));
            habits.add(new Habit("5", "Journal", "Productivity", "Easy", android.R.drawable.ic_menu_today, "#F59E0B"));
            habits.add(new Habit("6", "No Sugar", "Health", "Hard", android.R.drawable.btn_star, "#EF4444"));
            habits.add(new Habit("7", "Push-ups", "Fitness", "Medium", android.R.drawable.ic_menu_compass, "#6366F1"));
            habits.add(new Habit("8", "Stretch", "Health", "Easy", android.R.drawable.ic_menu_directions, "#10B981"));
            return habits;
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
            holder.ivIcon.setImageResource(h.getIconRes());

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

            holder.btnAdd.setOnClickListener(v -> 
                Toast.makeText(v.getContext(), h.getTitle() + " added!", Toast.LENGTH_SHORT).show()
            );
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
