package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArenaFragment extends Fragment {

    private ArenaViewModel viewModel;
    private ArenaAdapter ongoingAdapter;
    private ArenaAdapter availableAdapter;
    
    private TextView tvRankValue;
    private TextView tvPercentile;
    private Button btnCreate;
    private Button btnLeaderboard;

    @Nullable
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arena, container, false);
        
        initViews(view);
        setupRecyclerViews(view);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(ArenaViewModel.class);
        
        viewModel.uiState.observe(getViewLifecycleOwner(), state -> {
            if (state != null) {
                tvRankValue.setText(String.format("#%d", state.getUserRank().getRank()));
                tvPercentile.setText(state.getUserRank().getPercentile());
                
                ongoingAdapter.setItems(state.getOngoingChallenges());
                availableAdapter.setItems(state.getAvailableChallenges());
            }
        });

        btnCreate.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new CreateChallengeFragment());
            }
        });

        btnLeaderboard.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(new LeaderboardFragment());
            }
        });
    }

    private void initViews(View view) {
        tvRankValue = view.findViewById(R.id.tv_rank_value);
        tvPercentile = view.findViewById(R.id.tv_percentile);
        btnCreate = view.findViewById(R.id.btn_create);
        btnLeaderboard = view.findViewById(R.id.btn_leaderboard);
    }

    private void setupRecyclerViews(View view) {
        RecyclerView rvOngoing = view.findViewById(R.id.rv_ongoing);
        RecyclerView rvAvailable = view.findViewById(R.id.rv_available);

        ongoingAdapter = new ArenaAdapter(true, null);
        availableAdapter = new ArenaAdapter(false, challenge -> viewModel.joinChallenge(challenge));

        rvOngoing.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOngoing.setAdapter(ongoingAdapter);

        rvAvailable.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAvailable.setAdapter(availableAdapter);
    }
}
