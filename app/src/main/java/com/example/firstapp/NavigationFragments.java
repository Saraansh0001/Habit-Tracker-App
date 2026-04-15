package com.example.firstapp;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationFragments {

    public static class BaseFragment extends Fragment {
        protected void showToast(String message) {
            if (getContext() != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class AnalyticsFragment extends BaseFragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_analytics, container, false);
            setupAnalyticsData(view);
            animateEntrance(view);
            return view;
        }

        private void setupAnalyticsData(View view) {
            // Header clicks
            View cardStreak = view.findViewById(R.id.card_streak);
            if (cardStreak != null) {
                cardStreak.setOnClickListener(v -> showToast("Keep it up! 12 days and counting."));
            }
            View tvThisWeek = view.findViewById(R.id.tv_this_week);
            if (tvThisWeek != null) {
                tvThisWeek.setOnClickListener(v -> showToast("Weekly Stats: Apr 8 - Apr 14"));
            }

            // Populate Ripple Chain Insight
            LinearLayout chainsContainer = view.findViewById(R.id.ll_chains_container);
            if (chainsContainer != null) {
                // Find indices of view_insight_chain includes
                List<View> chainViews = new ArrayList<>();
                for (int i = 0; i < chainsContainer.getChildCount(); i++) {
                    View child = chainsContainer.getChildAt(i);
                    if (child.findViewById(R.id.tv_chain_title) != null) {
                        chainViews.add(child);
                    }
                }

                if (!chainViews.isEmpty()) {
                    setupChain(chainViews.get(0), "Your Power Chain", 
                        new ChainNode("Meditate", R.drawable.ic_meditation, ""),
                        new ChainNode("Workout", R.drawable.ic_workout, "78%"),
                        new ChainNode("Read", R.drawable.ic_reading, "64%"),
                        new ChainNode("Sleep Early", R.drawable.ic_sleep, "82%"),
                        "When you complete Meditate, you have a 78% chance of also doing Workout");
                }
                
                if (chainViews.size() >= 2) {
                    setupChain(chainViews.get(1), "Body Wellness Loop",
                        new ChainNode("Hydrate", R.drawable.ic_health, ""),
                        new ChainNode("Workout", R.drawable.ic_workout, "71%"),
                        new ChainNode("Stretch", R.drawable.ic_bolt, "82%"),
                        null,
                        "When you complete Hydrate, you have a 71% chance of also doing Workout");
                }
            }

            // Populate Text Insights
            LinearLayout insightsContainer = view.findViewById(R.id.ll_insights_container);
            if (insightsContainer != null) {
                LinearLayout list = null;
                for (int i = 0; i < insightsContainer.getChildCount(); i++) {
                    View child = insightsContainer.getChildAt(i);
                    if (child instanceof LinearLayout && child.getId() == View.NO_ID) {
                        list = (LinearLayout) child;
                        break;
                    }
                }

                if (list != null) {
                    int insightIndex = 0;
                    for (int i = 0; i < list.getChildCount(); i++) {
                        View child = list.getChildAt(i);
                        if (child.findViewById(R.id.tv_insight_text) != null) {
                            if (insightIndex == 0) setupInsightItem(child, R.drawable.ic_workout, "#F59E0B", "You perform best on Mondays.");
                            else if (insightIndex == 1) setupInsightItem(child, R.drawable.ic_sleep, "#8B5CF6", "You miss habits mostly after 9 PM.");
                            else if (insightIndex == 2) setupInsightItem(child, R.drawable.ic_meditation, "#10B981", "Meditation streak is your strongest.");
                            insightIndex++;
                        }
                    }
                }
            }
        }

        private void setupChain(View view, String title, ChainNode n1, ChainNode n2, ChainNode n3, ChainNode n4, String desc) {
            if (view == null) return;
            TextView tvTitle = view.findViewById(R.id.tv_chain_title);
            TextView tvDesc = view.findViewById(R.id.tv_chain_desc);
            if (tvTitle != null) tvTitle.setText(title);
            if (tvDesc != null) tvDesc.setText(desc);
            
            LinearLayout row = null;
            if (view instanceof LinearLayout) {
                LinearLayout container = (LinearLayout) view;
                for (int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    if (child instanceof LinearLayout && ((LinearLayout) child).getOrientation() == LinearLayout.HORIZONTAL) {
                        row = (LinearLayout) child;
                        break;
                    }
                }
            }
            
            if (row != null) {
                if (row.getChildCount() > 0) fillNode(row.getChildAt(0), n1);
                if (row.getChildCount() > 2) fillNode(row.getChildAt(2), n2);
                if (row.getChildCount() > 4) fillNode(row.getChildAt(4), n3);
                if (row.getChildCount() > 6) {
                    if (n4 != null) {
                        fillNode(row.getChildAt(6), n4);
                    } else {
                        if (row.getChildCount() > 5) row.getChildAt(5).setVisibility(View.GONE);
                        row.getChildAt(6).setVisibility(View.GONE);
                    }
                }
            }
        }

        private void fillNode(View view, ChainNode node) {
            if (view == null || node == null) return;
            ImageView iv = view.findViewById(R.id.iv_chain_icon);
            TextView tvLabel = view.findViewById(R.id.tv_chain_label);
            TextView tvPercentage = view.findViewById(R.id.tv_chain_percent);
            
            if (iv != null) iv.setImageResource(node.icon);
            if (tvLabel != null) tvLabel.setText(node.label);
            if (tvPercentage != null) {
                if (node.percent.isEmpty()) {
                    tvPercentage.setVisibility(View.INVISIBLE);
                } else {
                    tvPercentage.setText(node.percent);
                    tvPercentage.setVisibility(View.VISIBLE);
                }
            }
        }

        private void setupInsightItem(View view, int icon, String color, String text) {
            ImageView iv = view.findViewById(R.id.iv_insight_icon);
            TextView tv = view.findViewById(R.id.tv_insight_text);
            if (iv != null) {
                iv.setImageResource(icon);
                iv.setImageTintList(ColorStateList.valueOf(Color.parseColor(color)));
            }
            if (tv != null) tv.setText(text);
            view.setOnClickListener(v -> showToast(text));
        }

        private static class ChainNode {
            String label, percent; int icon;
            ChainNode(String l, int i, String p) { label = l; icon = i; percent = p; }
        }

        private void animateEntrance(View view) {
            ProgressBar pb = view.findViewById(R.id.pb_completion);
            TextView tvPercent = view.findViewById(R.id.tv_completion_percent);
            if (pb != null && tvPercent != null) {
                ValueAnimator animator = ValueAnimator.ofInt(0, 72);
                animator.setDuration(1200);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.addUpdateListener(animation -> {
                    int progress = (int) animation.getAnimatedValue();
                    pb.setProgress(progress);
                    tvPercent.setText(getString(R.string.percentage_format, progress));
                });
                animator.start();
            }

            LinearLayout barChart = view.findViewById(R.id.ll_bar_chart);
            if (barChart != null) {
                int[] mockHeights = {80, 55, 90, 45, 65, 30, 60}; // in dp
                String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                
                for (int i = 0; i < barChart.getChildCount(); i++) {
                    View item = barChart.getChildAt(i);
                    View bar = item.findViewById(R.id.bar_view);
                    TextView tvDay = item.findViewById(R.id.tv_day);
                    
                    if (bar != null && i < mockHeights.length) {
                        if (tvDay != null) tvDay.setText(days[i]);
                        final int targetHeight = (int) (mockHeights[i] * getResources().getDisplayMetrics().density);
                        
                        ValueAnimator barAnimator = ValueAnimator.ofInt(0, targetHeight);
                        barAnimator.setDuration(800);
                        barAnimator.setStartDelay(100 + (i * 100L));
                        barAnimator.setInterpolator(new DecelerateInterpolator());
                        barAnimator.addUpdateListener(animation -> {
                            ViewGroup.LayoutParams params = bar.getLayoutParams();
                            params.height = (int) animation.getAnimatedValue();
                            bar.setLayoutParams(params);
                        });
                        barAnimator.start();
                    }
                }
            }
        }
    }

    public static class CreateChallengeFragment extends BaseFragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);
            
            View btnBack = view.findViewById(R.id.btn_back);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    if (getActivity() != null) {
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                });
            }

            Button btnCreate = view.findViewById(R.id.btn_create_final);
            if (btnCreate != null) {
                btnCreate.setOnClickListener(v -> {
                    EditText etName = view.findViewById(R.id.et_challenge_name);
                    if (etName != null) {
                        String name = etName.getText().toString();
                        if (name.isEmpty()) {
                            showToast(getString(R.string.error_challenge_name));
                        } else {
                            showToast(getString(R.string.challenge_created, name));
                            if (getActivity() != null) {
                                getActivity().getOnBackPressedDispatcher().onBackPressed();
                            }
                        }
                    }
                });
            }

            return view;
        }
    }

    public static class LeaderboardFragment extends BaseFragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
            
            View btnBack = view.findViewById(R.id.btn_back);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    if (getActivity() != null) {
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                });
            }

            RecyclerView rv = view.findViewById(R.id.rv_leaderboard);
            if (rv != null) {
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(new LeaderboardAdapter(getMockLeaderboard()));
            }

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
            private final List<LeaderboardUser> users;
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

    public static class SearchFragment extends BaseFragment {
        private HabitAdapter adapter;
        private List<Habit> allHabits;

        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_discover, container, false);
            
            allHabits = getMockHabits();
            RecyclerView rv = view.findViewById(R.id.rv_popular_habits);
            if (rv != null) {
                adapter = new HabitAdapter(allHabits);
                rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rv.setAdapter(adapter);
            }

            EditText etSearch = view.findViewById(R.id.et_search);
            if (etSearch != null) {
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filterHabits(s.toString());
                    }
                    @Override public void afterTextChanged(Editable s) {}
                });
            }

            setupCategoryClicks(view);

            return view;
        }

        private void filterHabits(String query) {
            List<Habit> filtered = allHabits.stream()
                    .filter(h -> h.title.toLowerCase().contains(query.toLowerCase()) || 
                                h.category.toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            if (adapter != null) {
                adapter.updateList(filtered);
            }
        }

        private void setupCategoryClicks(View view) {
            View catFitness = view.findViewById(R.id.cat_fitness);
            if (catFitness != null) catFitness.setOnClickListener(v -> filterHabits("Fitness"));
            
            View catStudy = view.findViewById(R.id.cat_study);
            if (catStudy != null) catStudy.setOnClickListener(v -> filterHabits("Study"));
            
            View catMeditation = view.findViewById(R.id.cat_meditation);
            if (catMeditation != null) catMeditation.setOnClickListener(v -> filterHabits("Meditation"));
            
            View viewAll = view.findViewById(R.id.tv_view_all);
            if (viewAll != null) viewAll.setOnClickListener(v -> filterHabits(""));
        }

        private List<Habit> getMockHabits() {
            List<Habit> list = new ArrayList<>();
            list.add(new Habit("Morning Run", "Fitness", "Medium", R.drawable.ic_workout));
            list.add(new Habit("Read 20 Pages", "Study", "Easy", R.drawable.ic_reading));
            list.add(new Habit("Deep Meditation", "Meditation", "Hard", R.drawable.ic_meditation));
            list.add(new Habit("Stay Hydrated", "Health", "Easy", R.drawable.ic_health));
            list.add(new Habit("No Sugar", "Diet", "Hard", R.drawable.ic_bolt));
            list.add(new Habit("Early Sleep", "Sleep", "Medium", R.drawable.ic_sleep));
            return list;
        }

        static class Habit {
            String title, category, difficulty; int icon;
            Habit(String t, String c, String d, int i) { title = t; category = c; difficulty = d; icon = i; }
        }

        static class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.VH> {
            private final List<Habit> habits;
            HabitAdapter(List<Habit> h) { habits = new ArrayList<>(h); }
            void updateList(List<Habit> newList) {
                habits.clear();
                habits.addAll(newList);
                notifyDataSetChanged();
            }
            @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
                return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_habit_discover, p, false));
            }
            @Override public void onBindViewHolder(@NonNull VH h, int p) {
                Habit habit = habits.get(p);
                h.title.setText(habit.title);
                h.category.setText(habit.category);
                h.difficulty.setText(habit.difficulty);
                h.icon.setImageResource(habit.icon);
                h.btnAdd.setOnClickListener(v -> 
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.habit_added, habit.title), Toast.LENGTH_SHORT).show());
            }
            @Override public int getItemCount() { return habits.size(); }
            static class VH extends RecyclerView.ViewHolder {
                TextView title, category, difficulty; ImageView icon; Button btnAdd;
                VH(View v) {
                    super(v);
                    title = v.findViewById(R.id.tv_habit_title);
                    category = v.findViewById(R.id.tv_habit_category);
                    difficulty = v.findViewById(R.id.tv_difficulty);
                    icon = v.findViewById(R.id.iv_habit_icon);
                    btnAdd = v.findViewById(R.id.btn_add_habit);
                }
            }
        }
    }

    public static class ProfileFragment extends Fragment {
        @Nullable @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
            TextView textView = view.findViewById(R.id.placeholder_text);
            if (textView != null) {
                textView.setText(R.string.nav_profile);
            }
            return view;
        }
    }
}