package com.example.tablia.presentation.splash.presenter;

import android.content.Context;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.presentation.splash.view.SplashView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashPresenterImp implements SplashPresenter {
    private final SplashView splashView;
    private final AuthRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public SplashPresenterImp(SplashView splashView, Context context) {
        this.splashView = splashView;
        this.repository = new AuthRepository(context);
    }

    @Override
    public void decideNextScreen() {
        disposable.add(repository.isFirstRun()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFirstRun -> {
                    if (isFirstRun) {
                        splashView.navigateToOnboarding();
                    } else {
                        checkLoginStatus();
                    }
                }, throwable -> splashView.navigateToOnboarding()));
    }

    private void checkLoginStatus() {
        disposable.add(repository.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn -> {
                    if (isLoggedIn) {
                        splashView.navigateToHome();
                    } else {
                        splashView.navigateToLogin();
                    }
                }, throwable -> splashView.navigateToLogin()));
    }

    public void dispose() {
        disposable.clear();
    }
}
