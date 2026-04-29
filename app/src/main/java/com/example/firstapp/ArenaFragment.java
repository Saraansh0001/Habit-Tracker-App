package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.data.ChallengeRepository;
import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.ArenaUiState;
import com.example.firstapp.models.Challenge;
import com.example.firstapp.models.UserRank;

import java.util.ArrayList;
import java.util.List;

public class ArenaFragment extends Fragment {

    private ArenaViewModel viewModel;
    private ArenaAdapter ongoingAdapter;
    private ArenaAdapter availableAdapter;
    
    private TextView tvRankValue;
    private TextView tvPercentile;
    private Button btnCreate;
    private Button btnLeaderboard;
    private TextView tvSeeAllOngoing;
    private TextView tvSeeAllAvailable;

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
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state != null) {
                tvRankValue.setText(getString(R.string.rank_format, state.getUserRank().getRank()));
                tvPercentile.setText(getString(R.string.percentile_format, state.getUserRank().getPercentile()));
                ongoingAdapter.setItems(state.getOngoingChallenges());
                availableAdapter.setItems(state.getAvailableChallenges());
            }
        });

        tvSeeAllOngoing.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_analytics);
            }
        });

        tvSeeAllAvailable.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).navigateToTab(R.id.navigation_search);
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

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refresh();
        }
    }

    private void initViews(View view) {
        tvRankValue = view.findViewById(R.id.tv_rank_value);
        tvPercentile = view.findViewById(R.id.tv_percentile);
        btnCreate = view.findViewById(R.id.btn_create);
        btnLeaderboard = view.findViewById(R.id.btn_leaderboard);
        tvSeeAllOngoing = view.findViewById(R.id.tv_see_all_ongoing);
        tvSeeAllAvailable = view.findViewById(R.id.tv_see_all_available);
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

    public static class ArenaViewModel extends androidx.lifecycle.AndroidViewModel {
        private final MutableLiveData<ArenaUiState> _uiState = new MutableLiveData<>();
        public LiveData<ArenaUiState> getUiState() { return _uiState; }
        
        private static final String PREFS_NAME = "arena_prefs";
        private static final String JOINED_KEY = "joined_challenges";
        private final android.content.SharedPreferences prefs;
        private final ChallengeRepository challengeRepository;

        public ArenaViewModel(@NonNull android.app.Application application) {
            super(application);
            prefs = application.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
            challengeRepository = new ChallengeRepository(application);
            loadData();
        }

        public void refresh() {
            loadData();
        }

        private void loadData() {
            challengeRepository.getAllChallenges().observeForever(allChallenges -> {
                if (allChallenges == null) return;
                
                java.util.Set<String> joinedIds = prefs.getStringSet(JOINED_KEY, new java.util.HashSet<>());
                UserRank rank = new UserRank(42, 15); // Mock rank
                
                List<Challenge> ongoing = new ArrayList<>();
                List<Challenge> available = new ArrayList<>();

                for (Challenge c : allChallenges) {
                    if (joinedIds.contains(c.getId())) {
                        c.setActive(true);
                        ongoing.add(c);
                    }
                    // Keep in available list even if joined, but marked as active
                    available.add(c);
                }
                
                _uiState.setValue(new ArenaUiState(rank, ongoing, available));
            });
        }

        public void joinChallenge(Challenge challenge) {
            // OFFLINE: Bypass API call
            challenge.setParticipants(challenge.getParticipants() + 1);
            challenge.setActive(true);
            
            // Persist locally
            challengeRepository.updateLocalChallenge(challenge);
            
            // Mark as joined in Arena prefs
            java.util.Set<String> joinedIds = new java.util.HashSet<>(prefs.getStringSet(JOINED_KEY, new java.util.HashSet<>()));
            joinedIds.add(challenge.getId());
            prefs.edit().putStringSet(JOINED_KEY, joinedIds).apply();
            
            android.widget.Toast.makeText(getApplication(), "Joined " + challenge.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
            loadData(); // reload UI
        }
    }

    private static class ArenaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Challenge> items = new ArrayList<>();
        private final boolean isOngoing;
        private final OnChallengeClickListener listener;
        public interface OnChallengeClickListener { void onJoinClick(Challenge challenge); }
        public ArenaAdapter(boolean isOngoing, OnChallengeClickListener listener) { this.isOngoing = isOngoing; this.listener = listener; }
        public void setItems(List<Challenge> newItems) { this.items = newItems; notifyDataSetChanged(); }
        @Override
        public int getItemViewType(int position) { return isOngoing ? 0 : 1; }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return viewType == 0 ? new OngoingViewHolder(inflater.inflate(R.layout.item_ongoing_challenge, parent, false))
                                 : new AvailableViewHolder(inflater.inflate(R.layout.item_available_challenge, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof OngoingViewHolder) ((OngoingViewHolder) holder).bind(items.get(position));
            else if (holder instanceof AvailableViewHolder) ((AvailableViewHolder) holder).bind(items.get(position), listener);
        }
        @Override
        public int getItemCount() { return items.size(); }

        static class OngoingViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvParticipants, tvDuration; ProgressBar progressBar;
            ImageView ivIcon; CardView cvIcon;
            OngoingViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title); tvParticipants = itemView.findViewById(R.id.tv_participants);
                tvDuration = itemView.findViewById(R.id.tv_duration); progressBar = itemView.findViewById(R.id.progress_bar);
                ivIcon = itemView.findViewById(R.id.iv_icon); cvIcon = itemView.findViewById(R.id.cv_icon_container);
            }
            void bind(Challenge challenge) {
                tvTitle.setText(challenge.getTitle());
                tvParticipants.setText(itemView.getContext().getString(R.string.participants_format, challenge.getParticipants()));
                tvDuration.setText(itemView.getContext().getString(R.string.duration_format, challenge.getDuration()));
                progressBar.setProgress(challenge.getProgress());
                if (challenge.getIconRes() != 0 && ivIcon != null) ivIcon.setImageResource(challenge.getIconRes());
                if (challenge.getColor() != null) {
                    int c = Color.parseColor(challenge.getColor());
                    if (ivIcon != null) {
                        ivIcon.setImageTintList(ColorStateList.valueOf(c));
                    }
                    if (cvIcon != null) {
                        cvIcon.setCardBackgroundColor(Color.argb(30, Color.red(c), Color.green(c), Color.blue(c)));
                    }
                    progressBar.setProgressTintList(ColorStateList.valueOf(c));
                }
            }
        }
        static class AvailableViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvSubtitle; ImageView ivIcon; CardView cvIcon; Button btnJoin;
            AvailableViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title); tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
                ivIcon = itemView.findViewById(R.id.iv_icon); cvIcon = itemView.findViewById(R.id.cv_icon_container);
                btnJoin = itemView.findViewById(R.id.btn_join);
            }
            void bind(Challenge challenge, OnChallengeClickListener listener) {
                tvTitle.setText(challenge.getTitle());
                tvSubtitle.setText(itemView.getContext().getString(R.string.subtitle_format, challenge.getParticipants(), challenge.getDuration()));
                if (challenge.getIconRes() != 0) ivIcon.setImageResource(challenge.getIconRes());
                if (challenge.isActive()) {
                    btnJoin.setText("Joined");
                    btnJoin.setEnabled(false);
                    btnJoin.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                } else {
                    btnJoin.setText("Join");
                    btnJoin.setEnabled(true);
                    if (challenge.getColor() != null) {
                        btnJoin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(challenge.getColor())));
                    }
                }

                if (challenge.getColor() != null) {
                    int c = Color.parseColor(challenge.getColor());
                    ivIcon.setImageTintList(ColorStateList.valueOf(c));
                    cvIcon.setCardBackgroundColor(Color.argb(30, Color.red(c), Color.green(c), Color.blue(c)));
                }
                btnJoin.setOnClickListener(v -> { if (listener != null) listener.onJoinClick(challenge); });
            }
        }
    }
}
