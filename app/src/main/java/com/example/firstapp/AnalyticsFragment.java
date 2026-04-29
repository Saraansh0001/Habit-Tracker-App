package com.example.firstapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.Habit;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsFragment extends Fragment {

    private HistoryAdapter adapter;
    private List<Habit> allArchivedHabits;
    private String currentCategory = "All";
    private String currentQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        
        setupClickListeners(view);
        setupHistory(view);
        setupSearchAndFilters(view);
        
        return view;
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

    private void setupSearchAndFilters(View view) {
        EditText etSearch = view.findViewById(R.id.et_search_history);

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    currentQuery = s.toString();
                    filterHistory();
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }

        // Setup Category Click Listeners (matching Discover style)
        setupCategoryButton(view, R.id.cat_history_all, "All");
        setupCategoryButton(view, R.id.cat_history_fitness, "Fitness");
        setupCategoryButton(view, R.id.cat_history_health, "Health");
        setupCategoryButton(view, R.id.cat_history_productivity, "Productivity");
    }

    private void setupCategoryButton(View view, int id, String category) {
        View catView = view.findViewById(id);
        if (catView != null) {
            catView.setOnClickListener(v -> {
                currentCategory = category;
                filterHistory();
            });
        }
    }

    private void filterHistory() {
        if (allArchivedHabits == null || adapter == null) return;

        List<Habit> filtered = allArchivedHabits.stream()
                .filter(h -> (currentCategory.equals("All") || h.getCategory().equalsIgnoreCase(currentCategory)))
                .filter(h -> h.getTitle().toLowerCase().contains(currentQuery.toLowerCase()))
                .collect(Collectors.toList());
        adapter.setItems(filtered);
    }

    private void setupClickListeners(View view) {
        View cardStreak = view.findViewById(R.id.card_streak);
        if (cardStreak != null) {
            cardStreak.setOnClickListener(v -> {
                // Streak details
            });
        }
        
        View tvThisWeek = view.findViewById(R.id.tv_this_week);
        if (tvThisWeek != null) {
            tvThisWeek.setOnClickListener(v -> {
                // Weekly range selector
            });
        }

        // Add more click listeners as needed for mock items
    }

}