package com.example.tablia.presentation.meal_details.view;

import com.example.tablia.data.meals.models.Meal;

public interface MealDetailsView {
    void showMealDetails(Meal meal);
    void showError(String message);
    void showLoading();
    void hideLoading();
    void onFavoriteStatusChanged(boolean isFavorite);
}
