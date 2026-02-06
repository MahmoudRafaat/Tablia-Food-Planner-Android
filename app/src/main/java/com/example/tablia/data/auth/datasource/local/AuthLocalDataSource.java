package com.example.tablia.data.auth.datasource.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tablia.BuildConfig;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthLocalDataSource {
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_FIRST_RUN = "isFirstRun";

    private final SharedPreferences prefs;
    private static AuthLocalDataSource instance ;

    private AuthLocalDataSource(Context context) {
        prefs = context.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static AuthLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new AuthLocalDataSource(context.getApplicationContext());
        }
        return instance;
    }

    public Single<Boolean> isLoggedIn() {
        return Single.fromCallable(() -> prefs.getBoolean(KEY_IS_LOGGED_IN, false));
    }

    public Completable setLoggedIn(boolean value) {
        return Completable.fromAction(() -> 
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
        );
    }

    public Single<Boolean> isFirstRun() {
        return Single.fromCallable(() -> prefs.getBoolean(KEY_IS_FIRST_RUN, true));
    }

    public Completable setFirstRun(boolean value) {
        return Completable.fromAction(() -> 
            prefs.edit().putBoolean(KEY_IS_FIRST_RUN, value).apply()
        );
    }

    public Completable clear() {
        return Completable.fromAction(() -> prefs.edit().clear().apply());
    }
}
