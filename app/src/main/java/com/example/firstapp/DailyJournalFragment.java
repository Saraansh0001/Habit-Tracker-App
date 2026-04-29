package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.models.FocusSession;

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
            if (getActivity() != null) getActivity().onBackPressed();
        });

        setupPastEntries(view);

        return view;
    }

    private void setupPastEntries(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_past_entries);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<FocusSession> pastEntries = new ArrayList<>();
        pastEntries.add(new FocusSession("Completed morning meditation", 0, 0));
        pastEntries.add(new FocusSession("Had a super productive day", 0, 0));
        pastEntries.add(new FocusSession("Drank all 8 glasses of water", 0, 0));

        rv.setAdapter(new FocusTimerFragment.RecentSessionsAdapter(pastEntries));
    }
}
