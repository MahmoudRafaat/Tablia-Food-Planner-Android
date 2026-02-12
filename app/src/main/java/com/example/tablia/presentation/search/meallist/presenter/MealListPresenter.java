package com.example.tablia.presentation.search.meallist.presenter;

import com.example.tablia.data.meals.models.Meal;

public interface MealListPresenter {
    void getMealsByCategory(String category);

    void getMealsByArea(String area);

    void getMealsByIngredient(String ingredient);

    void searchMeals(String query);

    void addToFavorite(Meal meal);

    void removeFromFavorite(Meal meal);

    void dispose();
}
