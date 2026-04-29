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
            android.widget.EditText etContent = view.findViewById(R.id.et_journal_content);
            String content = etContent != null ? etContent.getText().toString() : "No content";
            com.example.firstapp.models.JournalEntry entry = new com.example.firstapp.models.JournalEntry("Today", "Great", content);
            
            com.example.firstapp.network.ApiClient.getService(getContext()).createJournalEntry(entry)
                .enqueue(new retrofit2.Callback<com.example.firstapp.models.JournalEntry>() {
                    @Override
                    public void onResponse(retrofit2.Call<com.example.firstapp.models.JournalEntry> call, retrofit2.Response<com.example.firstapp.models.JournalEntry> response) {
                        if (getActivity() != null) getActivity().onBackPressed();
                    }
                    @Override
                    public void onFailure(retrofit2.Call<com.example.firstapp.models.JournalEntry> call, Throwable t) {
                        android.widget.Toast.makeText(getContext(), "Network error", android.widget.Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) getActivity().onBackPressed();
                    }
                });
        });

        setupPastEntries(view);

        return view;
    }

    private void setupPastEntries(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_past_entries);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        com.example.firstapp.network.ApiClient.getService(getContext()).getJournalEntries()
            .enqueue(new retrofit2.Callback<List<com.example.firstapp.models.JournalEntry>>() {
                @Override
                public void onResponse(retrofit2.Call<List<com.example.firstapp.models.JournalEntry>> call, retrofit2.Response<List<com.example.firstapp.models.JournalEntry>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        rv.setAdapter(new JournalAdapter(response.body()));
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<com.example.firstapp.models.JournalEntry>> call, Throwable t) {
                    android.widget.Toast.makeText(getContext(), "Network error", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
    }

    static class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.VH> {
        List<com.example.firstapp.models.JournalEntry> entries;
        JournalAdapter(List<com.example.firstapp.models.JournalEntry> e) { entries = e; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(android.R.layout.simple_list_item_2, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            com.example.firstapp.models.JournalEntry e = entries.get(p);
            h.t1.setText(e.getDate() + " - " + e.getMood());
            h.t2.setText(e.getContent());
        }
        @Override public int getItemCount() { return entries.size(); }
        static class VH extends RecyclerView.ViewHolder {
            android.widget.TextView t1, t2;
            VH(View v) {
                super(v);
                t1 = v.findViewById(android.R.id.text1);
                t2 = v.findViewById(android.R.id.text2);
            }
        }
    }
}
