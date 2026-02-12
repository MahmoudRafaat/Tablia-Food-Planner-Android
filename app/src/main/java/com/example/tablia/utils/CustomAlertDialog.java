package com.example.tablia.utils;

import android.content.Context;

import com.example.tablia.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CustomAlertDialog {

    public static void show(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showConfirmation(Context context, String title, String message, OnConfirmationListener listener) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (listener != null) listener.onConfirm();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showGuestModeAlert(Context context, OnConfirmationListener onLoginListener) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.guest_mode_title)
                .setMessage(R.string.guest_mode_message)
                .setPositiveButton(R.string.go_to_login, (dialog, which) -> {
                    if (onLoginListener != null) onLoginListener.onConfirm();
                })
                .setNegativeButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showError(Context context, String message) {
        show(context, context.getString(R.string.error), message);
    }

    public static void showNoInternet(Context context) {
        show(context, context.getString(R.string.no_internet_connection), context.getString(R.string.no_internet_message));
    }

    public interface OnConfirmationListener {
        void onConfirm();
    }
}
