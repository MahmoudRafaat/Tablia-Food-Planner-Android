package com.example.tablia.presentation.auth.login.presenter;

import android.content.Context;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.presentation.auth.login.view.LoginView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImp implements LoginPresenter {
    private final AuthRepository repository;
    private final MealsRepository mealsRepository;
    private final LoginView view;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenterImp(LoginView view, Context context) {
        this.view = view;
        this.mealsRepository = MealsRepository.getInstance(context);
        this.repository = new AuthRepository(context);
    }

    @Override
    public void loginWithEmail(String email, String password) {
        if (email.isEmpty()) {
            view.showEmailError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            view.showPasswordError("Password is required");
            return;
        }

        view.showLoading();
        disposables.add(repository.loginWithEmail(email, password)
                .andThen(repository.fetchUserProfileRemote())
                .flatMapCompletable(user -> repository.saveUser(user)
                        .andThen(repository.setLoggedIn(true))
                        .andThen(mealsRepository.syncAppointmentsWithRemote())
                        .andThen(mealsRepository.syncFavoritesWithRemote()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.hideLoading();
                            view.onLoginSuccess();
                        },
                        throwable -> {
                            view.hideLoading();
                            parseError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void loginWithGoogle(String idToken) {
        view.showLoading();
        disposables.add(repository.loginWithGoogle(idToken)
                .flatMapCompletable(user -> repository.saveUserRemote(user)
                        .andThen(repository.saveUser(user))
                        .andThen(mealsRepository.syncFavoritesWithRemote())
                        .andThen(mealsRepository.syncAppointmentsWithRemote())
                        .andThen(repository.setLoggedIn(true)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.hideLoading();
                            view.onLoginSuccess();
                        },
                        throwable -> {
                            view.hideLoading();
                            parseError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void loginAsGuest() {
        view.onLoginSuccess();
    }

    private void parseError(String errorMessage) {
        if (errorMessage == null) return;
        String lowerMsg = errorMessage.toLowerCase();

        if (lowerMsg.contains("credential") || lowerMsg.contains("wrong-password") || lowerMsg.contains("incorrect")) {
            view.showFullAuthError("Invalid email or password. Please try again.");
        } else if (lowerMsg.contains("user-not-found") || lowerMsg.contains("no user record")) {
            view.showEmailError("This email is not registered.");
        } else if (lowerMsg.contains("network") || lowerMsg.contains("connection") || lowerMsg.contains("timeout")) {
            view.showGeneralError("No internet connection. Please check your network.");
        } else if (lowerMsg.contains("badly formatted") || lowerMsg.contains("invalid email")) {
            view.showEmailError("The email address is badly formatted.");
        } else {
            view.showGeneralError(errorMessage);
        }
    }

    public void detach() {
        disposables.clear();
    }
}
