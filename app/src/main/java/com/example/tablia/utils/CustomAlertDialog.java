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

    public static void showError(Context context, String message) {
        show(context, context.getString(R.string.error), message);
    }

    public static void showNoInternet(Context context) {
        show(context, context.getString(R.string.no_internet_connection), context.getString(R.string.no_internet_message));
    }
}
