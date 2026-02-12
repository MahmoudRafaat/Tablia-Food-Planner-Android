package com.example.tablia.presentation.planner.view;

import com.example.tablia.data.meals.models.MealAppointment;

import java.util.List;

public interface PlannerView {
    void showPlannedMeals(List<MealAppointment> appointments);

    void showError(String message);

    void showSuccess(String message);

    void showGuestAlert();
}
