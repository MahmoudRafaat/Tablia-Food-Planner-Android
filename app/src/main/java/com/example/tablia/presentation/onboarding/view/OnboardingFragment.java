package com.example.tablia.presentation.onboarding.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablia.databinding.FragmentOnboardingHostBinding;
import com.example.tablia.presentation.onboarding.presenter.OnBoardingPresenter;
import com.example.tablia.presentation.onboarding.presenter.OnBoardingPresenterImp;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingFragment extends Fragment implements OnboardingView {
    private FragmentOnboardingHostBinding binding;
    private OnBoardingPresenter presenter;
    private OnboardingAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnboardingHostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new OnboardingAdapter(this);
        binding.viewPager.setAdapter(adapter);
        presenter = new OnBoardingPresenterImp(this, getContext());binding.viewPager.setPageTransformer((page, position) -> {
            page.setAlpha(1 - Math.abs(position));
        });
        binding.dotIndicator.attachTo(binding.viewPager);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update button text logic here
                binding.btnNext.setText(position == 2 ? "Get Started" : "Next");

            }
        });
        binding.btnNext.setOnClickListener(v ->
                presenter.handleNext(binding.viewPager.getCurrentItem()));

        binding.tvSkip.setOnClickListener(v ->
                presenter.handleSkip());
    }

    @Override
    public void navigateToNextPage(int nextIndex) {
        binding.viewPager.setCurrentItem(nextIndex);
    }

    @Override
    public void navigateToLogin() {

    }
}