package com.example.tablia.presentation.planner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.tablia.R;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.databinding.FragmentPlannerBinding;
import com.example.tablia.presentation.meal_details.view.MealDetailsActivity;
import com.example.tablia.presentation.planner.presenter.PlannerPresenterImpl;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;
import java.util.List;

public class PlannerFragment extends Fragment implements PlannerView, PlannerAdapter.OnPlannerClickListener {

    private FragmentPlannerBinding binding;
    private PlannerPresenterImpl presenter;
    private PlannerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlannerBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new PlannerPresenterImpl(MealsRepository.getInstance(requireContext()), this);
        setupRecyclerView();
        setupCalendar();

        loadMealsForDate(Calendar.getInstance());
    }

    private void setupRecyclerView() {
        adapter = new PlannerAdapter(this);
        binding.rvPlannedMeals.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPlannedMeals.setAdapter(adapter);
    }

    private void setupCalendar() {
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            loadMealsForDate(selectedDate);
        });
    }

    private void loadMealsForDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        presenter.getMealsForDate(calendar.getTimeInMillis());
    }

    @Override
    public void showPlannedMeals(List<MealAppointment> appointments) {
        if (appointments.isEmpty()) {
            binding.rvPlannedMeals.setVisibility(View.GONE);
            binding.tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            binding.rvPlannedMeals.setVisibility(View.VISIBLE);
            binding.tvEmptyState.setVisibility(View.GONE);
            adapter.setList(appointments);
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(requireActivity(), MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    @Override
    public void onRemoveClick(MealAppointment appointment) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remove Plan")
                .setMessage("Are you sure you want to remove this meal from your plan?")
                .setPositiveButton("Yes", (dialog, which) -> presenter.removeMealFromPlan(appointment))
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess(String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.primary));
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.dispose();
        binding = null;
    }
}
