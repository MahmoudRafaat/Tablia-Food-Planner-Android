package com.example.tablia.data.meals.datasource;

import android.content.Context;

import com.example.tablia.data.db.AppDatabase;
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
    private static MealsRepository instance ;

    private MealsRepository(Context context) {
        this.localDataSource = MealsLocalDataSource.getInstance(AppDatabase.getInstance(context).mealDao());
        this.remoteDataSource = MealsRemoteDataSoucre.getInstance();
    }

    public static MealsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new MealsRepository(context);
        }
        return instance;
    }
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
    public Observable<MealResponse> searchMealsByName(String name) {
        return remoteDataSource.searchMealsByName(name);
    }


    public Observable<List<Meal>> getAllFavMeals() {
        return localDataSource.getAllFavMeals();
    }

    public Completable insertFavMeal(Meal meal) {
        return localDataSource.insertFavMeal(meal)
                .andThen(remoteDataSource.addFavorite(meal));
    }

    public Completable deleteFavMeal(Meal meal) {
        return localDataSource.deleteFavMeal(meal)
                .andThen(remoteDataSource.removeFavorite(meal.getIdMeal()));
    }

    public Single<Boolean> isMealFavorite(String id) {
        return localDataSource.isMealFavorite(id);
    }


    public Completable clearAllFavorites() {
        return localDataSource.clearAllFavorites();
    }

    public Observable<List<MealAppointment>> getAllAppointments() {
        return localDataSource.getAllAppointments();
    }

    public Observable<List<MealAppointment>> getAppointmentsByDate(long timestamp) {
        return localDataSource.getAppointmentsByDate(timestamp);
    }

    public Completable insertAppointment(MealAppointment appointment) {
        return localDataSource.insertAppointment(appointment)
                .andThen(remoteDataSource.addAppointment(appointment));
    }

    public Completable deleteAppointment(MealAppointment appointment) {
        return localDataSource.deleteAppointment(appointment)
                .andThen(remoteDataSource.removeAppointment(appointment.getId()));
    }

    public Completable clearAllAppointments() {
        return localDataSource.clearAllAppointments();
    }



    public Completable syncFavoritesWithRemote() {
        return remoteDataSource.getFavorites()
                .flatMapCompletable(localDataSource::insertAllFavMeals);
    }

    public Completable syncAppointmentsWithRemote() {
        return remoteDataSource.getAppointments()
                .flatMapCompletable(appointments -> localDataSource.insertAllAppointments(appointments));
    }
}
