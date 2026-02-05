package com.example.tablia.presentation.auth.login.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tablia.R;
import com.example.tablia.databinding.FragmentLoginBinding;
import com.example.tablia.presentation.auth.login.presenter.LoginPresenter;
import com.example.tablia.presentation.auth.login.presenter.LoginPresenterImp;
import com.example.tablia.utils.GoogleSignInHelper;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment implements LoginView {
    private LoginPresenter presenter;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new LoginPresenterImp(this);

        setupClickListeners();
        setupTextWatchers();
    }

    private void setupClickListeners() {
        binding.btnSignIn.setOnClickListener(v -> {
            clearErrors();
            presenter.loginWithEmail(
                    binding.etEmail.getText().toString().trim(),
                    binding.etPassword.getText().toString()
            );
        });

        binding.btnGust.setOnClickListener(v -> presenter.loginAsGuest());
        
        binding.btnGoogle.setOnClickListener(v -> {
            GoogleSignInHelper.signIn(requireActivity(), new GoogleSignInHelper.CredentialCallback() {
                @Override
                public void onSuccess(String idToken) {
                    presenter.loginWithGoogle(idToken);
                }

                @Override
                public void onFailure(String error) {
                    showGeneralError(getString(R.string.sign_in_failed_prefix, error));
                }
            });
        });

        binding.tvSignUpLink.setOnClickListener(v -> navigateToSignUp());
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearErrors();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.etEmail.addTextChangedListener(textWatcher);
        binding.etPassword.addTextChangedListener(textWatcher);
    }

    private void clearErrors() {
        binding.layoutEmail.setError(null);
        binding.layoutPassword.setError(null);
    }

    @Override
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        setInputsEnabled(false);
    }

    @Override
    public void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        setInputsEnabled(true);
    }

    private void setInputsEnabled(boolean enabled) {
        binding.etEmail.setEnabled(enabled);
        binding.etPassword.setEnabled(enabled);
        binding.btnSignIn.setEnabled(enabled);
        binding.btnGoogle.setEnabled(enabled);
        binding.btnGust.setEnabled(enabled);
        binding.tvSignUpLink.setEnabled(enabled);
    }

    @Override
    public void showEmailError(String message) {
        hideLoading();
        binding.layoutEmail.setError(message);
        binding.layoutEmail.requestFocus();
    }

    @Override
    public void showPasswordError(String message) {
        hideLoading();
        binding.layoutPassword.setError(message);
        binding.layoutPassword.requestFocus();
    }
    
    @Override
    public void showFullAuthError(String message) {
        hideLoading();
        binding.layoutEmail.setError(" ");
        binding.layoutPassword.setError(message);
        binding.layoutPassword.requestFocus();
    }

    @Override
    public void showGeneralError(String message) {
        hideLoading();
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.tomato_red, requireContext().getTheme()))
                    .setAction("Retry", v -> binding.btnSignIn.performClick())
                    .setActionTextColor(getResources().getColor(R.color.white, requireContext().getTheme()))
                    .show();
        }
    }

    @Override
    public void onLoginSuccess() {
        hideLoading();
        Log.d("LOGIN_SUCCESS", "Login successful. Navigating to home...");
    }

    @Override
    public void navigateToSignUp() {
        if (getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_signUpFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}