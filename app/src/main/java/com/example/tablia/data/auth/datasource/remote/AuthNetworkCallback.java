package com.example.tablia.data.auth.datasource.remote;

public interface AuthNetworkCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}