package com.example.tablia.presentation.search.main_search.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tablia.R;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Area;
import com.example.tablia.data.meals.models.Category;
import com.example.tablia.data.meals.models.Ingredient;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.databinding.FragmentSearchBinding;
import com.example.tablia.presentation.home.view.PopularMealAdapter;
import com.example.tablia.presentation.meal_details.view.MealDetailsActivity;
import com.example.tablia.presentation.search.main_search.presenter.SearchPresenter;
import com.example.tablia.presentation.search.main_search.presenter.SearchPresenterImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment implements com.example.tablia.presentation.search.main_search.view.SearchView, PopularMealAdapter.OnMealClickListener {

    private FragmentSearchBinding binding;
    private SearchPresenter presenter;
    private PopularMealAdapter searchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MealsRepository repository = MealsRepository.getInstance(getContext());
        presenter = new SearchPresenterImpl(this, repository);
        searchAdapter = new PopularMealAdapter(this);
        binding.rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvSearchResults.setAdapter(searchAdapter);
        binding.cvCategoriesSearch.setOnClickListener(v -> navigateToExplore("category"));
        binding.cvCountriesSearch.setOnClickListener(v -> navigateToExplore("area"));
        binding.cvIngredientsSearch.setOnClickListener(v -> navigateToExplore("ingredient"));
        binding.btnBackToExplore.setOnClickListener(v -> {
            binding.searchView.setQuery("", false);
            showExploreMode();
        });

        setupSearchView();
        presenter.observeNetwork(requireContext());
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void navigateToExplore(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_exploreListFragment, bundle);
    }

    private void navigateToMeals(String type, String value) {
        Bundle bundle = new Bundle();
        bundle.putString("filterType", type);
        bundle.putString("filterValue", value);
        Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_mealListFragment, bundle);
    }

    private void addChip(ChipGroup group, String title, String type) {
        Chip chip = new Chip(requireContext());
        chip.setText(title);
        chip.setOnClickListener(v -> navigateToMeals(type, title));
        group.addView(chip);
    }

    private void addMoreChip(ChipGroup group, int count, String type) {
        Chip chip = new Chip(requireContext());
        chip.setText("+" + count + " More");
        chip.setOnClickListener(v -> navigateToExplore(type));
        group.addView(chip);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (binding == null) return;
        binding.cgCategories.removeAllViews();
        int limit = Math.min(categories.size(), 5);
        for (int i = 0; i < limit; i++) {
            Category category = categories.get(i);
            addChip(binding.cgCategories, category.getStrCategory(), "category");
        }
        if (categories.size() > 5) {
            addMoreChip(binding.cgCategories, categories.size() - 5, "category");
        }
    }

    @Override
    public void showAreas(List<Area> areas) {
        if (binding == null) return;
        binding.cgCountries.removeAllViews();
        int limit = Math.min(areas.size(), 5);
        for (int i = 0; i < limit; i++) {
            Area area = areas.get(i);
            addChip(binding.cgCountries, area.getStrArea(), "area");
        }
        if (areas.size() > 5) {
            addMoreChip(binding.cgCountries, areas.size() - 5, "area");
        }
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        if (binding == null) return;
        binding.cgIngredients.removeAllViews();
        int limit = Math.min(ingredients.size(), 5);
        for (int i = 0; i < limit; i++) {
            Ingredient ingredient = ingredients.get(i);
            addChip(binding.cgIngredients, ingredient.getStrIngredient(), "ingredient");
        }
        if (ingredients.size() > 5) {
            addMoreChip(binding.cgIngredients, ingredients.size() - 5, "ingredient");
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (binding == null) return;
        binding.tvNoResults.setVisibility(View.GONE);
        binding.rvSearchResults.setVisibility(View.VISIBLE);
        searchAdapter.setList(meals);
    }

    @Override
    public void showEmptyView() {
        if (binding == null) return;
        searchAdapter.setList(Collections.emptyList());
        binding.rvSearchResults.setVisibility(View.GONE);
        binding.tvNoResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        if (binding == null) return;
        binding.tvNoResults.setVisibility(View.GONE);
        binding.progressBarSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (binding != null) {
            binding.progressBarSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSearchMode() {
        if (binding == null) return;
        binding.scrollExplore.setVisibility(View.GONE);
        binding.rvSearchResults.setVisibility(View.VISIBLE);
        binding.btnBackToExplore.setVisibility(View.VISIBLE);
    }

    @Override
    public void showExploreMode() {
        if (binding == null) return;
        binding.tvNoResults.setVisibility(View.GONE);
        binding.scrollExplore.setVisibility(View.VISIBLE);
        binding.rvSearchResults.setVisibility(View.GONE);
        binding.btnBackToExplore.setVisibility(View.GONE);
    }

    @Override
    public void showNoInternet() {
        if(binding != null) {
            binding.layoutNoInternetSearch.noInternetOverlay.setVisibility(View.VISIBLE);
            binding.scrollExplore.setVisibility(View.GONE);
            binding.rvSearchResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideNoInternet() {
        if(binding != null) {
            binding.layoutNoInternetSearch.noInternetOverlay.setVisibility(View.GONE);
            showExploreMode();
        }
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
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dispose();
        }
    }
}
