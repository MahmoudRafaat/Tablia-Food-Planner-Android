package com.example.tablia.data.auth.datasource.remote;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.models.User;
import com.example.tablia.data.network.FirebaseManager;
import com.example.tablia.utils.ImageUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSource {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseManager firebaseManager = FirebaseManager.getInstance();

    public Single<User> registerWithEmail(String email, String password, String name, Uri imageUri, Context context) {
        return Single.create(emitter -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String base64 = null;
                            if (imageUri != null) {
                                base64 = ImageUtils.uriToBase64(context, imageUri);
                            }
                            User user = new User(mAuth.getUid(), name, email, base64);
                            firebaseManager.saveUserProfile(user)
                                    .subscribe(() -> emitter.onSuccess(user), emitter::onError);
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Single<User> loginWithEmail(String email, String password) {
        return Single.create(emitter -> {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fetchUserProfile().subscribe(emitter::onSuccess, emitter::onError);
                } else {
                    emitter.onError(task.getException());
                }
            });
        });
    }

    public Single<User> loginWithGoogle(String idToken) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fetchUserProfile().subscribe(emitter::onSuccess, emitter::onError);
                } else {
                    emitter.onError(new Exception("Google login failed"));
                }
            });
        });
    }

    private Single<User> fetchUserProfile() {
        return firebaseManager.getUserProfile();
    }
}
