package com.example.tablia.presentation.planner.presenter;

public interface PlannerPresenter {
    void getMealsForDate(long timestamp);
    void removeMealFromPlan(String appointmentId);
    void dispose();
}
