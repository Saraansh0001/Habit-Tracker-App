package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeeklyGoalsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_goals, container, false);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        setupHabitGoals(view);

        return view;
    }

    private void setupHabitGoals(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_habit_goals);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<HabitGoal> goals = new ArrayList<>();
        goals.add(new HabitGoal("Morning Meditation", 1, 5, 20, R.drawable.ic_meditation, "#6B3FD4"));
        goals.add(new HabitGoal("Read 30 Minutes", 0, 5, 0, R.drawable.ic_nav_search, "#8B5CF6"));
        goals.add(new HabitGoal("Workout", 0, 5, 0, R.drawable.ic_workout, "#EF4444"));
        goals.add(new HabitGoal("Drink 8 Glasses", 0, 7, 0, R.drawable.ic_health, "#10B981"));
        goals.add(new HabitGoal("Sleep by 11 PM", 0, 7, 0, R.drawable.ic_sleep, "#F59E0B"));

        rv.setAdapter(new GoalsAdapter(goals));
    }

    static class HabitGoal {
        String title;
        int completed, target, percent;
        int iconRes;
        String color;

        HabitGoal(String t, int c, int tg, int p, int ir, String col) {
            title = t; completed = c; target = tg; percent = p; iconRes = ir; color = col;
        }
    }

    static class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.VH> {
        List<HabitGoal> items;
        GoalsAdapter(List<HabitGoal> i) { items = i; }
        
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_habit_goal, p, false));
        }

        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            HabitGoal g = items.get(p);
            h.title.setText(g.title);
            h.info.setText(String.format("%d of %dx this week", g.completed, g.target));
            h.percentLabel.setText(String.format("%d%% of weekly goal", g.percent));
            h.progress.setProgress(g.percent);
            h.icon.setImageResource(g.iconRes);
            h.timesPerWeek.setText(String.format("Times per week: %dx", g.target));

            int color = android.graphics.Color.parseColor(g.color);
            h.icon.setImageTintList(android.content.res.ColorStateList.valueOf(color));
            h.progress.setProgressTintList(android.content.res.ColorStateList.valueOf(color));
            
            if (g.completed >= g.target) {
                h.btnStatus.setText("Done");
                h.btnStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF6366F1));
            } else {
                h.btnStatus.setText("Edit goal");
                h.btnStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFF1F5F9));
                h.btnStatus.setTextColor(0xFF64748B);
            }
        }

        @Override public int getItemCount() { return items.size(); }
        
        static class VH extends RecyclerView.ViewHolder {
            TextView title, info, percentLabel, timesPerWeek;
            ImageView icon;
            ProgressBar progress;
            TextView btnStatus;
            
            VH(View v) {
                super(v);
                title = v.findViewById(R.id.tv_habit_title);
                info = v.findViewById(R.id.tv_habit_info);
                percentLabel = v.findViewById(R.id.tv_percent_label);
                timesPerWeek = v.findViewById(R.id.tv_times_per_week);
                icon = v.findViewById(R.id.iv_habit_icon);
                progress = v.findViewById(R.id.pb_habit_goal);
                btnStatus = v.findViewById(R.id.btn_goal_status);
            }
        }
    }
}
