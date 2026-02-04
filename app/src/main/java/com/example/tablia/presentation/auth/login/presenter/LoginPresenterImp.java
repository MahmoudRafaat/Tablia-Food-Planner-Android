package com.example.tablia.presentation.auth.login.presenter;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.auth.datasource.remote.AuthNetworkCallback;
import com.example.tablia.presentation.auth.login.view.LoginView;

public class LoginPresenterImp implements LoginPresenter {
    private AuthRepository repository;
    private LoginView view;

    public LoginPresenterImp(LoginView view) {
        this.view = view;
        this.repository = new AuthRepository();
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
        repository.login(email, password, createCallback());
    }

    @Override
    public void loginWithGoogle(String idToken) {
        view.showLoading();
        repository.loginWithGoogle(idToken, createCallback());
    }

    @Override
    public void loginAsGuest() {
        view.showLoading();
        repository.loginAsGuest(createCallback());
    }

    private AuthNetworkCallback createCallback() {
        return new AuthNetworkCallback() {
            @Override
            public void onSuccess() {
                view.hideLoading();
                view.onLoginSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                parseError(errorMessage);
            }
        };
    }

    private void parseError(String errorMessage) {
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
}