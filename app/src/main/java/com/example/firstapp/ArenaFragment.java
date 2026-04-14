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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        btnCreate.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(PlaceholderFragment.newInstance(R.string.create_challenge));
            }
        });

        btnLeaderboard.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).loadFragment(PlaceholderFragment.newInstance(R.string.leaderboard));
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

    public static class UserRank {
        private int rank;
        private int percentile;
        public UserRank(int rank, int percentile) { this.rank = rank; this.percentile = percentile; }
        public int getRank() { return rank; }
        public int getPercentile() { return percentile; }
    }

    public static class Challenge {
        private String id, title, duration, type, color;
        private int participants, progress, iconRes;
        private boolean isActive;
        public Challenge(String id, String title, int participants, String duration, int progress, boolean isActive, int iconRes, String type, String color) {
            this.id = id; this.title = title; this.participants = participants; this.duration = duration;
            this.progress = progress; this.isActive = isActive; this.iconRes = iconRes; this.type = type; this.color = color;
        }
        public String getTitle() { return title; }
        public int getParticipants() { return participants; }
        public String getDuration() { return duration; }
        public int getProgress() { return progress; }
        public int getIconRes() { return iconRes; }
        public String getColor() { return color; }
        public void setActive(boolean active) { isActive = active; }
    }

    public static class ArenaUiState {
        private final UserRank userRank;
        private final List<Challenge> ongoingChallenges, availableChallenges;
        public ArenaUiState(UserRank userRank, List<Challenge> ongoing, List<Challenge> available) {
            this.userRank = userRank; this.ongoingChallenges = ongoing; this.availableChallenges = available;
        }
        public UserRank getUserRank() { return userRank; }
        public List<Challenge> getOngoingChallenges() { return ongoingChallenges; }
        public List<Challenge> getAvailableChallenges() { return availableChallenges; }
    }

    public static class ArenaViewModel extends ViewModel {
        private final MutableLiveData<ArenaUiState> _uiState = new MutableLiveData<>();
        public LiveData<ArenaUiState> getUiState() { return _uiState; }
        public ArenaViewModel() { loadMockData(); }
        private void loadMockData() {
            UserRank rank = new UserRank(42, 15);
            List<Challenge> ongoing = new ArrayList<>();
            ongoing.add(new Challenge("1", "30-Day Meditation", 128, "18d left", 40, true, 0, "Meditation", "#6B3FD4"));
            ongoing.add(new Challenge("2", "Morning Workout", 85, "6d left", 60, true, 0, "Workout", "#6B3FD4"));
            List<Challenge> available = new ArrayList<>();
            available.add(new Challenge("3", "No Social Media", 234, "7 days", 0, false, android.R.drawable.ic_menu_search, "Social", "#38BDF8"));
            available.add(new Challenge("4", "Reading Marathon", 156, "14 days", 0, false, android.R.drawable.ic_menu_search, "Reading", "#34D399"));
            available.add(new Challenge("5", "Hydration Challenge", 312, "30 days", 0, false, android.R.drawable.ic_menu_search, "Health", "#818CF8"));
            available.add(new Challenge("6", "Sleep by 10 PM", 189, "21 days", 0, false, android.R.drawable.ic_menu_search, "Sleep", "#F59E0B"));
            _uiState.setValue(new ArenaUiState(rank, ongoing, available));
        }
        public void joinChallenge(Challenge challenge) {
            ArenaUiState currentState = _uiState.getValue();
            if (currentState != null) {
                List<Challenge> available = new ArrayList<>(currentState.availableChallenges);
                List<Challenge> ongoing = new ArrayList<>(currentState.ongoingChallenges);
                if (available.remove(challenge)) {
                    challenge.setActive(true);
                    ongoing.add(challenge);
                    _uiState.setValue(new ArenaUiState(currentState.userRank, ongoing, available));
                }
            }
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
            OngoingViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title); tvParticipants = itemView.findViewById(R.id.tv_participants);
                tvDuration = itemView.findViewById(R.id.tv_duration); progressBar = itemView.findViewById(R.id.progress_bar);
            }
            void bind(Challenge challenge) {
                tvTitle.setText(challenge.getTitle());
                tvParticipants.setText(itemView.getContext().getString(R.string.participants_format, challenge.getParticipants()));
                tvDuration.setText(itemView.getContext().getString(R.string.duration_format, challenge.getDuration()));
                progressBar.setProgress(challenge.getProgress());
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
                if (challenge.getColor() != null) {
                    int c = Color.parseColor(challenge.getColor());
                    btnJoin.setBackgroundTintList(ColorStateList.valueOf(c));
                    ivIcon.setImageTintList(ColorStateList.valueOf(c));
                    cvIcon.setCardBackgroundColor(Color.argb(30, Color.red(c), Color.green(c), Color.blue(c)));
                }
                btnJoin.setOnClickListener(v -> { if (listener != null) listener.onJoinClick(challenge); });
            }
        }
    }
}