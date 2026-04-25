package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firstapp.adapters.HomeHabitAdapter;
import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.Habit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProgressBar pbMain;
    private TextView tvPercentage;
    private RecyclerView rvHabits;
    private HomeHabitAdapter habitAdapter;
    private HabitRepository habitRepository;
    private List<Habit> userHabits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        habitRepository = new HabitRepository(requireContext());
        userHabits = habitRepository.getUserHabits();

        pbMain = view.findViewById(R.id.pb_main_progress);
        tvPercentage = view.findViewById(R.id.tv_progress_percentage);
        rvHabits = view.findViewById(R.id.rv_habits_home);

        setupRecyclerView();
        updateOverallProgress();

        view.findViewById(R.id.tv_all_features).setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_analytics);
            }
        });

        view.findViewById(R.id.tv_see_all_habits).setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_search);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_habit);
        fab.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_search);
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        habitAdapter = new HomeHabitAdapter(userHabits, new HomeHabitAdapter.OnHabitClickListener() {
            @Override
            public void onHabitClick(Habit habit) {
                boolean newState = !habit.isCompleted();
                habitRepository.updateHabitCompletion(habit.getId(), newState);
                
                // Refresh local list and UI
                userHabits = habitRepository.getUserHabits();
                updateAdapterData();
                updateOverallProgress();
            }

            @Override
            public void onHabitDetailClick(Habit habit) {
                android.content.Intent intent = new android.content.Intent(requireContext(), HabitDetailActivity.class);
                intent.putExtra(HabitDetailActivity.EXTRA_HABIT, habit);
                startActivity(intent);
            }
        });
        rvHabits.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHabits.setAdapter(habitAdapter);
    }

    private void updateAdapterData() {
        habitAdapter.updateList(userHabits);
    }

    private void updateOverallProgress() {
        int count = 0;
        for (Habit h : userHabits) {
            if (h.isCompleted()) count++;
        }

        int total = userHabits.size();
        int progress = total > 0 ? (count * 100) / total : 0;

        pbMain.setProgress(progress);
        tvPercentage.setText(getString(R.string.percentage_format, progress));
    }
}