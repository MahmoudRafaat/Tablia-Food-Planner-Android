package com.example.tablia.presentation.favorites.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablia.R;
import com.example.tablia.data.auth.AuthRepository;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.presentation.auth.AuthActivity;
import com.example.tablia.presentation.favorites.presenter.FavoritesPresenter;
import com.example.tablia.presentation.favorites.presenter.FavoritesPresenterImpl;
import com.example.tablia.presentation.meal_details.view.MealDetailsActivity;
import com.example.tablia.utils.CustomAlertDialog;

import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesView, FavoriteMealAdapter.OnFavoriteClickListener {

    private RecyclerView rvFavorites;
    private TextView tvSavedMealsCount;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyState;
    private FavoriteMealAdapter adapter;
    private FavoritesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        initPresenter();
        presenter.loadFavorites();
    }

    private void initViews(View view) {
        rvFavorites = view.findViewById(R.id.rv_favorites);
        tvSavedMealsCount = view.findViewById(R.id.tv_saved_meals_count);
        progressBar = view.findViewById(R.id.progress_bar);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
    }

    private void setupRecyclerView() {
        adapter = new FavoriteMealAdapter();
        adapter.setOnFavoriteClickListener(this);
        rvFavorites.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvFavorites.setAdapter(adapter);
    }

    private void initPresenter() {
        MealsRepository repository = MealsRepository.getInstance(getContext());
        AuthRepository authRepository = new AuthRepository(requireContext());
        presenter = new FavoritesPresenterImpl(this, repository, authRepository);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFavorites(List<Meal> favorites) {
        layoutEmptyState.setVisibility(View.GONE);
        rvFavorites.setVisibility(View.VISIBLE);
        tvSavedMealsCount.setVisibility(View.VISIBLE);
        tvSavedMealsCount.setText(getString(R.string.saved_meals_count, favorites.size()));
        adapter.setFavorites(favorites);
    }

    @Override
    public void showEmptyMessage() {
        rvFavorites.setVisibility(View.GONE);
        tvSavedMealsCount.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealDeleted() {
        presenter.loadFavorites();
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    @Override
    public void onRemoveClick(Meal meal) {
        CustomAlertDialog.showConfirmation(
                requireContext(),
                getString(R.string.remove_from_favorites),
                getString(R.string.are_you_sure_you_want_to_delete_from_favorites),
                () -> presenter.removeFavorite(meal)
        );
    }

    @Override
    public void showGuestAlert() {
        CustomAlertDialog.showGuestModeAlert(requireContext(), () -> {
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.putExtra("destination", "login");
            startActivity(intent);
            requireActivity().finish();
        });
        showEmptyMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
