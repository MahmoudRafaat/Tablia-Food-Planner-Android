package com.example.tablia.presentation.profile.presenter;

import android.content.Context;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.presentation.profile.view.ProfileView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenterImp implements ProfilePresenter {
    private final ProfileView view;
    private final AuthRepository authRepository;
    private final MealsRepository mealsRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ProfilePresenterImp(ProfileView view, Context context) {
        this.view = view;
        this.authRepository = new AuthRepository(context);
        this.mealsRepository = MealsRepository.getInstance(context);
    }

    @Override
    public void getProfileDeatails() {
        disposables.add(authRepository.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn -> {
                    if (isLoggedIn) {
                        fetchProfileDetails();
                    } else {
                        view.showGuestAlert();
                    }
                }, throwable -> view.onLogoutFailure("Failed to check login status")));
    }

    private void fetchProfileDetails() {
        disposables.add(authRepository.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showUserInfo,
                        throwable -> view.onLogoutFailure("Failed to load user info")
                ));

        disposables.add(mealsRepository.getAllFavMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showFavoritesCount(meals.size()),
                        throwable -> {
                        }
                ));

        disposables.add(mealsRepository.getAllAppointments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        appointments -> view.showPlannedCount(appointments.size()),
                        throwable -> {
                        }
                ));
    }

    @Override
    public void logout() {
        disposables.add(authRepository.logout()
                .andThen(mealsRepository.clearAllFavorites())
                .andThen(mealsRepository.clearAllAppointments())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::onLogoutSuccess,
                        throwable -> view.onLogoutFailure(throwable.getMessage())
                ));
    }

    public void detachView() {
        disposables.clear();
    }
}
