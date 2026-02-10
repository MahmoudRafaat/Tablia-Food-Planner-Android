package com.example.tablia.presentation.profile.view;

import com.example.tablia.data.auth.models.User;

public interface ProfileView {
    void showUserInfo(User user);
    void showFavoritesCount(int count);
    void showPlannedCount(int count);
    void onLogoutSuccess();
    void onLogoutFailure(String error);
}
