package com.example.tablia.presentation.home.presenter;

import android.content.Context;
import android.util.Log;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.home.view.HomeView;
import com.example.tablia.utils.NetworkUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private final HomeView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private boolean isConnected = true;

    public HomePresenterImpl(HomeView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void observeNetwork(Context context) {
        disposable.add(
                NetworkUtil.observeNetwork(context)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(connected -> {
                            this.isConnected = connected;
                            if (!connected) {
                                view.showNoInternet();
                            } else {
                                view.hideNoInternet();
                                getRandomMeal();
                                getPopularMeals();
                            }
                        }, throwable -> {
                            Log.e("HomePresenter", "Network error", throwable);
                        })
        );
    }

    @Override
    public void getRandomMeal() {
        if (!isConnected) {
            view.showNoInternet();
            return;
        }
        view.showLoading();
        disposable.add(repository.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                Meal meal = response.getMeals().get(0);
                                checkIsFavorite(meal);
                                view.showRandomMeal(meal);
                            }
                        },
                        throwable -> {
                            view.hideLoading();
                            handleError(throwable);
                        }
                ));
    }

    @Override
    public void getPopularMeals() {
        if (!isConnected) return;
        disposable.add(repository.searchMealsByFirstLetter("b")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showPopularMeals(response.getMeals()),
                        throwable -> handleError(throwable)
                ));
    }

    @Override
    public void toggleFavorite(Meal meal) {
        disposable.add(repository.isMealFavorite(meal.getIdMeal())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFav -> {
                            if (isFav) {
                                removeFromFavorite(meal);
                            } else {
                                addToFavorite(meal);
                            }
                        },
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void addToFavorite(Meal meal) {
        meal.setFavorite(true);
        disposable.add(repository.insertFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.onMealAddedToFavorites("Added to favorites successfully");
                            view.updateFavoriteStatus(meal.getIdMeal(), true);
                        },
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        meal.setFavorite(false);
        disposable.add(repository.deleteFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.onMealRemovedFromFavorites("Removed from favorites");
                            view.updateFavoriteStatus(meal.getIdMeal(), false);
                        },
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void checkIsFavorite(Meal meal) {
        disposable.add(repository.isMealFavorite(meal.getIdMeal())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFav -> view.updateFavoriteStatus(meal.getIdMeal(), isFav),
                        throwable -> {}
                ));
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof java.net.UnknownHostException || throwable instanceof java.net.ConnectException) {
            view.showNoInternet();
        } else {
            view.showError(throwable.getMessage());
        }
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
