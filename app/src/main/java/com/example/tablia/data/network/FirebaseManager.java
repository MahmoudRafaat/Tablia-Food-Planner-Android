package com.example.tablia.data.network;

import android.util.Log;

import com.example.tablia.data.auth.models.User;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseManager {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseManager instance ;

    public FirebaseManager() {}

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public Completable saveUserProfile(User user) {
        return Completable.create(emitter -> {
            db.collection("users").document(user.getId())
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener(v -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public String getUserId() { return mAuth.getUid(); }
    public Single<User> getUserProfile() {
        String userId = getUserId();
        if (userId == null) return Single.error(new Exception("Not logged in"));
        return Single.create(emitter -> {
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            emitter.onSuccess(user);
                        } else {
                            emitter.onError(new Exception("User profile not found"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
    public Completable addFavorite(Meal meal) {
        String userId = getUserId();
        Log.d("TAG", "addFavorite: "+userId);
        if (userId == null) return Completable.complete();
        return Completable.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("fav").document(meal.getIdMeal())
                    .set(meal)
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Completable removeFavorite(String mealId) {
        String userId = getUserId();
        if (userId == null) return Completable.complete();
        return Completable.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("fav").document(mealId)
                    .delete()
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Completable addAppointment(MealAppointment appointment) {
        String userId = getUserId();
        if (userId == null) return Completable.complete();
        return Completable.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("appointment").document(appointment.getId())
                    .set(appointment)
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Completable removeAppointment(String appointmentId) {
        String userId = getUserId();
        if (userId == null) return Completable.complete();
        return Completable.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("appointment").document(appointmentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Single<List<Meal>> getFavorites() {
        String userId = getUserId();
        if (userId == null) return Single.error(new Exception("Not logged in"));
        return Single.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("fav").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        emitter.onSuccess(queryDocumentSnapshots.toObjects(Meal.class));
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Single<List<MealAppointment>> getAppointments() {
        String userId = getUserId();
        if (userId == null) return Single.error(new Exception("Not logged in"));
        return Single.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("appointment").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        emitter.onSuccess(queryDocumentSnapshots.toObjects(MealAppointment.class));
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
