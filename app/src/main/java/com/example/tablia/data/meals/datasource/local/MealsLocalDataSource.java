package com.example.tablia.data.meals.datasource.local;

import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSource {

    private static MealsLocalDataSource instance = null;
    private final MealDao mealDao;

    private MealsLocalDataSource(MealDao mealDao) {
        this.mealDao = mealDao;
    }

    public static MealsLocalDataSource getInstance(MealDao mealDao) {
        if (instance == null) {
            instance = new MealsLocalDataSource(mealDao);
        }
        return instance;
    }

    public Observable<List<Meal>> getAllFavMeals() {
        return mealDao.getAllFavMeals();
    }

    public Completable insertFavMeal(Meal meal) {
        return mealDao.insertFavMeal(meal);
    }

    public Completable deleteFavMeal(Meal meal) {
        return mealDao.deleteFavMeal(meal);
    }

    public Single<Boolean> isMealFavorite(String id) {
        return mealDao.isMealFavorite(id);
    }

    public Single<Meal> getFavMealById(String id) {
        return mealDao.getFavMealById(id);
    }

    public Completable clearAllFavorites() {
        return mealDao.clearAllFavorites();
    }

    public Observable<List<MealAppointment>> getAllAppointments() {
        return mealDao.getAllAppointments();
    }

    public Observable<List<MealAppointment>> getAppointmentsByDate(long timestamp) {
        return mealDao.getAppointmentsByDate(timestamp);
    }

    public Completable insertAppointment(MealAppointment appointment) {
        return mealDao.insertAppointment(appointment);
    }

    public Completable insertAllFavMeals(List<Meal> meals) {
        return mealDao.insertAllFavMeals(meals);
    }

    public Completable insertAllAppointments(List<MealAppointment> appointments) {
        return mealDao.insertAllAppointments(appointments);
    }

    public Completable deleteAppointment(MealAppointment appointment) {
        return mealDao.deleteAppointment(appointment);
    }

    public Completable clearAllAppointments() {
        return mealDao.clearAllAppointments();
    }


}
