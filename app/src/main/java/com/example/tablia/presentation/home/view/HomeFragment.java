package com.example.tablia.presentation.home.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.databinding.FragmentHomeBinding;
import com.example.tablia.presentation.home.presenter.HomePresenter;
import com.example.tablia.presentation.home.presenter.HomePresenterImpl;
import com.example.tablia.presentation.meal_details.view.MealDetailsActivity;
import com.example.tablia.utils.CustomAlertDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView, PopularMealAdapter.OnMealClickListener {

    private FragmentHomeBinding binding;
    private HomePresenter presenter;
    private PopularMealAdapter popularMealAdapter;
    private Meal currentRandomMeal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MealsRepository repository = MealsRepository.getInstance(getContext());
        presenter = new HomePresenterImpl(this, repository);

        initViews();
        initRecyclerViews();
        
        presenter.observeNetwork(getContext());
    }

    private void initViews() {
        binding.btnFavMealOfDay.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                presenter.toggleFavorite(currentRandomMeal);
            }
        });

        binding.cvMealOfTheDay.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                navigateToDetails(currentRandomMeal);
            }
        });
    }

    private void initRecyclerViews() {
        binding.rvPopularMeals.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        popularMealAdapter = new PopularMealAdapter(this);
        binding.rvPopularMeals.setAdapter(popularMealAdapter);
    }

    private void navigateToDetails(Meal meal) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        if (binding == null) return;
        this.currentRandomMeal = meal;
        binding.tvMealName.setText(meal.getStrMeal());
        binding.tvMealArea.setText(meal.getStrArea());
        if (isAdded()) {
            Glide.with(this).load(meal.getStrMealThumb()).into(binding.ivMealOfDay);
        }
        presenter.checkIsFavorite(meal);
    }

    @Override
    public void showPopularMeals(List<Meal> meals) {
        if (binding == null) return;
        popularMealAdapter.setList(meals);
    }

    @Override
    public void showError(String message) {
        if (getActivity() != null) {
            CustomAlertDialog.showError(getActivity(), message);
        }
    }

    @Override
    public void showNoInternet() {
        if (binding != null) {
            binding.layoutNoInternet.noInternetOverlay.setVisibility(View.VISIBLE);
            binding.homeContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideNoInternet() {
        if (binding != null) {
            binding.layoutNoInternet.noInternetOverlay.setVisibility(View.GONE);
            binding.homeContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
        if (binding != null) binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (binding != null) binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMealAddedToFavorites(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.primary))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show();
        }
    }

    @Override
    public void onMealRemovedFromFavorites(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.text_grey))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show();
        }
    }

    @Override
    public void updateFavoriteStatus(String mealId, boolean isFavorite) {
        if (currentRandomMeal != null && currentRandomMeal.getIdMeal().equals(mealId)) {
            currentRandomMeal.setFavorite(isFavorite);
            if (binding != null) {
                if (isFavorite) {
                    binding.btnFavMealOfDay.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    binding.btnFavMealOfDay.setImageResource(R.drawable.ic_heart);
                }
                binding.btnFavMealOfDay.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary)));
            }
        }
        if (popularMealAdapter != null) {
            popularMealAdapter.updateFavoriteStatus(mealId, isFavorite);
        }
    }

    @Override
    public void onFavoriteClick(Meal meal) {
        presenter.toggleFavorite(meal);
    }

    @Override
    public void onMealClick(Meal meal) {
        navigateToDetails(meal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.dispose();
        }
        binding = null;
    }
}
