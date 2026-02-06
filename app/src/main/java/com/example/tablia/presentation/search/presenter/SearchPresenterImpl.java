package com.example.tablia.presentation.search.presenter;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealResponse;
import com.example.tablia.presentation.search.view.SearchView;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchPresenterImpl implements SearchPresenter {

    private final SearchView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    public SearchPresenterImpl(SearchView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
        initSearch();
    }

    private void initSearch() {
        disposable.add(searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(query -> {
                    if (query.trim().isEmpty()) {
                        view.showExploreMode();
                    } else {
                        view.showSearchMode();
                        view.showLoading();
                        view.showMeals(Collections.emptyList()); 
                    }
                })
                .filter(query -> !query.trim().isEmpty())
                .switchMap(query -> repository.searchMealsByName(query)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(throwable -> new MealResponse()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                                view.showMeals(response.getMeals());
                            } else {
                                view.showEmptyView();
                            }
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                            view.showEmptyView();
                        }
                ));
    }

    @Override
    public void getCategories() {
        view.showLoading();
        disposable.add(repository.listCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            view.showCategories(response.getCategories());
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void getAreas() {
        disposable.add(repository.listAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showAreas(response.getAreas()),
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void getIngredients() {
        disposable.add(repository.listIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showIngredients(response.getIngredients()),
                        throwable -> view.showError(throwable.getMessage())
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
                .subscribe(() -> {
                }, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        disposable.add(repository.deleteFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
