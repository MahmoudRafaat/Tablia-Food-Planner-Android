package com.example.tablia.presentation.search.meallist.presenter;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.search.meallist.view.MealListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MealListPresenterImpl implements MealListPresenter {

    private final MealListView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();
    private List<Meal> originalList = new ArrayList<>();

    public MealListPresenterImpl(MealListView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
        setupSearchDebounce();
    }

    private void setupSearchDebounce() {
        disposable.add(searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    if (query.isEmpty()) {
                        view.showMeals(originalList);
                    } else {
                        filterLocalList(query);
                    }
                }));
    }

    private void filterLocalList(String query) {
        List<Meal> filteredList = new ArrayList<>();
        for (Meal meal : originalList) {
            if (meal.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(meal);
            }
        }
        view.showMeals(filteredList);
    }

    @Override
    public void getMealsByCategory(String category) {
        view.showLoading();
        disposable.add(repository.filterByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            originalList = response.getMeals();
                            view.showMeals(originalList);
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void getMealsByArea(String area) {
        view.showLoading();
        disposable.add(repository.filterByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            originalList = response.getMeals();
                            view.showMeals(originalList);
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        view.showLoading();
        disposable.add(repository.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            originalList = response.getMeals();
                            view.showMeals(originalList);
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void searchMeals(String query) {
        searchSubject.onNext(query);
    }

    @Override
    public void addToFavorite(Meal meal) {
        disposable.add(repository.insertFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        disposable.add(repository.deleteFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
