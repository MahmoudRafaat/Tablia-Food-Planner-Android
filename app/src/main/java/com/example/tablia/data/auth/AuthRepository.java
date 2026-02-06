package com.example.tablia.data.auth;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.datasource.local.AuthLocalDataSource;
import com.example.tablia.data.auth.datasource.remote.*;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    private final AuthRemoteDataSource remote;
    private final AuthLocalDataSource local;

    public AuthRepository(Context context) {
        this.remote = new AuthRemoteDataSource();
        this.local = AuthLocalDataSource.getInstance(context);
    }

    public void loginWithEmail(String email, String password, AuthNetworkCallback callback) {
        remote.loginWithEmail(email, password, callback);
    }

    public void register(String email, String password, String name, Uri imageUri, Context context , AuthNetworkCallback callback) {
        remote.registerWithEmail(email, password, name, imageUri, context, callback);
    }

    public void loginWithGoogle(String token, AuthNetworkCallback callback) {
        remote.loginWithGoogle(token, callback);
    }

    public Single<Boolean> isLoggedIn() { return local.isLoggedIn(); }
    public Completable setLoggedIn(boolean value) { return local.setLoggedIn(value); }
    public Single<Boolean> isFirstRun() { return local.isFirstRun(); }
    public Completable setFirstRun(boolean value) { return local.setFirstRun(value); }
    public Completable logout() { return local.clear(); }
}
