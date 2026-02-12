package com.example.tablia.presentation.planner.presenter;

import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.presentation.planner.view.PlannerView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenterImpl implements PlannerPresenter {

    private final MealsRepository repository;
    private final AuthRepository authRepository;
    private final PlannerView view;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public PlannerPresenterImpl(MealsRepository repository, AuthRepository authRepository, PlannerView view) {
        this.repository = repository;
        this.authRepository = authRepository;
        this.view = view;
    }

    @Override
    public void getMealsForDate(long timestamp) {
        disposable.add(authRepository.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn -> {
                    if (isLoggedIn) {
                        fetchMealsForDate(timestamp);
                    } else {
                        if (view != null) view.showGuestAlert();
                    }
                }, throwable -> {
                    if (view != null) view.showError(throwable.getMessage());
                }));
    }

    private void fetchMealsForDate(long timestamp) {
        disposable.add(repository.getAppointmentsByDate(timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        appointments -> {
                            if (view != null) view.showPlannedMeals(appointments);
                        },
                        throwable -> {
                            if (view != null) view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void removeMealFromPlan(MealAppointment appointment) {
        disposable.add(repository.deleteAppointment(appointment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (view != null) view.showSuccess("Meal removed from plan");
                        },
                        throwable -> {
                            if (view != null) view.showError(throwable.getMessage());
                        }
                ));
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
