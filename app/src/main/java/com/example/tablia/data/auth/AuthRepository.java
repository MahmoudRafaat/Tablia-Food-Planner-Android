package com.example.tablia.data.auth;

import android.content.Context;
import android.net.Uri;

import com.example.tablia.data.auth.datasource.local.AuthLocalDataSource;
import com.example.tablia.data.auth.datasource.remote.*;

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

    public boolean isLoggedIn() { return local.isLoggedIn(); }
    public void setLoggedIn(boolean value) { local.setLoggedIn(value); }
    public boolean isFirstRun() { return local.isFirstRun(); }
    public void setFirstRun(boolean value) { local.setFirstRun(value); }
}