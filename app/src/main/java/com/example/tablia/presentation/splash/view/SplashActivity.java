package com.example.tablia.presentation.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.tablia.databinding.ActivitySplashBinding;
import com.example.tablia.presentation.auth.AuthActivity;
import com.example.tablia.presentation.home.view.HomeActivity;
import com.example.tablia.presentation.splash.presenter.SplashPresenter;
import com.example.tablia.presentation.splash.presenter.SplashPresenterImp;

public class SplashActivity extends AppCompatActivity implements SplashView {

    private SplashPresenter presenter;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new SplashPresenterImp(this, getApplication());

        startAnimations();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            presenter.decideNextScreen();
        }, 4000);
    }

    public void startAnimations() {
        // Initial States
        binding.plateCircle.setScaleX(0f);
        binding.plateCircle.setScaleY(0f);
        binding.plateCircle.setRotation(-180f);
        binding.iconUtensils.setScaleX(0f);
        binding.iconUtensils.setScaleY(0f);
        binding.txtTablia.setAlpha(0f);
        binding.txtTablia.setTranslationY(30f);
        binding.txtTagline.setAlpha(0f);
        binding.lottieLoading.setAlpha(0f);

        binding.plateCircle.animate().scaleX(1.1f).scaleY(1.1f).rotation(0f)
                .setDuration(800).setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    binding.plateCircle.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    animateSteamRising();
                }).start();
        binding.iconUtensils.animate().scaleX(1f).scaleY(1f).setStartDelay(600)
                .setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        binding.txtTablia.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(600).start();
        binding.txtTagline.animate().alpha(1f).setStartDelay(1300).setDuration(600).start();
        binding.lottieLoading.animate().alpha(1f).setStartDelay(1800).setDuration(800).start();
    }

    private void animateSteamRising() {
        View[] steamLines = {binding.steamLeft, binding.steamMiddle, binding.steamRight};

        for (int i = 0; i < steamLines.length; i++) {
            final View line = steamLines[i];
            line.setTranslationY(10f);
            new Handler(Looper.getMainLooper()).postDelayed(() -> startSteamLoop(line), i * 300);
        }
    }

    private void startSteamLoop(View line) {
        line.setAlpha(0f);
        line.setTranslationY(10f);

        line.animate()
                .alpha(0.6f)
                .translationY(-50f)
                .setDuration(1500)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    line.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                        if (!isFinishing()) {
                            startSteamLoop(line);
                        }
                    }).start();
                }).start();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("destination", "login");
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToOnboarding() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("destination", "onboarding");
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
