package com.example.tablia.presentation.profile.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tablia.R;
import com.example.tablia.data.auth.models.User;
import com.example.tablia.databinding.FragmentProfileBinding;
import com.example.tablia.presentation.auth.AuthActivity;
import com.example.tablia.presentation.profile.presenter.ProfilePresenter;
import com.example.tablia.presentation.profile.presenter.ProfilePresenterImp;
import com.example.tablia.utils.ImageUtils;
import com.google.android.material.snackbar.Snackbar;


public class ProfileFragment extends Fragment implements ProfileView {

    private FragmentProfileBinding binding;
    private ProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ProfilePresenterImp(this, getContext());

        presenter.getProfileDeatails();

        binding.btnLogout.setOnClickListener(v -> presenter.logout());
    }

    @Override
    public void showUserInfo(User user) {
        // Reset visibility for logged-in user
        binding.btnLogout.setVisibility(View.VISIBLE);
        binding.btnSignin.setVisibility(View.GONE);
        binding.cvEditProfile.setVisibility(View.VISIBLE);

        binding.tvUserName.setText(user.getFullName());
        binding.tvUserEmail.setText(user.getEmail());

        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            Bitmap bitmap = ImageUtils.base64ToBitmap(user.getProfilePicture());
            if (bitmap != null) {
                binding.ivProfileImage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void showFavoritesCount(int count) {
        binding.tvFavoritesCount.setText(String.valueOf(count));
    }

    @Override
    public void showPlannedCount(int count) {
        binding.tvPlannedCount.setText(String.valueOf(count));
    }

    @Override
    public void onLogoutSuccess() {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "Logout successful", Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(getResources().getColor(R.color.white, getContext().getTheme()));
        snackbar.setBackgroundTint(getResources().getColor(R.color.tomato_red, getContext().getTheme()));
        snackbar.show();

        Intent intent = new Intent(getActivity(), AuthActivity.class);
        intent.putExtra("destination", "login");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLogoutFailure(String error) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(getResources().getColor(R.color.tomato_red_dark, getContext().getTheme()));
        snackbar.setBackgroundTint(getResources().getColor(R.color.white, getContext().getTheme()));
        snackbar.show();
    }

    @Override
    public void showGuestAlert() {
        if (getContext() != null) {
            // UI for Guest
            binding.tvUserName.setText(R.string.join_tablia_and_start_planning);
            binding.tvUserEmail.setText("");
            binding.tvFavoritesCount.setText("0");
            binding.tvPlannedCount.setText("0");

            binding.btnLogout.setVisibility(View.GONE);
            binding.btnSignin.setVisibility(View.VISIBLE);
            binding.cvEditProfile.setVisibility(View.GONE); // Guests can't edit profile

            binding.btnSignin.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.putExtra("destination", "login");
                startActivity(intent);
                requireActivity().finish();
            });
        }
    }
}
