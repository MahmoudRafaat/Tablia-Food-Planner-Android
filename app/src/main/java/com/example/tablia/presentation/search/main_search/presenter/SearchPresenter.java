package com.example.tablia.presentation.search.main_search.presenter;

import android.content.Context;

public interface SearchPresenter {
    void getCategories();

    void getAreas();

    void getIngredients();

    void searchMeals(String query);

    void observeNetwork(Context context);

    void dispose();
}
