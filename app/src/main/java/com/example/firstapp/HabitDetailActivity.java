package com.example.firstapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.firstapp.models.Habit;

public class HabitDetailActivity extends AppCompatActivity {

    public static final String EXTRA_HABIT = "extra_habit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_habit_detail);

        Habit habit = (Habit) getIntent().getSerializableExtra(EXTRA_HABIT);
        if (habit == null) {
            finish();
            return;
        }

        setupViews(habit);
    }

    private void setupViews(Habit habit) {
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvCategory = findViewById(R.id.tv_detail_category);
        TextView tvDifficulty = findViewById(R.id.tv_detail_difficulty);
        ImageView ivIcon = findViewById(R.id.iv_detail_icon);
        CardView cvIcon = findViewById(R.id.cv_habit_icon);

        tvTitle.setText(habit.getTitle());
        tvCategory.setText("Category: " + habit.getCategory());
        tvDifficulty.setText("Difficulty: " + habit.getDifficulty());

        if (habit.getIconRes() != 0) {
            ivIcon.setImageResource(habit.getIconRes());
        }

        try {
            int color = Color.parseColor(habit.getColor());
            ivIcon.setColorFilter(color);
            cvIcon.setCardBackgroundColor(Color.argb(30, Color.red(color), Color.green(color), Color.blue(color)));
        } catch (Exception e) {
            // Fallback
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}