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
import android.widget.LinearLayout;
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
import com.example.firstapp.models.ProfileFeature;

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
            users.add(new LeaderboardUser("4", "Aayush Rathore", getString(R.string.xp_format, "3890"), getString(R.string.streak_format, 28)));
            users.add(new LeaderboardUser("5", "Shejal Kushwaha", getString(R.string.xp_format, "3650"), getString(R.string.streak_format, 25)));
            users.add(new LeaderboardUser("6", "Priya Das", getString(R.string.xp_format, "3420"), getString(R.string.streak_format, 22)));
            users.add(new LeaderboardUser("7", "Romi", getString(R.string.xp_format, "3200"), getString(R.string.streak_format, 20)));
            users.add(new LeaderboardUser("8", "Sachin Singh", getString(R.string.xp_format, "2980"), getString(R.string.streak_format, 18)));
            users.add(new LeaderboardUser("9", "Aryan", getString(R.string.xp_format, "2750"), getString(R.string.streak_format, 15)));
            users.add(new LeaderboardUser("10", "Zoya Ansari", getString(R.string.xp_format, "2600"), getString(R.string.streak_format, 14)));
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

    public static class FeaturesFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_features, container, false);
            
            View btnBack = view.findViewById(R.id.btn_back);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    if (getActivity() != null) getActivity().onBackPressed();
                });
            }

            setupFeatures(view, inflater);

            return view;
        }

        private void setupFeatures(View view, LayoutInflater inflater) {
            LinearLayout container = view.findViewById(R.id.features_container);
            if (container == null) return;
            container.removeAllViews();

            List<ProfileFeature> features = new ArrayList<>();
            features.add(new ProfileFeature(getString(R.string.feature_streak_calendar_title), getString(R.string.feature_streak_calendar_desc), R.drawable.ic_nav_home, "#F3F0FF"));
            features.add(new ProfileFeature(getString(R.string.feature_focus_timer_title), getString(R.string.feature_focus_timer_desc), R.drawable.ic_bolt, "#FFF7ED"));
            features.add(new ProfileFeature(getString(R.string.feature_weekly_goals_title), getString(R.string.feature_weekly_goals_desc), R.drawable.ic_bolt, "#F0FDF4"));
            features.add(new ProfileFeature(getString(R.string.feature_daily_journal_title), getString(R.string.feature_daily_journal_desc), R.drawable.ic_bolt, "#EEF2FF"));
            features.add(new ProfileFeature(getString(R.string.feature_mood_tracker_title), getString(R.string.feature_mood_tracker_desc), R.drawable.ic_bolt, "#FFF1F2"));
            features.add(new ProfileFeature(getString(R.string.feature_friends_title), getString(R.string.feature_friends_desc), R.drawable.ic_bolt, "#F0FDFA"));
            features.add(new ProfileFeature(getString(R.string.feature_rewards_title), getString(R.string.feature_rewards_desc), R.drawable.ic_badge_1, "#FEFCE8"));
            features.add(new ProfileFeature(getString(R.string.feature_weekly_report_title), getString(R.string.feature_weekly_report_desc), R.drawable.ic_nav_analytics, "#FDF2F8"));

            for (ProfileFeature feature : features) {
                try {
                    View itemView = inflater.inflate(R.layout.item_profile_feature, container, false);
                    
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
                        if (getString(R.string.feature_streak_calendar_title).equals(feature.getTitle())) {
                            loadFragment(new StreakCalendarFragment());
                        } else if (getString(R.string.feature_focus_timer_title).equals(feature.getTitle())) {
                            loadFragment(new FocusTimerFragment());
                        } else if (getString(R.string.feature_daily_journal_title).equals(feature.getTitle())) {
                            loadFragment(new DailyJournalFragment());
                        } else if (getString(R.string.feature_weekly_goals_title).equals(feature.getTitle())) {
                            loadFragment(new WeeklyGoalsFragment());
                        } else {
                            Toast.makeText(getContext(), getString(R.string.action_clicked_format, feature.getTitle()), Toast.LENGTH_SHORT).show();
                        }
                    });
                    container.addView(itemView);
                    
                    // Add a small spacer
                    View spacer = new View(getContext());
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 16));
                    container.addView(spacer);
                } catch (Exception e) {}
            }
        }

        private void loadFragment(Fragment fragment) {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(fragment);
            }
        }
    }

    public static class SearchFragment extends Fragment {
        private HabitAdapter adapter;
        private List<Habit> allHabits;
        private String currentCategory;

        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_discover, container, false);
            
            currentCategory = getString(R.string.category_all);
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
            view.findViewById(R.id.cat_fitness).setOnClickListener(v -> toggleCategory(getString(R.string.cat_fitness)));
            view.findViewById(R.id.cat_study).setOnClickListener(v -> toggleCategory(getString(R.string.cat_study)));
            view.findViewById(R.id.cat_meditation).setOnClickListener(v -> toggleCategory(getString(R.string.cat_meditation)));

            // View All
            view.findViewById(R.id.tv_view_all).setOnClickListener(v -> {
                currentCategory = getString(R.string.category_all);
                etSearch.setText("");
                adapter.updateList(allHabits);
                Toast.makeText(getContext(), R.string.showing_all_habits, Toast.LENGTH_SHORT).show();
            });
        }

        private void toggleCategory(String category) {
            if (currentCategory.equals(category)) {
                currentCategory = getString(R.string.category_all);
            } else {
                currentCategory = category;
            }
            filterHabits("");
            Toast.makeText(getContext(), getString(R.string.filter_message, currentCategory), Toast.LENGTH_SHORT).show();
        }

        private void filterHabits(String query) {
            List<Habit> filtered = allHabits.stream()
                .filter(h -> (currentCategory.equals(getString(R.string.category_all)) || h.getCategory().equalsIgnoreCase(currentCategory)))
                .filter(h -> h.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
            adapter.updateList(filtered);
        }

        private List<Habit> getMockHabits() {
            List<Habit> habits = new ArrayList<>();
            habits.add(new Habit("1", getString(R.string.habit_run), getString(R.string.cat_fitness), getString(R.string.diff_medium), android.R.drawable.ic_menu_compass, "#6366F1"));
            habits.add(new Habit("2", getString(R.string.habit_read), getString(R.string.cat_study), getString(R.string.diff_easy), android.R.drawable.ic_menu_edit, "#8B5CF6"));
            habits.add(new Habit("3", getString(R.string.habit_meditation_simple), getString(R.string.cat_meditation), getString(R.string.diff_easy), android.R.drawable.ic_menu_info_details, "#06B6D4"));
            habits.add(new Habit("4", getString(R.string.habit_cold_shower), getString(R.string.cat_health), getString(R.string.diff_hard), android.R.drawable.btn_star_big_on, "#10B981"));
            habits.add(new Habit("5", getString(R.string.habit_journal), getString(R.string.cat_productivity), getString(R.string.diff_easy), android.R.drawable.ic_menu_today, "#F59E0B"));
            habits.add(new Habit("6", getString(R.string.habit_no_sugar), getString(R.string.cat_health), getString(R.string.diff_hard), android.R.drawable.btn_star, "#EF4444"));
            habits.add(new Habit("7", getString(R.string.habit_pushups), getString(R.string.cat_fitness), getString(R.string.diff_medium), android.R.drawable.ic_menu_compass, "#6366F1"));
            habits.add(new Habit("8", getString(R.string.habit_stretch), getString(R.string.cat_health), getString(R.string.diff_easy), android.R.drawable.ic_menu_directions, "#10B981"));
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
            String easy = holder.itemView.getContext().getString(R.string.diff_easy);
            String hard = holder.itemView.getContext().getString(R.string.diff_hard);
            
            if (h.getDifficulty().equals(easy)) {
                diffBg = holder.itemView.getContext().getColor(R.color.difficulty_easy_bg);
                diffText = holder.itemView.getContext().getColor(R.color.difficulty_easy_text);
            } else if (h.getDifficulty().equals(hard)) {
                diffBg = holder.itemView.getContext().getColor(R.color.difficulty_hard_bg);
                diffText = holder.itemView.getContext().getColor(R.color.difficulty_hard_text);
            } else { // Medium
                diffBg = holder.itemView.getContext().getColor(R.color.difficulty_medium_bg);
                diffText = holder.itemView.getContext().getColor(R.color.difficulty_medium_text);
            }
            holder.tvDifficulty.setBackgroundTintList(ColorStateList.valueOf(diffBg));
            holder.tvDifficulty.setTextColor(diffText);

            holder.btnAdd.setOnClickListener(v -> 
                Toast.makeText(v.getContext(), v.getContext().getString(R.string.habit_added, h.getTitle()), Toast.LENGTH_SHORT).show()
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
