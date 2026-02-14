package com.example.tablia.data.auth.datasource.remote;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.models.User;
import com.example.tablia.data.network.FirebaseManager;
import com.example.tablia.utils.ImageUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.core.Completable;
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
                            emitter.onSuccess(user);
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Completable loginWithEmail(String email, String password) {
        return Completable.create(emitter -> {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onComplete();
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
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String name = firebaseUser.getDisplayName();
                        String email = firebaseUser.getEmail();
                        String profilePictureUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "";
                        User user = new User(firebaseUser.getUid(), name, email, profilePictureUrl);
                        emitter.onSuccess(user);
                    } else {
                        emitter.onError(new Exception("login failed"));
                    }
                } else {
                    emitter.onError(new Exception("Google login failed"));
                }
            });
        });
    }

    public Completable saveUserProfile(User user) {
        return firebaseManager.saveUserProfile(user);
    }

    public Single<User> fetchUserProfileRemote() {
        return firebaseManager.getUserProfile();
    }
}
