package com.example.firstapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firstapp.models.ProfileFeature;

import java.util.ArrayList;
import java.util.List;

public class FeaturesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_features, container, false);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        setupFeatures(view, inflater);

        return view;
    }

    private void setupFeatures(View view, LayoutInflater inflater) {
        ViewGroup container = view.findViewById(R.id.features_container);
        if (container == null) return;

        List<ProfileFeature> features = new ArrayList<>();
        features.add(new ProfileFeature("Streak Calendar", "Visual habit history", R.drawable.ic_nav_home, "#F5F3FF"));
        features.add(new ProfileFeature("Focus Timer", "Deep work session", R.drawable.ic_nav_home, "#FFF7ED"));
        features.add(new ProfileFeature("Weekly Goals", "Set habit target", R.drawable.ic_nav_home, "#ECFDF5"));
        features.add(new ProfileFeature("Daily Journal", "Reflect & grow", R.drawable.ic_nav_home, "#EEF2FF"));
        features.add(new ProfileFeature("Mood Tracker", "Track how you feel", R.drawable.ic_nav_home, "#FFF1F2"));
        features.add(new ProfileFeature("Friends", "Social accountability", R.drawable.ic_nav_home, "#F0FDFA"));
        features.add(new ProfileFeature("Rewards", "Badges & milestone", R.drawable.ic_nav_home, "#FFFBEB"));
        features.add(new ProfileFeature("Weekly Report", "Sunday summary", R.drawable.ic_nav_home, "#FDF2F8"));

        for (ProfileFeature feature : features) {
            View itemView = inflater.inflate(R.layout.item_profile_feature, container, false);
            
            TextView title = itemView.findViewById(R.id.tv_feature_title);
            TextView desc = itemView.findViewById(R.id.tv_feature_desc);
            ImageView icon = itemView.findViewById(R.id.iv_feature_icon);
            CardView iconContainer = itemView.findViewById(R.id.cv_feature_icon_container);

            title.setText(feature.getTitle());
            desc.setText(feature.getDescription());
            icon.setImageResource(feature.getIconRes());
            iconContainer.setCardBackgroundColor(Color.parseColor(feature.getBackgroundColor()));
            
            int bgColor = Color.parseColor(feature.getBackgroundColor());
            float[] hsv = new float[3];
            Color.colorToHSV(bgColor, hsv);
            hsv[2] *= 0.6f;
            hsv[1] = Math.min(1.0f, hsv[1] * 1.5f);
            icon.setImageTintList(ColorStateList.valueOf(Color.HSVToColor(hsv)));

            itemView.setOnClickListener(v -> Toast.makeText(getContext(), feature.getTitle() + " clicked", Toast.LENGTH_SHORT).show());
            container.addView(itemView);
        }
    }
}
