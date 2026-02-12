package com.example.tablia.presentation.onboarding.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tablia.presentation.onboarding.plan.view.DiscoverFragment;
import com.example.tablia.presentation.onboarding.plan.view.PlanFragment;
import com.example.tablia.presentation.onboarding.plan.view.SaveFragment;

public class OnboardingAdapter extends FragmentStateAdapter {
    public OnboardingAdapter(@NonNull OnboardingFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PlanFragment();
            case 1:
                return new DiscoverFragment();
            case 2:
                return new SaveFragment();
            default:
                return new PlanFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}