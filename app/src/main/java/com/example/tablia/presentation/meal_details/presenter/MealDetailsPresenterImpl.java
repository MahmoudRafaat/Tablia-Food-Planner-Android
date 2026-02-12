package com.example.tablia.presentation.meal_details.presenter;

import android.content.Context;
import android.util.Log;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.presentation.meal_details.view.MealDetailsView;
import com.example.tablia.utils.NetworkUtil;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {

    private final MealsRepository repository;
    private final AuthRepository authRepository;
    private final MealDetailsView view;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private boolean isConnected = true;

    public MealDetailsPresenterImpl(MealsRepository repository, AuthRepository authRepository, MealDetailsView view) {
        this.repository = repository;
        this.authRepository = authRepository;
        this.view = view;
    }

    @Override
    public void observeNetwork(Context context) {
        disposable.add(
                NetworkUtil.observeNetwork(context)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(connected -> {
                            this.isConnected = connected;
                        }, throwable -> {
                            Log.e("MealDetailsPresenter", "Network error", throwable);
                        })
        );
    }

    private void checkAuth(Runnable onAuthorized) {
        disposable.add(authRepository.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn -> {
                    if (isLoggedIn) {
                        onAuthorized.run();
                    } else {
                        view.showGuestAlert();
                    }
                }, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void getMealDetails(Meal meal) {
        if (meal == null) return;

        view.showLoading();

        if (meal.getStrInstructions() != null && !meal.getStrInstructions().isEmpty()) {
            view.showMealDetails(meal);
            view.hideLoading();
        } else {
            if (!isConnected) {
                view.hideLoading();
                view.showNoInternet();
                return;
            }
            disposable.add(repository.getMealById(meal.getIdMeal())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                view.hideLoading();
                                if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                                    view.showMealDetails(response.getMeals().get(0));
                                } else {
                                    view.showError("Meal details not found");
                                }
                            },
                            throwable -> {
                                view.hideLoading();
                                view.showError(throwable.getMessage());
                            }
                    ));
        }
    }

    @Override
    public void addToFavorites(Meal meal) {
        checkAuth(() -> {
            if (!isConnected) {
                view.showNoInternet();
                return;
            }
            meal.setFavorite(true);
            disposable.add(repository.insertFavMeal(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.onFavoriteStatusChanged(true),
                            throwable -> view.showError(throwable.getMessage())
                    ));
        });
    }

    @Override
    public void removeFromFavorites(Meal meal) {
        checkAuth(() -> {
            if (!isConnected) {
                view.showNoInternet();
                return;
            }
            meal.setFavorite(false);
            disposable.add(repository.deleteFavMeal(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.onFavoriteStatusChanged(false),
                            throwable -> view.showError(throwable.getMessage())
                    ));
        });
    }

    @Override
    public void checkIsFavorite(String mealId) {
        disposable.add(repository.isMealFavorite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::onFavoriteStatusChanged,
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void addToPlan(Meal meal, long timestamp) {
        checkAuth(() -> {
            if (!isConnected) {
                view.showNoInternet();
                return;
            }
            MealAppointment appointment = new MealAppointment(
                    UUID.randomUUID().toString(),
                    meal,
                    timestamp
            );
            disposable.add(repository.insertAppointment(appointment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.showSuccess("Meal added to plan"),
                            throwable -> view.showError(throwable.getMessage())
                    ));
        });
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
