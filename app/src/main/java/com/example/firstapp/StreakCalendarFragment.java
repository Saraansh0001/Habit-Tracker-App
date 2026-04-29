package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StreakCalendarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streak_calendar, container, false);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        setupStreakGrid(view);
        setupHabitStreaks(view);

        return view;
    }

    private void setupStreakGrid(View view) {
        GridLayout grid = view.findViewById(R.id.streak_grid);
        if (grid == null) return;
        
        grid.removeAllViews();
        // 12 weeks * 7 days = 84 blocks
        int totalBlocks = 84;
        
        for (int i = 0; i < totalBlocks; i++) {
            View block = new View(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 40; // dp height
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(4, 4, 4, 4);
            block.setLayoutParams(params);
            
            // Mocking different intensity of streak colors
            int random = (int) (Math.random() * 5);
            if (random == 0) block.setBackgroundColor(0xFFF3E8FF); // Very Light
            else if (random == 1) block.setBackgroundColor(0xFFD8B4FE); // Light
            else if (random == 2) block.setBackgroundColor(0xFFA855F7); // Medium
            else if (random == 3) block.setBackgroundColor(0xFF7E22CE); // Dark
            else block.setBackgroundColor(0xFFE2E8F0); // Empty (Gray)
            
            grid.addView(block);
        }
    }

    private void setupHabitStreaks(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_habit_streaks);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<HabitStreak> streaks = new ArrayList<>();
        streaks.add(new HabitStreak("Morning Meditation", 12, 69, 84, 82, R.drawable.ic_meditation, "#6B3FD4"));
        streaks.add(new HabitStreak("Read 30 Minutes", 8, 50, 84, 60, R.drawable.ic_nav_search, "#8B5CF6"));
        streaks.add(new HabitStreak("Workout", 5, 46, 84, 55, R.drawable.ic_workout, "#EF4444"));
        streaks.add(new HabitStreak("Drink 8 Glasses", 15, 66, 84, 79, R.drawable.ic_health, "#10B981"));
        streaks.add(new HabitStreak("Sleep by 11 PM", 3, 34, 84, 40, R.drawable.ic_sleep, "#F59E0B"));

        rv.setAdapter(new StreakAdapter(streaks));
    }

    static class HabitStreak {
        String title;
        int currentStreak, completedDays, totalDays, consistency;
        int iconRes;
        String color;

        HabitStreak(String t, int cs, int cd, int td, int con, int ir, String c) {
            title = t; currentStreak = cs; completedDays = cd; totalDays = td; consistency = con; iconRes = ir; color = c;
        }
    }

    static class StreakAdapter extends RecyclerView.Adapter<StreakAdapter.VH> {
        List<HabitStreak> items;
        StreakAdapter(List<HabitStreak> i) { items = i; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_habit_streak, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            HabitStreak s = items.get(p);
            h.title.setText(s.title);
            h.info.setText("🔥 " + s.currentStreak + " day streak  •  " + s.completedDays + " / " + s.totalDays + " days");
            h.percent.setText(s.consistency + "%");
            h.progress.setProgress(s.consistency);
            h.icon.setImageResource(s.iconRes);
            
            // Custom tinting
            int color = android.graphics.Color.parseColor(s.color);
            h.icon.setImageTintList(android.content.res.ColorStateList.valueOf(color));
            h.progress.setProgressTintList(android.content.res.ColorStateList.valueOf(color));
        }
        @Override public int getItemCount() { return items.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView title, info, percent;
            ImageView icon;
            android.widget.ProgressBar progress;
            VH(View v) {
                super(v);
                title = v.findViewById(R.id.tv_habit_title);
                info = v.findViewById(R.id.tv_streak_info);
                percent = v.findViewById(R.id.tv_consistency_percent);
                icon = v.findViewById(R.id.iv_habit_icon);
                progress = v.findViewById(R.id.pb_habit_consistency);
            }
        }
    }
}
