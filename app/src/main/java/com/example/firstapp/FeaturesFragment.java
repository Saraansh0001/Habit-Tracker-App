package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firstapp.models.ProfileFeature;

import java.util.ArrayList;
import java.util.List;

public class FeaturesFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_features, container, false);
        
        View btnBack = view.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) getActivity().onBackPressed();
            });
        }

        setupFeatures(view, inflater);

        return view;
    }

    private void setupFeatures(View view, LayoutInflater inflater) {
        LinearLayout container = view.findViewById(R.id.features_container);
        if (container == null) return;
        container.removeAllViews();

        List<ProfileFeature> features = new ArrayList<>();
        features.add(new ProfileFeature("Streak Calendar", "Visual habit history", R.drawable.ic_nav_home, "#F3F0FF"));
        features.add(new ProfileFeature("Focus Timer", "Deep focus on one habit at a time", R.drawable.ic_bolt, "#FFF7ED"));
        features.add(new ProfileFeature("Weekly Goals", "Set habit target", R.drawable.ic_bolt, "#F0FDF4"));
        features.add(new ProfileFeature("Daily Journal", "Reflect & grow", R.drawable.ic_bolt, "#EEF2FF"));
        features.add(new ProfileFeature("Mood Tracker", "Track how you feel", R.drawable.ic_bolt, "#FFF1F2"));
        features.add(new ProfileFeature("Friends", "Social accountability", R.drawable.ic_bolt, "#F0FDFA"));
        features.add(new ProfileFeature("Rewards", "Badges & milestone", R.drawable.ic_badge_1, "#FEFCE8"));
        features.add(new ProfileFeature("Weekly Report", "Sunday summary", R.drawable.ic_nav_analytics, "#FDF2F8"));

        for (ProfileFeature feature : features) {
            try {
                View itemView = inflater.inflate(R.layout.item_profile_feature, container, false);
                
                TextView title = itemView.findViewById(R.id.tv_feature_title);
                TextView desc = itemView.findViewById(R.id.tv_feature_desc);
                ImageView icon = itemView.findViewById(R.id.iv_feature_icon);
                CardView iconContainer = itemView.findViewById(R.id.cv_feature_icon_container);

                if (title != null) title.setText(feature.getTitle());
                if (desc != null) desc.setText(feature.getDescription());
                if (icon != null) icon.setImageResource(feature.getIconRes());
                
                int bgColor = Color.parseColor(feature.getBackgroundColor());
                if (iconContainer != null) iconContainer.setCardBackgroundColor(bgColor);

                if (icon != null) {
                    float[] hsv = new float[3];
                    Color.colorToHSV(bgColor, hsv);
                    hsv[2] *= 0.5f; 
                    hsv[1] = Math.min(1.0f, hsv[1] * 2.0f); 
                    icon.setImageTintList(ColorStateList.valueOf(Color.HSVToColor(hsv)));
                }

                itemView.setOnClickListener(v -> {
                    if ("Streak Calendar".equals(feature.getTitle())) {
                        loadFragment(new StreakCalendarFragment());
                    } else if ("Focus Timer".equals(feature.getTitle())) {
                        loadFragment(new FocusTimerFragment());
                    } else if ("Daily Journal".equals(feature.getTitle())) {
                        loadFragment(new DailyJournalFragment());
                    } else if ("Weekly Goals".equals(feature.getTitle())) {
                        loadFragment(new WeeklyGoalsFragment());
                    } else {
                        // Placeholder
                    }
                });
                container.addView(itemView);
                
                View spacer = new View(getContext());
                spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 16));
                container.addView(spacer);
            } catch (Exception ignored) {}
        }
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).loadFragment(fragment);
        }
    }
}
