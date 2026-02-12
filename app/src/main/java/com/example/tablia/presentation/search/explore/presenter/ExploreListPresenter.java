package com.example.tablia.presentation.search.explore.presenter;

import android.content.Context;

public interface ExploreListPresenter {
    void getCategories();

    void getAreas();

    void getIngredients();

    void observeNetwork(Context context);

    void dispose();
}
