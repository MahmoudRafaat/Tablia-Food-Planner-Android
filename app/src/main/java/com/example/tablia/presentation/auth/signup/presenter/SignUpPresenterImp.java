package com.example.tablia.presentation.auth.signup.presenter;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.presentation.auth.signup.view.SignUpView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpPresenterImp implements SignUpPresenter {
    private final SignUpView view;
    private final AuthRepository repository;
    private final Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SignUpPresenterImp(SignUpView view, Context context) {
        this.view = view;
        this.context = context;
        this.repository = new AuthRepository(context);
    }

    @Override
    public void signUp(String fullName, String email, String password, Uri imageUri) {
        if (fullName.isEmpty()) {
            view.showFullNameError("Full name is required");
            return;
        }
        if (email.isEmpty()) {
            view.showEmailError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            view.showPasswordError("Password is required");
            return;
        }
        if (password.length() < 6) {
            view.showPasswordError("Password should be at least 6 characters");
            return;
        }

        view.showLoading();
        disposables.add(repository.register(email, password, fullName, imageUri, context)
                .flatMapCompletable(user -> repository.saveUserRemote(user)
                        .andThen(repository.saveUser(user))
                        .andThen(repository.setLoggedIn(true))
                        .andThen(repository.setFirstRun(false)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.hideLoading();
                            view.onSignUpSuccess();
                        },
                        throwable -> {
                            view.hideLoading();
                            parseError(throwable.getMessage());
                        }
                ));
    }

    private void parseError(String errorMessage) {
        if (errorMessage == null) return;
        String lowerMsg = errorMessage.toLowerCase();
        if (lowerMsg.contains("email") && lowerMsg.contains("already")) {
            view.showEmailError("This email is already registered.");
        } else if (lowerMsg.contains("badly formatted") || lowerMsg.contains("invalid email")) {
            view.showEmailError("Invalid email format.");
        } else if (lowerMsg.contains("network") || lowerMsg.contains("connection")) {
            view.showGeneralError("No internet connection.");
        } else {
            view.showGeneralError(errorMessage);
        }
    }

    public void detach() {
        disposables.clear();
    }
}
