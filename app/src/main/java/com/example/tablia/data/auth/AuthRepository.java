package com.example.tablia.data.auth;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.datasource.local.AuthLocalDataSource;
import com.example.tablia.data.auth.datasource.remote.*;
import com.example.tablia.data.auth.models.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    private final AuthRemoteDataSource remote;
    private final AuthLocalDataSource local;

    public AuthRepository(Context context) {
        this.remote = new AuthRemoteDataSource();
        this.local = AuthLocalDataSource.getInstance(context);
    }

    public Single<User> loginWithEmail(String email, String password) {
        return remote.loginWithEmail(email, password);
    }

    public Single<User> register(String email, String password, String name, Uri imageUri, Context context) {
        return remote.registerWithEmail(email, password, name, imageUri, context);
    }

    public Single<User> loginWithGoogle(String token) {
        return remote.loginWithGoogle(token);
    }

    public Single<Boolean> isLoggedIn() { return local.isLoggedIn(); }
    public Completable setLoggedIn(boolean value) { return local.setLoggedIn(value); }
    public Single<Boolean> isFirstRun() { return local.isFirstRun(); }
    public Completable setFirstRun(boolean value) { return local.setFirstRun(value); }
    public Completable logout() { return local.clear(); }

    public Completable saveUser(User user) {
        return local.saveUser(user);
    }

    public Single<User> getUser() {
        return local.getUser();
    }
}
