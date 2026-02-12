package com.example.tablia.presentation.home.presenter;

import android.content.Context;

public interface HomePresenter {
    void getRandomMeal();

    void getPopularMeals();

    void observeNetwork(Context context);

    void dispose();
}
