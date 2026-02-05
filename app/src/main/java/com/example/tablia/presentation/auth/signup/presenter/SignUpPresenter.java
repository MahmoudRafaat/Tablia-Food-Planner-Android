package com.example.tablia.presentation.auth.signup.presenter;

import android.net.Uri;

public interface SignUpPresenter {
    void signUp(String fullName, String email, String password, Uri imageUri);
}