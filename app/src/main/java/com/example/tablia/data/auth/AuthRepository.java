package com.example.tablia.data.auth;

import com.example.tablia.data.auth.datasource.remote.AuthNetworkCallback;
import com.example.tablia.data.auth.datasource.remote.AuthRemoteDataSource;
import com.example.tablia.data.auth.models.User;

public class AuthRepository {
    private AuthRemoteDataSource remoteDataSource;

    public AuthRepository() {
        this.remoteDataSource = new AuthRemoteDataSource();
    }

    public void login(String email, String password, AuthNetworkCallback callback) {
        remoteDataSource.loginWithEmail(email, password, callback);
    }

    public void register(String email, String password, AuthNetworkCallback callback) {
        remoteDataSource.registerWithEmail(email, password, callback);
    }

    public void saveUser(User user, AuthNetworkCallback callback) {
        remoteDataSource.saveUserToFirestore(user, callback);
    }

    public void loginAsGuest(AuthNetworkCallback callback) {
        remoteDataSource.loginAnonymously(callback);
    }

    public void loginWithGoogle(String idToken, AuthNetworkCallback callback) {
        remoteDataSource.loginWithGoogle(idToken, callback);
    }
    
    public String getCurrentUid() {
        return remoteDataSource.getCurrentUserUid();
    }
}