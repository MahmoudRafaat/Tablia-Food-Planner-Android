package com.example.tablia.presentation.meal_details.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.databinding.ActivityMealDetailsBinding;
import com.example.tablia.presentation.meal_details.presenter.MealDetailsPresenter;
import com.example.tablia.presentation.meal_details.presenter.MealDetailsPresenterImpl;
import com.example.tablia.utils.VideoHelper;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsView {

    private ActivityMealDetailsBinding binding;
    private MealDetailsPresenter presenter;
    private IngredientAdapter ingredientsAdapter;
    private Meal currentMeal;
    private boolean isFavorite = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new MealDetailsPresenterImpl(MealsRepository.getInstance(this), this);

        setupToolbar();
        setupRecyclerView();

        getLifecycle().addObserver(binding.youtubePlayerView);

        String mealId = getIntent().getStringExtra("mealId");
        if (mealId != null) {
            presenter.getMealDetails(mealId);
            presenter.checkIsFavorite(mealId);
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
                    presenter.removeFromFavorites(currentMeal);
                } else {
                    presenter.addToFavorites(currentMeal);
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        ingredientsAdapter = new IngredientAdapter(null);
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        binding.rvIngredients.setAdapter(ingredientsAdapter);
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
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onFavoriteStatusChanged(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite) {
            binding.fabFav.setImageResource(R.drawable.ic_heart_filled);
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_heart);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
}
