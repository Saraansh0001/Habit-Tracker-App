package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FocusTimerFragment extends Fragment {

    private TextView tvTimerDisplay, tvSelectedHabitTimer;
    private ProgressBar timerProgress;
    private MaterialButton btnStartSession;
    private LinearLayout habitSelectionContainer, durationContainer;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private long totalTimeInMillis = 25 * 60 * 1000; // Default 25 min
    private boolean timerRunning;

    private String selectedHabit = "Morning";
    private int selectedDuration = 25;

    private List<Session> recentSessions;
    private RecentSessionsAdapter sessionsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_timer, container, false);

        initViews(view);
        setupHabitSelection();
        setupDurationSelection();
        setupRecentSessions(view);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        btnStartSession.setOnClickListener(v -> {
            if (timerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        view.findViewById(R.id.btn_reset).setOnClickListener(v -> resetTimer());

        updateCountDownText();

        return view;
    }

    private void initViews(View view) {
        tvTimerDisplay = view.findViewById(R.id.tv_timer_display);
        tvSelectedHabitTimer = view.findViewById(R.id.tv_selected_habit_timer);
        timerProgress = view.findViewById(R.id.timer_progress);
        btnStartSession = view.findViewById(R.id.btn_start_session);
        habitSelectionContainer = view.findViewById(R.id.habit_selection_container);
        durationContainer = view.findViewById(R.id.duration_container);
    }

    private void setupHabitSelection() {
        String[] habits = {"Morning", "Read", "Workout", "Water"};
        int[] icons = {R.drawable.ic_meditation, R.drawable.ic_edit, R.drawable.ic_workout, R.drawable.ic_launcher_foreground};
        String[] colors = {"#6366F1", "#8B5CF6", "#F59E0B", "#06B6D4"};

        for (int i = 0; i < habits.length; i++) {
            final String habitName = habits[i];
            View chip = getLayoutInflater().inflate(R.layout.item_habit_chip, habitSelectionContainer, false);
            TextView text = chip.findViewById(R.id.tv_chip_text);
            ImageView icon = chip.findViewById(R.id.iv_chip_icon);
            CardView container = chip.findViewById(R.id.cv_chip_container);

            text.setText(habitName);
            icon.setImageResource(icons[i]);
            
            int color = Color.parseColor(colors[i]);
            container.setCardBackgroundColor(color);
            icon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            text.setTextColor(Color.WHITE);

            chip.setOnClickListener(v -> {
                selectedHabit = habitName;
                tvSelectedHabitTimer.setText(habitName);
            });

            habitSelectionContainer.addView(chip);
        }
    }

    private void setupDurationSelection() {
        int[] durations = {5, 10, 15, 25, 30};
        for (int duration : durations) {
            MaterialButton btn = new MaterialButton(getContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            lp.setMargins(4, 0, 4, 0);
            btn.setLayoutParams(lp);
            btn.setText(duration + "m");
            btn.setCornerRadius(12);
            btn.setPadding(0, 0, 0, 0);
            
            btn.setOnClickListener(v -> {
                selectedDuration = duration;
                totalTimeInMillis = duration * 60 * 1000;
                resetTimer();
            });

            durationContainer.addView(btn);
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis == 0 ? totalTimeInMillis : timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStartSession.setText("Start Session");
                // Add to recent sessions
                addSessionToHistory();
            }
        }.start();

        timerRunning = true;
        btnStartSession.setText("Pause Session");
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        btnStartSession.setText("Resume Session");
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        timeLeftInMillis = totalTimeInMillis;
        updateCountDownText();
        updateProgressBar();
        btnStartSession.setText("Start Session");
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimerDisplay.setText(timeLeftFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) ((timeLeftInMillis * 100) / totalTimeInMillis);
        timerProgress.setProgress(progress);
    }

    private void addSessionToHistory() {
        recentSessions.add(0, new Session(selectedHabit + " Focus", "Today", selectedDuration + " min", "+ " + (selectedDuration * 2) + " XP"));
        sessionsAdapter.notifyItemInserted(0);
    }

    private void setupRecentSessions(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_recent_sessions);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        recentSessions = new ArrayList<>();
        recentSessions.add(new Session("Morning Meditation", "Apr 4, 2026", "25 min", "+50 XP"));
        recentSessions.add(new Session("Read 30 Minutes", "Apr 3, 2026", "30 min", "+60 XP"));
        recentSessions.add(new Session("Workout", "Apr 2, 2026", "25 min", "+50 XP"));

        sessionsAdapter = new RecentSessionsAdapter(recentSessions);
        rv.setAdapter(sessionsAdapter);
    }

    static class Session {
        String title, date, duration, xp;
        Session(String t, String d, String dur, String x) {
            title = t; date = d; duration = dur; xp = x;
        }
    }

    static class RecentSessionsAdapter extends RecyclerView.Adapter<RecentSessionsAdapter.VH> {
        List<Session> sessions;
        RecentSessionsAdapter(List<Session> s) { sessions = s; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_habit_history, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            Session s = sessions.get(p);
            h.title.setText(s.title);
            h.date.setText(s.date);
            h.duration.setText(s.duration);
            h.xp.setText(s.xp);
        }
        @Override public int getItemCount() { return sessions.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView title, date, duration, xp;
            VH(View v) {
                super(v);
                title = v.findViewById(R.id.tv_history_title);
                date = v.findViewById(R.id.tv_history_date);
                duration = v.findViewById(R.id.tv_history_time);
                xp = v.findViewById(R.id.tv_history_xp);
            }
        }
    }
}
