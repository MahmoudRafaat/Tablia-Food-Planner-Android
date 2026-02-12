package com.example.tablia.presentation.onboarding.plan.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tablia.R;
import com.example.tablia.databinding.FragmentOnBoardingBinding;

public class PlanFragment extends Fragment {
    private FragmentOnBoardingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showPlanData();

    }

    public void showPlanData() {
        binding.cardOnboardingIcon.setCardBackgroundColor(getResources().getColor(R.color.onboarding_red_end, null));
        binding.imgOnboarding.setImageResource(R.drawable.ic_chef_hat);
        binding.tvOnboardingTitle.setText("Plan Your Meals Easily");
        binding.tvOnboardingSubtitle.setText("Weekly meal planning made simple and delightful");
        binding.cardOnboardingIcon.setAlpha(0f);
        binding.cardOnboardingIcon.setScaleX(0.8f);
        binding.cardOnboardingIcon.setRotation(-10f);

        binding.tvOnboardingTitle.setAlpha(0f);
        binding.tvOnboardingTitle.setTranslationY(40f);

        binding.tvOnboardingSubtitle.setAlpha(0f);
        binding.tvOnboardingSubtitle.setTranslationY(40f);

        binding.cardOnboardingIcon.animate()
                .alpha(1f)
                .scaleX(1f)
                .rotation(0f)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator()) // Mimics spring
                .start();

        binding.tvOnboardingTitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(200)
                .setDuration(500)
                .start();

        binding.tvOnboardingSubtitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(400)
                .setDuration(500)
                .start();
    }


}