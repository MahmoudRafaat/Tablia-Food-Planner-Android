package com.example.tablia.presentation.onboarding.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tablia.presentation.onboarding.view.OnboardingView;

public class OnBoardingPresenterImp implements OnBoardingPresenter{
    private OnboardingView view;
    private Context context;
    public OnBoardingPresenterImp(OnboardingView view, Context context) {
        this.view = view;
        this.context = context;

    }
    @Override
    public void handleNext(int currentItem) {
        if (currentItem < 2) {
            view.navigateToNextPage(currentItem + 1);
        } else {
            finishOnboarding();
        }
    }

    public void handleSkip() {
        finishOnboarding();
    }

    private void finishOnboarding() {
        // Business Logic: Save the state [cite: 25]
        SharedPreferences prefs = context.getSharedPreferences("TabliaPrefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isFirstRun", false).apply();
        // Navigation Logic: Go to Login [cite: 46]
        view.navigateToLogin();
    }
}
