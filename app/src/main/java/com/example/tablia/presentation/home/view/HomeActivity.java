package com.example.tablia.presentation.home.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.db.AppDatabase;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.datasource.local.MealsLocalDataSource;
import com.example.tablia.data.meals.datasource.remote.MealsRemoteDataSoucre;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.home.presenter.HomePresenter;
import com.example.tablia.presentation.home.presenter.HomePresenterImpl;
import com.example.tablia.utils.CustomAlertDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView, PopularMealAdapter.OnFavoriteClickListener {

    private HomePresenter presenter;
    private ImageView ivMealOfDay;
    private TextView tvMealName, tvMealArea;
    private ImageButton btnFavMealOfDay;
    private RecyclerView rvPopularMeals;
    private PopularMealAdapter popularMealAdapter;
    private ProgressBar progressBar;
    private Meal currentRandomMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MealsRepository repository = MealsRepository.getInstance(
                MealsLocalDataSource.getInstance(AppDatabase.getInstance(this).mealDao()),
                MealsRemoteDataSoucre.getInstance()
        );
        presenter = new HomePresenterImpl(this, repository);

        initViews();
        initRecyclerViews();

        presenter.getRandomMeal();
        presenter.getPopularMeals();
    }

    private void initViews() {
        ivMealOfDay = findViewById(R.id.ivMealOfDay);
        tvMealName = findViewById(R.id.tvMealName);
        tvMealArea = findViewById(R.id.tvMealArea);
        btnFavMealOfDay = findViewById(R.id.btnFavMealOfDay);
        progressBar = findViewById(R.id.progressBar);

        btnFavMealOfDay.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                presenter.toggleFavorite(currentRandomMeal);
            }
        });
    }

    private void initRecyclerViews() {
        rvPopularMeals = findViewById(R.id.rvPopularMeals);
        rvPopularMeals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularMealAdapter = new PopularMealAdapter(this);
        rvPopularMeals.setAdapter(popularMealAdapter);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        this.currentRandomMeal = meal;
        tvMealName.setText(meal.getStrMeal());
        tvMealArea.setText(meal.getStrArea());
        Glide.with(this).load(meal.getStrMealThumb()).into(ivMealOfDay);
        presenter.checkIsFavorite(meal);
    }

    @Override
    public void showPopularMeals(List<Meal> meals) {
        popularMealAdapter.setList(meals);
    }

    @Override
    public void showError(String message) {
        CustomAlertDialog.showError(this, message);
    }

    @Override
    public void showNoInternet() {
        CustomAlertDialog.showNoInternet(this);
    }

    @Override
    public void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMealAddedToFavorites(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.primary))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .show();
    }

    @Override
    public void onMealRemovedFromFavorites(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.text_grey))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .show();
    }

    @Override
    public void updateFavoriteStatus(String mealId, boolean isFavorite) {
        // Update Random Meal UI if it's the one
        if (currentRandomMeal != null && currentRandomMeal.getIdMeal().equals(mealId)) {
            currentRandomMeal.setFavorite(isFavorite);
            if (isFavorite) {
                btnFavMealOfDay.setImageResource(R.drawable.ic_heart_filled);
            } else {
                btnFavMealOfDay.setImageResource(R.drawable.ic_heart);
            }
            btnFavMealOfDay.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
        }
        
        // Update Popular Meals UI
        popularMealAdapter.updateFavoriteStatus(mealId, isFavorite);
    }

    @Override
    public void onFavoriteClick(Meal meal) {
        presenter.toggleFavorite(meal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter instanceof HomePresenterImpl) {
            ((HomePresenterImpl) presenter).dispose();
        }
    }
}
