package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.data.HabitRepository;
import com.example.firstapp.models.Habit;

import java.util.List;

public class NavigationFragments {

    // --- Adapter for Discover Page ---
    public static class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
        private List<Habit> habits;

        public HabitAdapter(List<Habit> habits) { this.habits = habits; }

        public void updateList(List<Habit> newList) {
            this.habits = newList;
            notifyDataSetChanged();
        }

        @NonNull @Override
        public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit_discover, parent, false);
            return new HabitViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
            Habit h = habits.get(position);
            holder.tvTitle.setText(h.getTitle());
            holder.tvCategory.setText(h.getCategory());
            holder.tvDifficulty.setText(h.getDifficulty());
            holder.ivIcon.setImageResource(h.getIconRes() != 0 ? h.getIconRes() : R.drawable.ic_nav_home);

            int color = Color.parseColor(h.getColor());
            holder.ivIcon.setImageTintList(ColorStateList.valueOf(color));
            holder.cvIconContainer.setCardBackgroundColor(Color.argb(30, Color.red(color), Color.green(color), Color.blue(color)));

            // Style Difficulty Badge
            int diffBg, diffText;
            switch (h.getDifficulty()) {
                case "Easy":
                    diffBg = holder.itemView.getContext().getColor(R.color.difficulty_easy_bg);
                    diffText = holder.itemView.getContext().getColor(R.color.difficulty_easy_text);
                    break;
                case "Hard":
                    diffBg = holder.itemView.getContext().getColor(R.color.difficulty_hard_bg);
                    diffText = holder.itemView.getContext().getColor(R.color.difficulty_hard_text);
                    break;
                default: // Medium
                    diffBg = holder.itemView.getContext().getColor(R.color.difficulty_medium_bg);
                    diffText = holder.itemView.getContext().getColor(R.color.difficulty_medium_text);
                    break;
            }
            holder.tvDifficulty.setBackgroundTintList(ColorStateList.valueOf(diffBg));
            holder.tvDifficulty.setTextColor(diffText);

            holder.btnAdd.setOnClickListener(v -> {
                HabitRepository repository = new HabitRepository(v.getContext());
                repository.addHabit(new Habit(h.getTitle(), h.getCategory(), h.getDifficulty(), h.getColor(), h.getIconRes()));
                Toast.makeText(v.getContext(), v.getContext().getString(R.string.habit_added, h.getTitle()), Toast.LENGTH_SHORT).show();
            });
        }

        @Override public int getItemCount() { return habits.size(); }

        static class HabitViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvCategory, tvDifficulty;
            ImageView ivIcon;
            CardView cvIconContainer;
            Button btnAdd;
            HabitViewHolder(View v) {
                super(v);
                tvTitle = v.findViewById(R.id.tv_habit_title);
                tvCategory = v.findViewById(R.id.tv_habit_category);
                tvDifficulty = v.findViewById(R.id.tv_difficulty);
                ivIcon = v.findViewById(R.id.iv_habit_icon);
                cvIconContainer = v.findViewById(R.id.cv_icon_container);
                btnAdd = v.findViewById(R.id.btn_add_habit);
            }
        }
    }
}
