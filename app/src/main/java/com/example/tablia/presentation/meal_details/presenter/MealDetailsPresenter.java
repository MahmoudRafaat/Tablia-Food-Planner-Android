package com.example.tablia.presentation.meal_details.presenter;

import com.example.tablia.data.meals.models.Meal;

public interface MealDetailsPresenter {
    void getMealDetails(String mealId);
    void addToFavorites(Meal meal);
    void removeFromFavorites(Meal meal);
    void checkIsFavorite(String mealId);
    void dispose();
}
