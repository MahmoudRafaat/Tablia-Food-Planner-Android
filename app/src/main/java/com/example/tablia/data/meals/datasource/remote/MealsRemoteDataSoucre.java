package com.example.tablia.data.meals.datasource.remote;

import com.example.tablia.data.meals.models.AreaResponse;
import com.example.tablia.data.meals.models.CategoryResponse;
import com.example.tablia.data.meals.models.IngredientResponse;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.data.meals.models.MealResponse;
import com.example.tablia.data.network.FirebaseManager;
import com.example.tablia.data.network.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsRemoteDataSoucre {

    private final MealApiService apiService;
    private final FirebaseManager firebaseManager = FirebaseManager.getInstance();
    private static MealsRemoteDataSoucre instance;

    private MealsRemoteDataSoucre() {
        this.apiService = RetrofitClient.getClient().create(MealApiService.class);
    }

    public static MealsRemoteDataSoucre getInstance() {
        if (instance == null) {
            instance = new MealsRemoteDataSoucre();
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {
        return apiService.getRandomMeal();
    }

    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return apiService.getMealsByIngredient(ingredient);
    }

    public Single<MealResponse> getMealById(String id) {
        return apiService.getMealById(id);
    }

    public Observable<MealResponse> searchMealsByFirstLetter(String firstLetter) {
        return apiService.searchMealsByFirstLetter(firstLetter);
    }

    public Single<CategoryResponse> listCategories() {
        return apiService.listCategories();
    }

    public Single<AreaResponse> listAreas() {
        return apiService.listAreas();
    }

    public Single<IngredientResponse> listIngredients() {
        return apiService.listIngredients();
    }

    public Observable<MealResponse> filterByCategory(String category) {
        return apiService.filterByCategory(category);
    }

    public Observable<MealResponse> filterByArea(String area) {
        return apiService.filterByArea(area);
    }
    public Observable<MealResponse> searchMealsByName(String name) {
        return apiService.searchMealsByName(name);
    }

    public Completable addFavorite(Meal meal) {
        return firebaseManager.addFavorite(meal);
    }

    public Completable removeFavorite(String mealId) {
        return firebaseManager.removeFavorite(mealId);
    }

    public Completable addAppointment(MealAppointment appointment) {
        return firebaseManager.addAppointment(appointment);
    }

    public Completable removeAppointment(String appointmentId) {
        return firebaseManager.removeAppointment(appointmentId);
    }

    public Single<List<Meal>> getFavorites() {
        return firebaseManager.getFavorites();
    }

    public Single<List<MealAppointment>> getAppointments() {
        return firebaseManager.getAppointments();
    }
}
