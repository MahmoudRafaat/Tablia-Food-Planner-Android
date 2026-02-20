package com.example.tablia.presentation.planner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tablia.R;
import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.example.tablia.databinding.FragmentPlannerBinding;
import com.example.tablia.presentation.auth.AuthActivity;
import com.example.tablia.presentation.meal_details.view.MealDetailsActivity;
import com.example.tablia.presentation.planner.presenter.PlannerPresenterImpl;
import com.example.tablia.utils.CustomAlertDialog;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlannerFragment extends Fragment implements PlannerView, PlannerAdapter.OnPlannerClickListener {

    private FragmentPlannerBinding binding;
    private PlannerPresenterImpl presenter;
    private PlannerAdapter adapter;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthRepository authRepository = new AuthRepository(getContext());
        presenter = new PlannerPresenterImpl(MealsRepository.getInstance(getContext()), authRepository, this);
        adapter = new PlannerAdapter(this);
        binding.rvPlannedMeals.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPlannedMeals.setAdapter(adapter);
        
        setupCalendar();
        
        Calendar today = Calendar.getInstance();
        updateSelectedDayText(today);
        loadMealsForDate(today);
    }

    private void setupCalendar() {
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            updateSelectedDayText(selectedDate);
            loadMealsForDate(selectedDate);
        });
    }

    private void updateSelectedDayText(Calendar calendar) {
        if (binding != null) {
            binding.tvSelectedDay.setText(dateFormat.format(calendar.getTime()));
        }
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
            binding.emptyStateContainer.setVisibility(View.VISIBLE);
        } else {
            binding.rvPlannedMeals.setVisibility(View.VISIBLE);
            binding.emptyStateContainer.setVisibility(View.GONE);
            adapter.setList(appointments);
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(requireActivity(), MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRemoveClick(MealAppointment appointment) {
        CustomAlertDialog.showConfirmation(
                requireContext(),
                getString(R.string.remove_plan),
                getString(R.string.are_you_sure_you_want_to_remove_this_meal_from_your_plan),
                () -> presenter.removeMealFromPlan(appointment)
        );
    }

    @Override
    public void showError(String message) {
        if (binding != null) {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showSuccess(String message) {
        if (binding != null) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.primary));
            snackbar.show();
        }
    }

    @Override
    public void showGuestAlert() {
        if (getContext() != null) {
            CustomAlertDialog.showGuestModeAlert(requireContext(), () -> {
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.putExtra("destination", "login");
                startActivity(intent);
                requireActivity().finish();
            });
            showPlannedMeals(java.util.Collections.emptyList());
        }
    }

    @Override
    public void showLoading() {
        if (binding != null) {
            binding.loadingIndicator.setVisibility(View.VISIBLE);
            binding.rvPlannedMeals.setVisibility(View.GONE);
            binding.emptyStateContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        if (binding != null) {
            binding.loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.dispose();
        }
        binding = null;
    }
}
