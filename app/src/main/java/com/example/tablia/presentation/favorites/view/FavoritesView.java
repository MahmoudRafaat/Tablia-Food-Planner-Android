
package com.example.tablia.presentation.favorites.view;

import com.example.tablia.data.meals.models.Meal;
import java.util.List;

public interface FavoritesView {
    void showLoading();
    void hideLoading();
    void showFavorites(List<Meal> favorites);
    void showEmptyMessage();
    void showError(String message);
    void onMealDeleted();
}
