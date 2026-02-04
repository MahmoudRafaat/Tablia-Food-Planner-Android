package com.example.tablia.data.auth.datasource.remote;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthRemoteDataSource {
    private FirebaseAuth mAuth;

    public AuthRemoteDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void loginWithEmail(String email, String password, AuthNetworkCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(task.getException().getMessage());
                });
    }

    public void loginAnonymously(AuthNetworkCallback callback) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure("Guest login failed.");
                });
    }

    public void loginWithGoogle(String idToken, AuthNetworkCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure("Google Sign-In failed.");
                });
    }
}