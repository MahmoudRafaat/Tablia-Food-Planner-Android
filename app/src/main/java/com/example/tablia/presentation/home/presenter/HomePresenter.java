package com.example.tablia.presentation.home.presenter;

import com.example.tablia.data.meals.models.Meal;

public interface HomePresenter {
    void getRandomMeal();
    void getPopularMeals();
    void addToFavorite(Meal meal);
    void removeFromFavorite(Meal meal);
    void toggleFavorite(Meal meal);
    void checkIsFavorite(Meal meal);
}
