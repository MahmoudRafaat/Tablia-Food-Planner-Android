package com.example.tablia.presentation.favorites.presenter;

import com.example.tablia.data.meals.models.Meal;

public interface FavoritesPresenter {
    void loadFavorites();

    void removeFavorite(Meal meal);

    void detachView();
}
