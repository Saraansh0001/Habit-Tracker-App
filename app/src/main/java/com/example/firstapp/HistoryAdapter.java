package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.models.Habit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Habit> archivedHabits;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public HistoryAdapter(List<Habit> archivedHabits) {
        this.archivedHabits = archivedHabits;
    }

    public void setItems(List<Habit> newItems) {
        this.archivedHabits = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Habit habit = archivedHabits.get(position);
        holder.bind(habit, dateFormat);
    }

    @Override
    public int getItemCount() {
        return archivedHabits.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvCategory;
        private final TextView tvCompletedDate;
        private final ImageView ivIcon;
        private final CardView cvIconContainer;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_habit_title);
            tvCategory = itemView.findViewById(R.id.tv_habit_category);
            tvCompletedDate = itemView.findViewById(R.id.tv_completed_date);
            ivIcon = itemView.findViewById(R.id.iv_habit_icon);
            cvIconContainer = itemView.findViewById(R.id.cv_icon_container);
        }

        public void bind(Habit habit, SimpleDateFormat dateFormat) {
            tvTitle.setText(habit.getTitle());
            tvCategory.setText(habit.getCategory());
            
            if (habit.getCompletedDate() > 0) {
                tvCompletedDate.setText(dateFormat.format(new Date(habit.getCompletedDate())));
            } else {
                tvCompletedDate.setText("N/A");
            }

            int color = Color.parseColor(habit.getColor());
            ivIcon.setImageTintList(ColorStateList.valueOf(color));
            cvIconContainer.setCardBackgroundColor(Color.argb(30, Color.red(color), Color.green(color), Color.blue(color)));
            
            // Set icon if available, otherwise default
            if (habit.getIconRes() != 0) {
                ivIcon.setImageResource(habit.getIconRes());
            } else {
                ivIcon.setImageResource(R.drawable.ic_nav_home);
            }
        }
    }
}