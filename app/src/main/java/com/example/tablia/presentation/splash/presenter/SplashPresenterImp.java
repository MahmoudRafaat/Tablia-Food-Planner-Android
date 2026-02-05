package com.example.tablia.presentation.splash.presenter;

import android.content.Context;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.presentation.splash.view.SplashView;

public class SplashPresenterImp implements SplashPresenter {
    private SplashView splashView;
    private AuthRepository repository;

    public SplashPresenterImp(SplashView splashView, Context context) {
        this.splashView = splashView;
        this.repository = new AuthRepository(context);
    }

    @Override
    public void decideNextScreen() {
        if (repository.isFirstRun()) {
            splashView.navigateToOnboarding();
        } else if (repository.isLoggedIn()) {
            splashView.navigateToHome();
        } else {
            splashView.navigateToLogin();
        }
    }
}
