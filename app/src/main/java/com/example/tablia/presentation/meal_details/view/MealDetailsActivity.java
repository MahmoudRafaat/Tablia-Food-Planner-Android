package com.example.tablia.presentation.meal_details.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.databinding.ActivityMealDetailsBinding;
import com.example.tablia.presentation.auth.AuthActivity;
import com.example.tablia.presentation.meal_details.presenter.MealDetailsPresenter;
import com.example.tablia.presentation.meal_details.presenter.MealDetailsPresenterImpl;
import com.example.tablia.utils.CustomAlertDialog;
import com.example.tablia.utils.VideoHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsView {

    private ActivityMealDetailsBinding binding;
    private MealDetailsPresenter presenter;
    private IngredientAdapter ingredientsAdapter;
    private Meal currentMeal;
    private boolean isFavorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthRepository authRepository = new AuthRepository(getApplication());
        presenter = new MealDetailsPresenterImpl(MealsRepository.getInstance(getApplication()), authRepository, this);
        presenter.observeNetwork(getApplication());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        ingredientsAdapter = new IngredientAdapter(null);
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        ingredientsAdapter.setList(null);
        binding.rvIngredients.setAdapter(ingredientsAdapter);
        getLifecycle().addObserver(binding.youtubePlayerView);

        Meal meal = getIntent().getParcelableExtra("meal");
        if (meal != null) {
            presenter.getMealDetails(meal);
            presenter.checkIsFavorite(meal.getIdMeal());
        }

        binding.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_ingredients) {
                    binding.rvIngredients.setVisibility(View.VISIBLE);
                    binding.tvInstructions.setVisibility(View.GONE);
                } else if (checkedId == R.id.btn_instructions) {
                    binding.rvIngredients.setVisibility(View.GONE);
                    binding.tvInstructions.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.fabFav.setOnClickListener(v -> {
            if (currentMeal != null) {
                if (isFavorite) {
                    CustomAlertDialog.showConfirmation(
                            this,
                            getString(R.string.remove_from_favorites),
                            getString(R.string.are_you_sure_you_want_to_delete_from_favorites),
                            () -> presenter.removeFromFavorites(currentMeal)
                    );
                } else {
                    presenter.addToFavorites(currentMeal);
                }
            }
        });

        binding.btnAddToPlanner.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.CustomDatePickerDialog,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth);
                    selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                    selectedDate.set(Calendar.MINUTE, 0);
                    selectedDate.set(Calendar.SECOND, 0);
                    selectedDate.set(Calendar.MILLISECOND, 0);

                    presenter.addToPlan(currentMeal, selectedDate.getTimeInMillis());
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        c.add(Calendar.DAY_OF_YEAR, 6);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        datePickerDialog.show();
    }


    @Override
    public void showMealDetails(Meal meal) {
        currentMeal = meal;
        binding.collapsingToolbar.setTitle(meal.getStrMeal());
        binding.tvMealName.setText(meal.getStrMeal());
        binding.tvCategoryChip.setText(meal.getStrCategory());
        binding.tvInstructions.setText(meal.getStrInstructions());

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivMealDetails);

        ingredientsAdapter.setList(meal.getIngredientsWithMeasures());

        VideoHelper.setupVideo(
                this,
                meal.getStrYoutube(),
                binding.ivVideoThumbnail,
                binding.youtubePlayerView,
                binding.playerPlaceholder,
                binding.tvNoVideo
        );

        binding.cvYoutube.setVisibility(View.VISIBLE);
        binding.nestedScrollView.setVisibility(View.VISIBLE);
        binding.btnAddToPlanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.primary));
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void showSuccess(String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.primary));
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void showLoading() {
        binding.loadingOverlay.setVisibility(View.VISIBLE);
        // Hide only content that shouldn't be seen behind loading if needed
        // but typically a semi-transparent overlay is better
    }

    @Override
    public void hideLoading() {
        binding.loadingOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onFavoriteStatusChanged(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite) {
            binding.fabFav.setImageResource(R.drawable.ic_heart_filled);
            binding.fabFav.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.tomato_red)));
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_heart);
            binding.fabFav.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.text_grey)));
        }
    }

    @Override
    public void showNoInternet() {
        CustomAlertDialog.showNoInternet(this);
    }

    @Override
    public void showGuestAlert() {
        CustomAlertDialog.showGuestModeAlert(this, () -> {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.putExtra("destination", "login");
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
}
