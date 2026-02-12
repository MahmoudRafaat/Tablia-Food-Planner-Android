package com.example.tablia.presentation.auth.signup.view;

public interface SignUpView {
    void showLoading();

    void hideLoading();

    void onSignUpSuccess();

    void showFullNameError(String message);

    void showEmailError(String message);

    void showPasswordError(String message);

    void showGeneralError(String message);
}