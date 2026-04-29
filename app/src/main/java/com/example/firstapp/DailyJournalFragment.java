package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DailyJournalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_journal, container, false);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_save_entry).setOnClickListener(v -> {
            Toast.makeText(getContext(), R.string.journal_saved, Toast.LENGTH_SHORT).show();
        });

        setupPastEntries(view);

        return view;
    }

    private void setupPastEntries(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_past_entries);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<FocusTimerFragment.Session> pastEntries = new ArrayList<>();
        pastEntries.add(new FocusTimerFragment.Session("Completed morning meditation", "Apr 4, 2026", getString(R.string.mood_format, "😊"), ""));
        pastEntries.add(new FocusTimerFragment.Session("Had a super productive day", "Apr 3, 2026", getString(R.string.mood_format, "🤩"), ""));
        pastEntries.add(new FocusTimerFragment.Session("Drank all 8 glasses of water", "Apr 2, 2026", getString(R.string.mood_format, "🙂"), ""));

        rv.setAdapter(new FocusTimerFragment.RecentSessionsAdapter(pastEntries));
    }
}
