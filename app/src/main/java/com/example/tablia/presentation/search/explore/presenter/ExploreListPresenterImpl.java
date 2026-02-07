package com.example.tablia.presentation.search.explore.presenter;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.presentation.search.explore.view.ExploreListView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExploreListPresenterImpl implements ExploreListPresenter {

    private final ExploreListView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public ExploreListPresenterImpl(ExploreListView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
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
                            view.showData(response.getCategories());
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void getAreas() {
        view.showLoading();
        disposable.add(repository.listAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            view.showData(response.getAreas());
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void getIngredients() {
        view.showLoading();
        disposable.add(repository.listIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.hideLoading();
                            view.showData(response.getIngredients());
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        }
                ));
    }

    public void dispose() {
        disposable.clear();
    }
}
