package com.example.tablia.presentation.meal_details.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
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
import com.example.tablia.presentation.planner.view.CurrentWeekDecorator;
import com.example.tablia.presentation.planner.view.WeekDayDecorator;
import com.example.tablia.utils.CustomAlertDialog;
import com.example.tablia.utils.VideoHelper;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

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
        binding.rvIngredients.setAdapter(ingredientsAdapter);
        
        getLifecycle().addObserver(binding.youtubePlayerView);

        Meal meal = getIntent().getParcelableExtra("meal");
        if (meal != null) {
            currentMeal = meal;
            showPartialMealDetails(meal);
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

    private void showPartialMealDetails(Meal meal) {
        binding.collapsingToolbar.setTitle(meal.getStrMeal());
        binding.tvMealName.setText(meal.getStrMeal());
        binding.tvCategoryChip.setText(meal.getStrCategory());
        
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.ic_chef_hat)
                .into(binding.ivMealDetails);

        binding.nestedScrollView.setVisibility(View.VISIBLE);
        binding.btnAddToPlanner.setVisibility(View.VISIBLE);
    }

    private void showDatePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDatePickerDialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_calendar, null);
        builder.setView(dialogView);

        MaterialCalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        // minDate is TODAY
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.HOUR_OF_DAY, 0);
        minDate.set(Calendar.MINUTE, 0);
        minDate.set(Calendar.SECOND, 0);
        minDate.set(Calendar.MILLISECOND, 0);

        // maxDate is TODAY + 6 days
        Calendar maxDate = (Calendar) minDate.clone();
        maxDate.add(Calendar.DAY_OF_YEAR, 6);
        maxDate.set(Calendar.HOUR_OF_DAY, 23);
        maxDate.set(Calendar.MINUTE, 59);
        maxDate.set(Calendar.SECOND, 59);
        maxDate.set(Calendar.MILLISECOND, 999);

        calendarView.state().edit()
                .setMinimumDate(minDate)
                .setMaximumDate(maxDate)
                .commit();

        calendarView.addDecorators(
                new WeekDayDecorator(this),
                new CurrentWeekDecorator(this)
        );

        AlertDialog dialog = builder.create();
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                Calendar selectedDate = date.getCalendar();
                // Safety check (redundant because of setMinimumDate)
                if (selectedDate.before(minDate)) {
                    showError(getString(R.string.cannot_select_past_date));
                    return;
                }

                selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                selectedDate.set(Calendar.MINUTE, 0);
                selectedDate.set(Calendar.SECOND, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);

                presenter.addToPlan(currentMeal, selectedDate.getTimeInMillis());
                dialog.dismiss();
            }
        });

        dialog.show();
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
        binding.toggleGroup.setVisibility(View.VISIBLE);
        binding.rvIngredients.setVisibility(View.VISIBLE);
        binding.detailsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showCustomSnackbar(message, true);
    }

    @Override
    public void showSuccess(String message) {
        showCustomSnackbar(message, false);
    }

    private void showCustomSnackbar(String message, boolean isError) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
        if (binding.btnAddToPlanner.getVisibility() == View.VISIBLE) {
            snackbar.setAnchorView(binding.btnAddToPlanner);
        }
        snackbar.setBackgroundTint(ContextCompat.getColor(this, isError ? R.color.tomato_red : R.color.primary));
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    @Override
    public void showLoading() {
        if (currentMeal == null || currentMeal.getStrInstructions() == null || currentMeal.getStrInstructions().isEmpty()) {
            binding.loadingOverlay.setVisibility(View.VISIBLE);
        } else {
            binding.detailsProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        binding.loadingOverlay.setVisibility(View.GONE);
        binding.detailsProgressBar.setVisibility(View.GONE);
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
