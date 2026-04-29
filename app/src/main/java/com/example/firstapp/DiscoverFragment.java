package com.example.firstapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.models.Habit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscoverFragment extends Fragment {
    private NavigationFragments.HabitAdapter adapter;
    private List<Habit> allHabits;
    private String currentCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        
        currentCategory = getString(R.string.category_all);
        allHabits = getMockHabits();
        RecyclerView rvPopular = view.findViewById(R.id.rv_popular_habits);
        rvPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        adapter = new NavigationFragments.HabitAdapter(new ArrayList<>(allHabits));
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
        habits.add(new Habit(getString(R.string.habit_run), getString(R.string.cat_fitness), getString(R.string.diff_medium), "#6366F1", R.drawable.ic_nav_arena));
        habits.add(new Habit(getString(R.string.habit_read), getString(R.string.cat_study), getString(R.string.diff_easy), "#8B5CF6", R.drawable.ic_nav_search));
        habits.add(new Habit(getString(R.string.habit_meditation_simple), getString(R.string.cat_meditation), getString(R.string.diff_easy), "#06B6D4", R.drawable.ic_nav_profile));
        habits.add(new Habit(getString(R.string.habit_cold_shower), getString(R.string.cat_health), getString(R.string.diff_hard), "#10B981", R.drawable.ic_bolt));
        habits.add(new Habit(getString(R.string.habit_journal), getString(R.string.cat_productivity), getString(R.string.diff_easy), "#F59E0B", R.drawable.ic_nav_home));
        habits.add(new Habit(getString(R.string.habit_no_sugar), getString(R.string.cat_health), getString(R.string.diff_hard), "#EF4444", R.drawable.ic_bolt));
        habits.add(new Habit(getString(R.string.habit_pushups), getString(R.string.cat_fitness), getString(R.string.diff_medium), "#6366F1", R.drawable.ic_nav_arena));
        habits.add(new Habit(getString(R.string.habit_stretch), getString(R.string.cat_health), getString(R.string.diff_easy), "#10B981", R.drawable.ic_bolt));
        return habits;
    }
}
