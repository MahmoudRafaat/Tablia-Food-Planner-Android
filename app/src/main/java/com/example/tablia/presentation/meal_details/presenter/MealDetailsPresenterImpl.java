package com.example.tablia.presentation.meal_details.presenter;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.meal_details.view.MealDetailsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {

    private final MealsRepository repository;
    private final MealDetailsView view;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MealDetailsPresenterImpl(MealsRepository repository, MealDetailsView view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void getMealDetails(String mealId) {
        view.showLoading();
        disposable.add(repository.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                view.showMealDetails(response.getMeals().get(0));
                            } else {
                                view.showError("Meal not found");
                            }
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void addToFavorites(Meal meal) {
        disposable.add(repository.insertFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onFavoriteStatusChanged(true),
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void removeFromFavorites(Meal meal) {
        disposable.add(repository.deleteFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onFavoriteStatusChanged(false),
                        throwable -> view.showError(throwable.getMessage())
                ));
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
    public void dispose() {
        disposable.clear();
    }
}
