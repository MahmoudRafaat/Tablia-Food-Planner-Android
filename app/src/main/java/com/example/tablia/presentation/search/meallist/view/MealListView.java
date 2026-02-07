package com.example.tablia.presentation.search.meallist.view;

import com.example.tablia.data.meals.models.Meal;
import java.util.List;

public interface MealListView {
    void showMeals(List<Meal> meals);
    void showError(String message);
    void showLoading();
    void hideLoading();
}
