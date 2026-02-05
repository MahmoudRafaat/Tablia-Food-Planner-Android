package com.example.tablia.data.network;

import com.example.tablia.data.auth.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseManager {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Completable saveUserProfile(User user) {
        return Completable.create(emitter -> {
            db.collection("users").document(user.getId())
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener(v -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }


    public String getUserId() { return mAuth.getUid(); }
}