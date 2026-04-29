package com.example.firstapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    private String currentCategory = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        
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
        view.findViewById(R.id.cat_fitness).setOnClickListener(v -> toggleCategory("Fitness"));
        view.findViewById(R.id.cat_study).setOnClickListener(v -> toggleCategory("Study"));
        view.findViewById(R.id.cat_meditation).setOnClickListener(v -> toggleCategory("Meditation"));

        // View All
        view.findViewById(R.id.tv_view_all).setOnClickListener(v -> {
            currentCategory = "All";
            etSearch.setText("");
            adapter.updateList(allHabits);
        });
    }

    private void toggleCategory(String category) {
        if (currentCategory.equals(category)) {
            currentCategory = "All";
        } else {
            currentCategory = category;
        }
        filterHabits("");
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
        // Using constructor: Habit(String title, String category, String difficulty, String color, int iconRes)
        habits.add(new Habit("Morning Run", "Fitness", "Medium", "#6366F1", R.drawable.ic_nav_arena));
        habits.add(new Habit("Read Books", "Study", "Easy", "#8B5CF6", R.drawable.ic_nav_search));
        habits.add(new Habit("Meditate", "Meditation", "Easy", "#06B6D4", R.drawable.ic_nav_profile));
        habits.add(new Habit("Cold Shower", "Health", "Hard", "#10B981", R.drawable.ic_bolt));
        habits.add(new Habit("Journal", "Productivity", "Easy", "#F59E0B", R.drawable.ic_nav_home));
        habits.add(new Habit("No Sugar", "Health", "Hard", "#EF4444", R.drawable.ic_bolt));
        habits.add(new Habit("Push-ups", "Fitness", "Medium", "#6366F1", R.drawable.ic_nav_arena));
        habits.add(new Habit("Stretch", "Health", "Easy", "#10B981", R.drawable.ic_bolt));
        return habits;
    }
}
