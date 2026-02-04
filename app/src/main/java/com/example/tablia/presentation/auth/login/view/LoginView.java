package com.example.tablia.presentation.auth.login.view;

public interface LoginView {
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    
    void showEmailError(String message);
    void showPasswordError(String message);
    void showFullAuthError(String message);
    void showGeneralError(String message);

    void navigateToSignUp();
}