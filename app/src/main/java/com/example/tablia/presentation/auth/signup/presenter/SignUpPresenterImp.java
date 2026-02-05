package com.example.tablia.presentation.auth.signup.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.auth.datasource.remote.AuthNetworkCallback;
import com.example.tablia.data.auth.models.User;
import com.example.tablia.presentation.auth.signup.view.SignUpView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SignUpPresenterImp implements SignUpPresenter {
    private SignUpView view;
    private AuthRepository repository;
    private Context context;

    public SignUpPresenterImp(SignUpView view, Context context) {
        this.view = view;
        this.repository = new AuthRepository();
        this.context = context;
    }

    @Override
    public void signUp(String fullName, String email, String password, Uri imageUri) {
        if (fullName.isEmpty()) {
            view.showFullNameError("Full name is required");
            return;
        }
        if (email.isEmpty()) {
            view.showEmailError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            view.showPasswordError("Password is required");
            return;
        }
        if (password.length() < 6) {
            view.showPasswordError("Password should be at least 6 characters");
            return;
        }

        view.showLoading();
        
        String base64Image = null;
        if (imageUri != null) {
            base64Image = uriToBase64(imageUri);
        }
        
        final String finalBase64Image = base64Image;

        repository.register(email, password, new AuthNetworkCallback() {
            @Override
            public void onSuccess() {
                String uid = repository.getCurrentUid();
                User user = new User(uid, fullName, email, finalBase64Image);
                saveUserToFirestore(user);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                parseError(errorMessage);
            }
        });
    }

    private String uriToBase64(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveUserToFirestore(User user) {
        repository.saveUser(user, new AuthNetworkCallback() {
            @Override
            public void onSuccess() {
                view.hideLoading();
                view.onSignUpSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showGeneralError("Failed to save user data: " + errorMessage);
            }
        });
    }

    private void parseError(String errorMessage) {
        String lowerMsg = errorMessage.toLowerCase();
        if (lowerMsg.contains("email") && lowerMsg.contains("already")) {
            view.showEmailError("This email is already registered.");
        } else if (lowerMsg.contains("badly formatted") || lowerMsg.contains("invalid email")) {
            view.showEmailError("Invalid email format.");
        } else if (lowerMsg.contains("network") || lowerMsg.contains("connection")) {
            view.showGeneralError("No internet connection.");
        } else {
            view.showGeneralError(errorMessage);
        }
    }
}