package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        
        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        TextView myRank = view.findViewById(R.id.tv_my_rank);
        TextView myXp = view.findViewById(R.id.tv_my_xp);
        TextView myStreak = view.findViewById(R.id.tv_my_streak);

        myRank.setText(getString(R.string.rank_format, 42));
        myXp.setText(getString(R.string.xp_format, "2,450"));
        myStreak.setText(getString(R.string.streak_format, 12));

        RecyclerView rv = view.findViewById(R.id.rv_leaderboard);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new LeaderboardAdapter(getMockLeaderboard()));

        return view;
    }

    private List<LeaderboardUser> getMockLeaderboard() {
        List<LeaderboardUser> users = new ArrayList<>();
        users.add(new LeaderboardUser("4", "Aayush Rathore", 3890, 28));
        users.add(new LeaderboardUser("5", "Shejal Kushwaha", 3650, 25));
        users.add(new LeaderboardUser("6", "Priya Das", 3420, 22));
        users.add(new LeaderboardUser("7", "Romi", 3200, 20));
        users.add(new LeaderboardUser("8", "Sachin Singh", 2980, 18));
        users.add(new LeaderboardUser("9", "Aryan", 2750, 15));
        users.add(new LeaderboardUser("10", "Zoya Ansari", 2600, 14));
        return users;
    }

    static class LeaderboardUser {
        String rank, name;
        int xp, streak;
        LeaderboardUser(String r, String n, int x, int s) {
            rank = r; name = n; xp = x; streak = s;
        }
    }

    static class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.VH> {
        List<LeaderboardUser> users;
        LeaderboardAdapter(List<LeaderboardUser> u) { users = u; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_leaderboard_user, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            LeaderboardUser u = users.get(p);
            h.rank.setText(u.rank);
            h.name.setText(u.name);
            h.xp.setText(h.itemView.getContext().getString(R.string.xp_format, String.valueOf(u.xp)));
            h.streak.setText(h.itemView.getContext().getString(R.string.streak_format, u.streak));
        }
        @Override public int getItemCount() { return users.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView rank, name, xp, streak;
            VH(View v) {
                super(v);
                rank = v.findViewById(R.id.tv_rank);
                name = v.findViewById(R.id.tv_name);
                xp = v.findViewById(R.id.tv_xp);
                streak = v.findViewById(R.id.tv_streak);
            }
        }
    }
}
