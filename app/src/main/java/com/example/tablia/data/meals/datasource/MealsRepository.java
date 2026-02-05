package com.example.tablia.data.meals.datasource;

import com.example.tablia.data.meals.datasource.local.MealsLocalDataSource;
import com.example.tablia.data.meals.datasource.remote.MealsRemoteDataSoucre;
import com.example.tablia.data.meals.models.AreaResponse;
import com.example.tablia.data.meals.models.CategoryResponse;
import com.example.tablia.data.meals.models.IngredientResponse;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.data.meals.models.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsRepository {

    private final MealsLocalDataSource localDataSource;
    private final MealsRemoteDataSoucre remoteDataSource;
    private static MealsRepository instance = null;

    private MealsRepository(MealsLocalDataSource localDataSource, MealsRemoteDataSoucre remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static MealsRepository getInstance(MealsLocalDataSource localDataSource, MealsRemoteDataSoucre remoteDataSource) {
        if (instance == null) {
            instance = new MealsRepository(localDataSource, remoteDataSource);
        }
        return instance;
    }

    // Remote methods
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return remoteDataSource.getMealsByIngredient(ingredient);
    }

    public Single<MealResponse> getMealById(String id) {
        return remoteDataSource.getMealById(id);
    }

    public Observable<MealResponse> searchMealsByFirstLetter(String firstLetter) {
        return remoteDataSource.searchMealsByFirstLetter(firstLetter);
    }

    public Single<CategoryResponse> listCategories() {
        return remoteDataSource.listCategories();
    }

    public Single<AreaResponse> listAreas() {
        return remoteDataSource.listAreas();
    }

    public Single<IngredientResponse> listIngredients() {
        return remoteDataSource.listIngredients();
    }

    public Observable<MealResponse> filterByCategory(String category) {
        return remoteDataSource.filterByCategory(category);
    }

    public Observable<MealResponse> filterByArea(String area) {
        return remoteDataSource.filterByArea(area);
    }

    // Local methods
    public Observable<List<Meal>> getAllFavMeals() {
        return localDataSource.getAllFavMeals();
    }

    public Completable insertFavMeal(Meal meal) {
        return localDataSource.insertFavMeal(meal);
    }

    public Completable deleteFavMeal(Meal meal) {
        return localDataSource.deleteFavMeal(meal);
    }

    public Single<Boolean> isMealFavorite(String id) {
        return localDataSource.isMealFavorite(id);
    }

    public Single<Meal> getFavMealById(String id) {
        return localDataSource.getFavMealById(id);
    }

    public Completable clearAllFavorites() {
        return localDataSource.clearAllFavorites();
    }

    public Observable<List<MealAppointment>> getAllAppointments() {
        return localDataSource.getAllAppointments();
    }

    public Completable insertAppointment(MealAppointment appointment) {
        return localDataSource.insertAppointment(appointment);
    }

    public Completable insertAllFavMeals(List<Meal> meals) {
        return localDataSource.insertAllFavMeals(meals);
    }

    public Completable insertAllAppointments(List<MealAppointment> appointments) {
        return localDataSource.insertAllAppointments(appointments);
    }

    public Completable deleteAppointment(MealAppointment appointment) {
        return localDataSource.deleteAppointment(appointment);
    }

    public Completable clearAllAppointments() {
        return localDataSource.clearAllAppointments();
    }

    public Observable<List<MealAppointment>> getAppointmentsForMeal(String mealId) {
        return localDataSource.getAppointmentsForMeal(mealId);
    }
}
