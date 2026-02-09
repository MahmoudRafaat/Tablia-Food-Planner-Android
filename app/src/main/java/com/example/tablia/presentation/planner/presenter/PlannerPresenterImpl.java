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
    public void removeMealFromPlan(String appointmentId) {
        // We first need the full object to delete from Room if we use @Delete, 
        // but since we have the ID, we can handle it via repository.
        // For simplicity here, assuming repository has a delete by ID or we fetch first.
        
        // As per current repo structure: deleteAppointment(MealAppointment)
        // I'll assume we pass the object from the adapter.
    }

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
