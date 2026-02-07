
package com.example.tablia.presentation.favorites.presenter;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.favorites.view.FavoritesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesPresenterImpl implements FavoritesPresenter {

    private FavoritesView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public FavoritesPresenterImpl(FavoritesView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadFavorites() {
        if (view != null) view.showLoading();
        disposables.add(repository.syncFavoritesWithRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .andThen(repository.getAllFavMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favorites -> {
                            if (view != null) {
                                view.hideLoading();
                                if (favorites.isEmpty()) {
                                    view.showEmptyMessage();
                                } else {
                                    view.showFavorites(favorites);
                                }
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(throwable.getMessage());
                            }
                        }
                ));
    }

    @Override
    public void removeFavorite(Meal meal) {
        disposables.add(repository.deleteFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (view != null) view.onMealDeleted();
                        },
                        throwable -> {
                            if (view != null) view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void detachView() {
        view = null;
        disposables.clear();
    }
}
