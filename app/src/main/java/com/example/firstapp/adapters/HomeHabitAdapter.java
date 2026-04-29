package com.example.firstapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firstapp.R;
import com.example.firstapp.models.Habit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeHabitAdapter extends RecyclerView.Adapter<HomeHabitAdapter.HabitViewHolder> {

    private List<Habit> habits;
    private List<Habit> habitsFull;
    private final OnHabitClickListener listener;

    public interface OnHabitClickListener {
        void onHabitClick(Habit habit);
        void onHabitDetailClick(Habit habit);
    }

    public HomeHabitAdapter(List<Habit> habits, OnHabitClickListener listener) {
        this.habits = new ArrayList<>(habits);
        this.habitsFull = new ArrayList<>(habits);
        this.listener = listener;
    }

    public void updateList(List<Habit> newList) {
        this.habitsFull = new ArrayList<>(newList);
        this.habits = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void filter(String query, String category) {
        List<Habit> filteredList = habitsFull.stream()
                .filter(h -> {
                    boolean matchesQuery = query.isEmpty() || h.getTitle().toLowerCase().contains(query.toLowerCase());
                    boolean matchesCategory = category.equals("All") || h.getCategory().equalsIgnoreCase(category);
                    return matchesQuery && matchesCategory;
                })
                .collect(Collectors.toList());
        
        this.habits = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit_home, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.bind(habit, listener);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvCategory;
        private final ImageView ivIcon;
        private final ImageView ivCheck;
        private final CardView cvIconContainer;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_habit_title);
            tvCategory = itemView.findViewById(R.id.tv_habit_category);
            ivIcon = itemView.findViewById(R.id.iv_habit_icon);
            ivCheck = itemView.findViewById(R.id.iv_check);
            cvIconContainer = itemView.findViewById(R.id.cv_icon_container);
        }

        public void bind(Habit habit, OnHabitClickListener listener) {
            tvTitle.setText(habit.getTitle());
            tvCategory.setText(habit.getCategory());
            
            // Set the specific icon from the habit object
            if (habit.getIconRes() != 0) {
                ivIcon.setImageResource(habit.getIconRes());
            } else {
                ivIcon.setImageResource(R.drawable.ic_bolt); // Default fallback
            }
            
            try {
                int color = Color.parseColor(habit.getColor());
                cvIconContainer.setCardBackgroundColor(adjustAlpha(color, 0.1f));
                ivIcon.setColorFilter(color);
            } catch (Exception e) {
                cvIconContainer.setCardBackgroundColor(Color.LTGRAY);
            }

            if (habit.isCompleted()) {
                ivCheck.setImageResource(R.drawable.ic_bolt);
                ivCheck.setColorFilter(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_dark));
            } else {
                ivCheck.setImageResource(R.drawable.circle_outline_grey);
                ivCheck.clearColorFilter();
            }

            ivCheck.setOnClickListener(v -> listener.onHabitClick(habit));
            itemView.setOnClickListener(v -> listener.onHabitDetailClick(habit));
        }

        private int adjustAlpha(int color, float factor) {
            int alpha = Math.round(255 * factor);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            return Color.argb(alpha, red, green, blue);
        }
    }
}
