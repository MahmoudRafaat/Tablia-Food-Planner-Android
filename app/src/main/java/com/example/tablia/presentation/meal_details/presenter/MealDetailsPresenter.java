package com.example.tablia.presentation.meal_details.presenter;

import android.content.Context;
import com.example.tablia.data.meals.models.Meal;

public interface MealDetailsPresenter {
    void getMealDetails(Meal meal);
    void addToFavorites(Meal meal);
    void removeFromFavorites(Meal meal);
    void checkIsFavorite(String mealId);
    void addToPlan(Meal meal, long timestamp);
    void observeNetwork(Context context);
    void dispose();
}
