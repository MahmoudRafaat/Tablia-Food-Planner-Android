package com.example.tablia.data.auth.datasource.remote;

import com.example.tablia.data.auth.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRemoteDataSource {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public AuthRemoteDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mFirestore = FirebaseFirestore.getInstance();
    }

    public void loginWithEmail(String email, String password, AuthNetworkCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(task.getException().getMessage());
                });
    }

    public void registerWithEmail(String email, String password, AuthNetworkCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Registration failed");
                });
    }

    public void saveUserToFirestore(User user, AuthNetworkCallback callback) {
        mFirestore.collection("users")
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Failed to save user data");
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

    public String getCurrentUserUid() {
        return (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;
    }
}