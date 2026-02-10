package com.example.tablia.presentation.planner.presenter;

import com.example.tablia.data.meals.models.MealAppointment;

public interface PlannerPresenter {
    void getMealsForDate(long timestamp);
    void removeMealFromPlan(MealAppointment appointment);
    void dispose();
}
