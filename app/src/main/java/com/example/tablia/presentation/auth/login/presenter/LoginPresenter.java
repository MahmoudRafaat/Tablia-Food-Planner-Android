package com.example.tablia.presentation.auth.login.presenter;

public interface LoginPresenter {
    void loginWithEmail(String email, String password);
    void loginWithGoogle(String idToken);
    void loginAsGuest();
}
