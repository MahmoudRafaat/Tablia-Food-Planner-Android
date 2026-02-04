package com.example.tablia.presentation.splash.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tablia.presentation.splash.view.SplashView;

public class SplashPresenterImp implements  SplashPresenter {
   private SplashView splashView;
    private Context context;

    public SplashPresenterImp(SplashView splashView, Context context) {
        this.splashView = splashView;
        this.context = context;
    }

    @Override
    public void decideNextScreen() {
        /// to do check if user is logged in
        SharedPreferences prefs = context.getSharedPreferences("TabliaPrefs", Context.MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

       // if (isFirstRun) {
            splashView.navigateToOnboarding();
        //} else {
            /// to do check if user is logged in
            // splashView.navigateToHome();
          //  splashView.navigateToLogin();
        }

    }
//}
