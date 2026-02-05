package com.example.tablia.presentation.home.presenter;

import com.example.tablia.data.meals.models.Meal;

public interface HomePresenter {
    void getRandomMeal();
    void getPopularMeals();
    void onMealClick(String mealId);
    void toggleFavorite(Meal meal);
    void checkIsFavorite(Meal meal);
}
