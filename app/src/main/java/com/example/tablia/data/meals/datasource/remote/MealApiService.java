package com.example.tablia.data.meals.datasource.remote;

import com.example.tablia.data.meals.models.AreaResponse;
import com.example.tablia.data.meals.models.CategoryResponse;
import com.example.tablia.data.meals.models.IngredientResponse;
import com.example.tablia.data.meals.models.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("filter.php")
    Single<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);

    @GET("search.php")
    Observable<MealResponse> searchMealsByName(@Query("s") String name);

    @GET("search.php")
    Observable<MealResponse> searchMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("categories.php")
    Single<CategoryResponse> listCategories();

    @GET("list.php?a=list")
    Single<AreaResponse> listAreas();

    @GET("list.php?i=list")
    Single<IngredientResponse> listIngredients();

    @GET("filter.php")
    Observable<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Observable<MealResponse> filterByArea(@Query("a") String area);
}
