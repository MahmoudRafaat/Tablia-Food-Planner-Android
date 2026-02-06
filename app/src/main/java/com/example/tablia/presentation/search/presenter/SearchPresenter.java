package com.example.tablia.presentation.search.presenter;

import com.example.tablia.data.meals.models.Meal;

public interface SearchPresenter {
    void getCategories();
    void getAreas();
    void getIngredients();
    void searchMeals(String query);
    void addToFavorite(Meal meal);
    void removeFromFavorite(Meal meal);
    void dispose();
}
