package com.example.tablia.data.auth.datasource.local;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthLocalDataSource {
    private final SharedPreferences prefs;
    private static AuthLocalDataSource instance = null;

    private AuthLocalDataSource(Context context) {
        prefs = context.getSharedPreferences("TabliaPrefs", Context.MODE_PRIVATE);
    }

    public static AuthLocalDataSource getInstance(Context context) {
        if (instance == null) instance = new AuthLocalDataSource(context);
        return instance;
    }

    public boolean isLoggedIn() { return prefs.getBoolean("isLoggedIn", false); }
    public void setLoggedIn(boolean value) { prefs.edit().putBoolean("isLoggedIn", value).apply(); }

    public boolean isFirstRun() { return prefs.getBoolean("isFirstRun", true); }
    public void setFirstRun(boolean value) { prefs.edit().putBoolean("isFirstRun", value).apply(); }
}