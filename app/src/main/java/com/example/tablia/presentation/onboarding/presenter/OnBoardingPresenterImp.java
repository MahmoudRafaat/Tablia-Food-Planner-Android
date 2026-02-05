package com.example.tablia.presentation.onboarding.presenter;

import android.content.Context;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.presentation.onboarding.view.OnboardingView;

public class OnBoardingPresenterImp implements OnBoardingPresenter {
    private OnboardingView view;
    private AuthRepository repository;

    public OnBoardingPresenterImp(OnboardingView view, Context context) {
        this.view = view;
        this.repository = new AuthRepository(context);
    }

    @Override
    public void handleNext(int currentItem) {
        if (currentItem < 2) {
            view.navigateToNextPage(currentItem + 1);
        } else {
            finishOnboarding();
        }
    }

    @Override
    public void handleSkip() {
        finishOnboarding();
    }

    private void finishOnboarding() {
        repository.setFirstRun(false);
        view.navigateToLogin();
    }
}
