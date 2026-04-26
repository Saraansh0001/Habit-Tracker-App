package com.example.firstapp;

import android.content.Intent;
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

    public static class CreateChallengeFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);
            
            view.findViewById(R.id.btn_back).setOnClickListener(v -> {
                if (getActivity() != null) getActivity().onBackPressed();
            });

            Button btnCreate = view.findViewById(R.id.btn_create_final);
            btnCreate.setOnClickListener(v -> {
                EditText etName = view.findViewById(R.id.et_challenge_name);
                String name = etName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), R.string.error_challenge_name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.challenge_created, name), Toast.LENGTH_LONG).show();
                    if (getActivity() != null) getActivity().onBackPressed();
                }
            });

            return view;
        }
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

    public static class ProfileFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_profile, container, false);
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
                Toast.makeText(getContext(), R.string.showing_all_habits, Toast.LENGTH_SHORT).show();
            });
        }

        private void toggleCategory(String category) {
            if (currentCategory.equals(category)) {
                currentCategory = "All";
            } else {
                currentCategory = category;
            }
            filterHabits("");
            Toast.makeText(getContext(), getString(R.string.filter_message, currentCategory), Toast.LENGTH_SHORT).show();
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
            habits.add(new Habit("6", "Workout", "Fitness", "Hard", android.R.drawable.ic_lock_power_off, "#EF4444"));
            return habits;
        }
    }

    public static class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.VH> {
        private List<Habit> habits;
        public HabitAdapter(List<Habit> habits) { this.habits = habits; }
        public void updateList(List<Habit> newList) {
            this.habits = newList;
            notifyDataSetChanged();
        }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_habit_discover, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            Habit hb = habits.get(p);
            h.title.setText(hb.getTitle());
            h.category.setText(hb.getCategory());
            h.icon.setImageResource(hb.getIconRes());
            h.container.setCardBackgroundColor(android.graphics.Color.parseColor(hb.getColor()));
            
            h.itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), hb.getTitle() + " selected", Toast.LENGTH_SHORT).show();
            });
        }
        @Override public int getItemCount() { return habits.size(); }
        public static class VH extends RecyclerView.ViewHolder {
            TextView title, category;
            ImageView icon;
            CardView container;
            public VH(View v) {
                super(v);
                title = v.findViewById(R.id.tv_habit_title);
                category = v.findViewById(R.id.tv_habit_category);
                icon = v.findViewById(R.id.iv_habit_icon);
                container = v.findViewById(R.id.cv_habit_container);
            }
        }
    }
}
