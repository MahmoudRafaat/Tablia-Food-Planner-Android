package com.example.tablia.data.auth.datasource.remote;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.models.User;
import com.example.tablia.data.network.FirebaseManager;
import com.example.tablia.utils.ImageUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthRemoteDataSource {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseManager firebaseManager = new FirebaseManager();

    public void registerWithEmail(String email, String password, String name, Uri imageUri, Context context, AuthNetworkCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String base64 = null;
                        if (imageUri != null) {
                            base64 = ImageUtils.uriToBase64(context, imageUri);
                        }                        User user = new User(mAuth.getUid(), name, email, base64);
                        firebaseManager.saveUserProfile(user)
                                .subscribeOn(Schedulers.io())
                                .subscribe(callback::onSuccess, t -> callback.onFailure(t.getMessage()));
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }
    public void loginWithEmail(String email, String password, AuthNetworkCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) callback.onSuccess();
            else callback.onFailure(task.getException().getMessage());
        });
    }

    public void loginWithGoogle(String idToken, AuthNetworkCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) callback.onSuccess();
            else callback.onFailure("Google login failed");
        });
    }
}