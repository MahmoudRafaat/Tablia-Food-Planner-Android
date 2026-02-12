package com.example.tablia.presentation.search.explore.presenter;

import android.content.Context;
import android.util.Log;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.presentation.search.explore.view.ExploreListView;
import com.example.tablia.utils.NetworkUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExploreListPresenterImpl implements ExploreListPresenter {

    private final ExploreListView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private boolean isConnected = true;

    public ExploreListPresenterImpl(ExploreListView view, MealsRepository repository) {
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
                            }
                        }, throwable -> {
                            Log.e("ExploreListPresenter", "Network error", throwable);
                        })
        );
    }

    @Override
    public void getCategories() {
        if (!isConnected) return;
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
        if (!isConnected) return;
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
        if (!isConnected) return;
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

    @Override
    public void dispose() {
        disposable.clear();
    }
}
