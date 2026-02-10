package com.example.tablia.presentation.onboarding.presenter;

import android.content.Context;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.presentation.onboarding.view.OnboardingView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OnBoardingPresenterImp implements OnBoardingPresenter {
    private final OnboardingView view;
    private final AuthRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

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
        disposables.add(repository.setFirstRun(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.navigateToLogin(),
                        throwable -> view.navigateToLogin()
                ));
    }

    public void detach() {
        disposables.clear();
    }
}
