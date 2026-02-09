package com.example.tablia.presentation.search.meallist.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tablia.R;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.databinding.FragmentMealListBinding;
import com.example.tablia.presentation.home.view.PopularMealAdapter;
import com.example.tablia.presentation.meal_details.view.MealDetailsActivity;
import com.example.tablia.presentation.search.meallist.presenter.MealListPresenter;
import com.example.tablia.presentation.search.meallist.presenter.MealListPresenterImpl;
import com.example.tablia.utils.CustomAlertDialog;

import java.util.List;

public class MealListFragment extends Fragment implements MealListView, PopularMealAdapter.OnMealClickListener {

    private FragmentMealListBinding binding;
    private MealListPresenter presenter;
    private PopularMealAdapter adapter;
    private String filterType;
    private String filterValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMealListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            filterType = getArguments().getString("filterType");
            filterValue = getArguments().getString("filterValue");
        }

        initViews();
        setupRecyclerView();
        setupSearchView();

        MealsRepository repository = MealsRepository.getInstance(getContext());
        presenter = new MealListPresenterImpl(this, repository);

        loadMeals();
    }

    private void initViews() {
        binding.btnBackMeals.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        
        if (filterValue != null) {
            binding.chipFilter.setText(filterValue);
            binding.chipFilter.setVisibility(View.VISIBLE);
        } else {
            binding.chipFilter.setVisibility(View.GONE);
        }

        binding.chipFilter.setOnCloseIconClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    private void setupRecyclerView() {
        binding.rvMeals.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new PopularMealAdapter(this);
        binding.rvMeals.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.searchViewMeals.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchMeals(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.searchMeals(newText);
                return true;
            }
        });
    }

    private void loadMeals() {
        if (filterType == null || filterValue == null) return;

        switch (filterType) {
            case "category":
                presenter.getMealsByCategory(filterValue);
                break;
            case "area":
                presenter.getMealsByArea(filterValue);
                break;
            case "ingredient":
                presenter.getMealsByIngredient(filterValue);
                break;
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        adapter.setList(meals);
        binding.tvFoundCount.setText(getString(R.string.found_recipes_count, meals != null ? meals.size() : 0));
    }

    @Override
    public void showError(String message) {
        if (getActivity() != null) {
            CustomAlertDialog.showError(getActivity(), message);
        }
    }

    @Override
    public void showLoading() {
        binding.progressBarMeals.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.progressBarMeals.setVisibility(View.GONE);
    }

    @Override
    public void onFavoriteClick(Meal meal) {
        if (meal.isFavorite()) {
            presenter.removeFromFavorite(meal);
            meal.setFavorite(false);
        } else {
            presenter.addToFavorite(meal);
            meal.setFavorite(true);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dispose();
        }
    }
}
