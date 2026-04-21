package com.example.firstapp;

import android.app.Application;
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
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.firstapp.data.ChallengeRepository;
import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.ArenaUiState;
import com.example.firstapp.models.Challenge;
import com.example.firstapp.models.Habit;
import com.example.firstapp.models.UserRank;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ArenaFragment extends Fragment {

    private ArenaViewModel viewModel;
    private ArenaAdapter ongoingAdapter;
    private ArenaAdapter availableAdapter;
    private HistoryAdapter historyAdapter;
    
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
                historyAdapter.setItems(state.getHabitHistory());
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
        ongoingAdapter = new ArenaAdapter(true, null);
        availableAdapter = new ArenaAdapter(false, challenge -> viewModel.joinChallenge(challenge));
        historyAdapter = new HistoryAdapter(new ArrayList<>());

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RecyclerView rv = new RecyclerView(parent.getContext());
                rv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                rv.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                rv.setPadding(0, 20, 0, 200);
                rv.setClipToPadding(false);
                return new RecyclerView.ViewHolder(rv) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                RecyclerView rv = (RecyclerView) holder.itemView;
                if (position == 0) rv.setAdapter(ongoingAdapter);
                else if (position == 1) rv.setAdapter(availableAdapter);
                else rv.setAdapter(historyAdapter);
            }

            @Override
            public int getItemCount() { return 3; }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText(R.string.ongoing_competitions);
            else if (position == 1) tab.setText(R.string.join_new_challenge);
            else tab.setText(R.string.history_title);
        }).attach();
    }

    public static class ArenaViewModel extends AndroidViewModel {
        private final MutableLiveData<ArenaUiState> _uiState = new MutableLiveData<>();
        public LiveData<ArenaUiState> getUiState() { return _uiState; }
        
        private static final String PREFS_NAME = "arena_prefs";
        private static final String JOINED_KEY = "joined_challenges";
        private final android.content.SharedPreferences prefs;
        private final HabitRepository habitRepository;
        private final ChallengeRepository challengeRepository;

        public ArenaViewModel(@NonNull Application application) {
            super(application);
            prefs = application.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
            habitRepository = new HabitRepository(application);
            challengeRepository = new ChallengeRepository(application);
            loadData();
        }

        private void loadData() {
            java.util.Set<String> joinedIds = prefs.getStringSet(JOINED_KEY, new java.util.HashSet<>());
            
            UserRank rank = new UserRank(42, 15);
            
            List<Challenge> allChallenges = challengeRepository.getAllChallenges();

            List<Challenge> ongoing = new ArrayList<>();
            List<Challenge> available = new ArrayList<>();

            for (Challenge c : allChallenges) {
                if (joinedIds.contains(c.getId())) {
                    c.setActive(true);
                    ongoing.add(c);
                } else {
                    available.add(c);
                }
            }
            
            if (joinedIds.isEmpty()) {
                ongoing.add(allChallenges.get(0));
                ongoing.add(allChallenges.get(1));
                allChallenges.get(0).setActive(true);
                allChallenges.get(1).setActive(true);
                available.remove(allChallenges.get(0));
                available.remove(allChallenges.get(1));
                
                java.util.Set<String> initialJoined = new java.util.HashSet<>();
                initialJoined.add("1");
                initialJoined.add("2");
                prefs.edit().putStringSet(JOINED_KEY, initialJoined).apply();
            }

            List<Habit> history = habitRepository.getArchivedHabits();

            _uiState.setValue(new ArenaUiState(rank, ongoing, available, history));
        }

        public void joinChallenge(Challenge challenge) {
            ArenaUiState currentState = _uiState.getValue();
            if (currentState != null) {
                List<Challenge> available = new ArrayList<>(currentState.getAvailableChallenges());
                List<Challenge> ongoing = new ArrayList<>(currentState.getOngoingChallenges());
                
                if (available.remove(challenge)) {
                    challenge.setActive(true);
                    ongoing.add(challenge);
                    
                    java.util.Set<String> joinedIds = new java.util.HashSet<>(prefs.getStringSet(JOINED_KEY, new java.util.HashSet<>()));
                    joinedIds.add(challenge.getId());
                    prefs.edit().putStringSet(JOINED_KEY, joinedIds).apply();

                    _uiState.setValue(new ArenaUiState(currentState.getUserRank(), ongoing, available, currentState.getHabitHistory()));
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
                if (challenge.getIconRes() != 0) ivIcon.setImageResource(challenge.getIconRes());
                if (challenge.getColor() != null) {
                    int c = Color.parseColor(challenge.getColor());
                    ivIcon.setImageTintList(ColorStateList.valueOf(c));
                    cvIcon.setCardBackgroundColor(Color.argb(30, Color.red(c), Color.green(c), Color.blue(c)));
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