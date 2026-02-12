package com.example.tablia.data.meals.datasource.remote;

import com.example.tablia.data.meals.models.Area;
import com.example.tablia.data.meals.models.Category;
import com.example.tablia.data.meals.models.Ingredient;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;

import java.util.List;

public interface MealsNetworkResponse {
    void onMealsLoaded(List<Meal> meals);

    void onMealLoaded(Meal meal);

    void onCategoriesLoaded(List<Category> categories);

    void onAreasLoaded(List<Area> areas);

    void onIngredientsLoaded(List<Ingredient> ingredients);

    void onFilteredMealsLoaded(List<Meal> meals);

    void onSearchMealsLoaded(List<Meal> meals);

    void onRandomMealLoaded(Meal meal);

    void onMealAddedToFavorites();

    void onMealRemovedFromFavorites();

    void onMealAddedToAppointments();

    void onMealRemovedFromAppointments();

    void onAppointmentsLoaded(List<MealAppointment> appointments);

    void noInternet();

    void onFailure(String errorMessage);


}
