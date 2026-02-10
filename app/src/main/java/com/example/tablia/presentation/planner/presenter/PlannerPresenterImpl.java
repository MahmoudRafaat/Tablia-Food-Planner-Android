package com.example.tablia.presentation.planner.presenter;

import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.presentation.planner.view.PlannerView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenterImpl implements PlannerPresenter {

    private final MealsRepository repository;
    private final PlannerView view;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public PlannerPresenterImpl(MealsRepository repository, PlannerView view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void getMealsForDate(long timestamp) {
        disposable.add(repository.getAppointmentsByDate(timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showPlannedMeals,
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void removeMealFromPlan(MealAppointment appointment) {
        disposable.add(repository.deleteAppointment(appointment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showSuccess("Meal removed from plan"),
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}
