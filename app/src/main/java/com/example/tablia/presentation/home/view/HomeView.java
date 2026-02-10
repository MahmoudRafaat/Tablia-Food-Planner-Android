package com.example.tablia.presentation.home.view;

import com.example.tablia.data.meals.models.Meal;
import java.util.List;

public interface HomeView {
    void showRandomMeal(Meal meal);
    void showPopularMeals(List<Meal> meals);
    void showError(String message);
    void showNoInternet();
    void hideNoInternet();
    void showLoading();
    void hideLoading();
    void onMealAddedToFavorites(String message);
    void onMealRemovedFromFavorites(String message);
    void updateFavoriteStatus(String mealId, boolean isFavorite);
}
