package com.example.tablia.presentation.search.main_search.view;

import com.example.tablia.data.meals.models.Area;
import com.example.tablia.data.meals.models.Category;
import com.example.tablia.data.meals.models.Ingredient;
import com.example.tablia.data.meals.models.Meal;

import java.util.List;

public interface SearchView {
    void showCategories(List<Category> categories);

    void showAreas(List<Area> areas);

    void showIngredients(List<Ingredient> ingredients);

    void showMeals(List<Meal> meals);

    void showEmptyView();

    void showError(String message);

    void showLoading();

    void hideLoading();

    void showSearchMode();

    void showExploreMode();

    void showNoInternet();

    void hideNoInternet();

}
