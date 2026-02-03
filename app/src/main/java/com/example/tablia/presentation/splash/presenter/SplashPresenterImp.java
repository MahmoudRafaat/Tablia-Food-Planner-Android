package com.example.tablia.presentation.splash.presenter;

import com.example.tablia.presentation.splash.view.SplashView;

public class SplashPresenterImp implements  SplashPresenter {
    SplashView splashView;

    public SplashPresenterImp(SplashView splashView) {
        this.splashView = splashView;
    }

    @Override
    public void decideNextScreen() {
        /// to do check if user is logged in
        splashView.navigateToLogin();
        /// to do check if user first time open the app
      //  splashView.navigateToOnboarding();
        /// to do check if user is logged in
        // splashView.navigateToHome();
    }
}
