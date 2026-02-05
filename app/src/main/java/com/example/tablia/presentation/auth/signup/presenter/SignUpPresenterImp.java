package com.example.tablia.presentation.auth.signup.presenter;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.auth.datasource.remote.AuthNetworkCallback;
import com.example.tablia.presentation.auth.signup.view.SignUpView;

public class SignUpPresenterImp implements SignUpPresenter {
    private SignUpView view;
    private AuthRepository repository;
    private Context context;

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
        repository.register(email, password, fullName, imageUri, context, new AuthNetworkCallback() {
            @Override
            public void onSuccess() {
                repository.setLoggedIn(true);
                repository.setFirstRun(false);
                view.hideLoading();
                view.onSignUpSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                parseError(errorMessage);
            }
        });
    }

    private void parseError(String errorMessage) {
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
}
