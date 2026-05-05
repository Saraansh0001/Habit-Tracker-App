package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.Habit;

import java.util.ArrayList;
import java.util.List;
import com.example.firstapp.network.ApiClient;
import com.example.firstapp.network.StatsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

public class AnalyticsFragment extends Fragment {

    private HistoryAdapter adapter;
    private List<Habit> allArchivedHabits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        
        setupClickListeners(view);
        setupHistory(view);
        loadStats(view);
        
        return view;
    }

    private void loadStats(View view) {
        ApiClient.getService(getContext()).getStats().enqueue(new Callback<StatsResponse>() {
            @Override
            public void onResponse(Call<StatsResponse> call, Response<StatsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StatsResponse stats = response.body();
                    TextView tvBestStreak = view.findViewById(R.id.tv_best_streak);
                    TextView tvCompletionRate = view.findViewById(R.id.tv_completion_rate);
                    TextView tvTotalFocus = view.findViewById(R.id.tv_total_focus);
                    TextView tvInsight = view.findViewById(R.id.tv_insight_text);
                    
                    if (tvBestStreak != null) tvBestStreak.setText(String.valueOf(stats.longestStreak));
                    
                    if (tvCompletionRate != null) {
                        int rate = stats.totalHabits > 0 ? (stats.completedToday * 100 / stats.totalHabits) : 0;
                        tvCompletionRate.setText(rate + "%");
                    }

                    if (tvTotalFocus != null) {
                        tvTotalFocus.setText(String.valueOf(stats.totalFocusMinutes));
                    }

                    if (tvInsight != null) {
                        if (stats.completedToday > 0) {
                            tvInsight.setText("Great job! You've completed " + stats.completedToday + " habits today. Keep the momentum going!");
                        } else {
                            tvInsight.setText("You haven't completed any habits today. Start small to build your streak!");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StatsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load stats", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupHistory(View view) {
        RecyclerView rvHistory = view.findViewById(R.id.rv_history);
        if (rvHistory != null) {
            rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            HabitRepository repository = new HabitRepository(requireContext());
            allArchivedHabits = repository.getArchivedHabits();
            adapter = new HistoryAdapter(new ArrayList<>(allArchivedHabits));
            rvHistory.setAdapter(adapter);
        }
    }

    private void setupClickListeners(View view) {
        View cardStreak = view.findViewById(R.id.card_streak);
        if (cardStreak != null) {
            cardStreak.setOnClickListener(v -> {
                // Streak details
            });
        }

        // Add more click listeners as needed for mock items
    }

}