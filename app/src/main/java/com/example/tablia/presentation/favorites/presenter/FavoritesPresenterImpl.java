package com.example.tablia.presentation.favorites.presenter;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.favorites.view.FavoritesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesPresenterImpl implements FavoritesPresenter {

    private final MealsRepository repository;
    private final AuthRepository authRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private FavoritesView view;

    public FavoritesPresenterImpl(FavoritesView view, MealsRepository repository, AuthRepository authRepository) {
        this.view = view;
        this.repository = repository;
        this.authRepository = authRepository;
    }

    @Override
    public void loadFavorites() {
        disposables.add(authRepository.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn -> {
                    if (isLoggedIn) {
                        fetchFavorites();
                    } else {
                        if (view != null) view.showGuestAlert();
                    }
                }, throwable -> {
                    if (view != null) view.showError(throwable.getMessage());
                }));
    }

    private void fetchFavorites() {
        if (view != null) view.showLoading();

        disposables.add(repository.getAllFavMeals()
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
