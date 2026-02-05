package com.example.tablia.presentation.auth.signup.view;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tablia.R;
import com.example.tablia.databinding.FragmentSignUpBinding;
import com.example.tablia.presentation.auth.signup.presenter.SignUpPresenter;
import com.example.tablia.presentation.auth.signup.presenter.SignUpPresenterImp;
import com.google.android.material.snackbar.Snackbar;

public class SignUpFragment extends Fragment implements SignUpView {
    private FragmentSignUpBinding binding;
    private SignUpPresenter presenter;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<String> pickMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                binding.imgProfile.setImageURI(uri);
                selectedImageUri = uri;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SignUpPresenterImp(this, requireContext());

        setupClickListeners();
        setupTextWatchers();
    }

    private void setupClickListeners() {
        binding.btnCreateAccount.setOnClickListener(v -> {
            clearErrors();
            presenter.signUp(
                    binding.etFullName.getText().toString().trim(),
                    binding.etSignUpEmail.getText().toString().trim(),
                    binding.etSignUpPassword.getText().toString(),
                    selectedImageUri
            );
        });

        binding.tvSignInLink.setOnClickListener(v -> {
            if (getView() != null) {
                Navigation.findNavController(getView()).navigateUp();
            }
        });

        binding.fabAddImage.setOnClickListener(v -> {
            pickMedia.launch("image/*");
        });
    }

    private void setupTextWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearErrors();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.etFullName.addTextChangedListener(watcher);
        binding.etSignUpEmail.addTextChangedListener(watcher);
        binding.etSignUpPassword.addTextChangedListener(watcher);
    }

    private void clearErrors() {
        binding.layoutFullName.setError(null);
        binding.layoutSignUpEmail.setError(null);
        binding.layoutSignUpPassword.setError(null);
        binding.layoutSignUpPassword.setErrorEnabled(false);
        binding.layoutSignUpEmail.setErrorEnabled(false);
        binding.layoutFullName.setErrorEnabled(false);
    }

    @Override
    public void showLoading() {
        binding.progressBarSignUp.setVisibility(View.VISIBLE);
        binding.btnCreateAccount.setEnabled(false);
        setInputsEnabled(false);
    }

    @Override
    public void hideLoading() {
        binding.progressBarSignUp.setVisibility(View.GONE);
        binding.btnCreateAccount.setEnabled(true);
        setInputsEnabled(true);
    }

    private void setInputsEnabled(boolean enabled) {
        binding.etFullName.setEnabled(enabled);
        binding.etSignUpEmail.setEnabled(enabled);
        binding.etSignUpPassword.setEnabled(enabled);
        binding.fabAddImage.setEnabled(enabled);
    }

    @Override
    public void onSignUpSuccess() {
        if (getView() != null) {
            Snackbar.make(getView(), "Account created successfully!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showFullNameError(String message) {
        binding.layoutFullName.setError(message);
        binding.layoutFullName.requestFocus();
    }

    @Override
    public void showEmailError(String message) {
        binding.layoutSignUpEmail.setError(message);
        binding.layoutSignUpEmail.requestFocus();
    }

    @Override
    public void showPasswordError(String message) {
        binding.layoutSignUpPassword.setError(message);
        binding.layoutSignUpPassword.requestFocus();
    }

    @Override
    public void showGeneralError(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.tomato_red, requireContext().getTheme()))
                    .show();
        }
    }

}