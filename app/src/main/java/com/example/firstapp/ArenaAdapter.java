package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.models.Challenge;

import java.util.ArrayList;
import java.util.List;

public class ArenaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ONGOING = 0;
    private static final int TYPE_AVAILABLE = 1;

    private List<Challenge> items = new ArrayList<>();
    private final boolean isOngoing;
    private OnChallengeClickListener listener;

    public interface OnChallengeClickListener {
        void onJoinClick(Challenge challenge);
    }

    public ArenaAdapter(boolean isOngoing, OnChallengeClickListener listener) {
        this.isOngoing = isOngoing;
        this.listener = listener;
    }

    public void setItems(List<Challenge> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isOngoing ? TYPE_ONGOING : TYPE_AVAILABLE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONGOING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ongoing_challenge, parent, false);
            return new OngoingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_challenge, parent, false);
            return new AvailableViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Challenge challenge = items.get(position);
        if (holder instanceof OngoingViewHolder) {
            ((OngoingViewHolder) holder).bind(challenge);
        } else if (holder instanceof AvailableViewHolder) {
            ((AvailableViewHolder) holder).bind(challenge, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OngoingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvParticipants, tvDuration;
        ProgressBar progressBar;

        OngoingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvParticipants = itemView.findViewById(R.id.tv_participants);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

        void bind(Challenge challenge) {
            tvTitle.setText(challenge.getTitle());
            tvParticipants.setText(String.format("👤 %d", challenge.getParticipants()));
            tvDuration.setText(String.format("🕒 %s", challenge.getDuration()));
            progressBar.setProgress(challenge.getProgress());
        }
    }

    static class AvailableViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle;
        ImageView ivIcon;
        CardView cvIconContainer;
        Button btnJoin;

        AvailableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            cvIconContainer = itemView.findViewById(R.id.cv_icon_container);
            btnJoin = itemView.findViewById(R.id.btn_join);
        }

        void bind(Challenge challenge, OnChallengeClickListener listener) {
            tvTitle.setText(challenge.getTitle());
            tvSubtitle.setText(String.format("👤 %d · %s", challenge.getParticipants(), challenge.getDuration()));
            
            if (challenge.getIconRes() != 0) {
                ivIcon.setImageResource(challenge.getIconRes());
            }

            // Set dynamic colors
            if (challenge.getColor() != null) {
                int color = Color.parseColor(challenge.getColor());
                btnJoin.setBackgroundTintList(ColorStateList.valueOf(color));
                ivIcon.setImageTintList(ColorStateList.valueOf(color));
                
                // Set light background for icon container
                int alphaColor = Color.argb(30, Color.red(color), Color.green(color), Color.blue(color));
                cvIconContainer.setCardBackgroundColor(alphaColor);
            }

            btnJoin.setOnClickListener(v -> {
                if (listener != null) listener.onJoinClick(challenge);
            });
        }
    }
}