package com.example.tablia.presentation.search.explore.view;

import android.os.Bundle;
import android.util.Log;
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
import com.example.tablia.data.meals.models.Area;
import com.example.tablia.data.meals.models.Category;
import com.example.tablia.data.meals.models.Ingredient;
import com.example.tablia.databinding.FragmentExploreListBinding;
import com.example.tablia.presentation.search.explore.presenter.ExploreListPresenter;
import com.example.tablia.presentation.search.explore.presenter.ExploreListPresenterImpl;
import com.example.tablia.utils.CustomAlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ExploreListFragment extends Fragment implements ExploreListView, ExploreAdapter.OnItemClickListener {

    private static final String TAG = "ExploreListFragment";
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();
    private FragmentExploreListBinding binding;
    private ExploreListPresenter presenter;
    private ExploreAdapter adapter;
    private String type;
    private List<Object> originalData = new ArrayList<>();
    private boolean isNetworkAvailable = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getString("type");
        }

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        if ("category".equals(type)) {
            binding.tvExploreTitle.setText(R.string.select_category);
        } else if ("area".equals(type)) {
            binding.tvExploreTitle.setText(R.string.select_country);
        } else if ("ingredient".equals(type)) {
            binding.tvExploreTitle.setText(R.string.select_ingredient);
        }

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSubject.onNext(newText);
                return true;
            }
        });
        binding.rvExplore.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new ExploreAdapter(this);
        binding.rvExplore.setAdapter(adapter);
        disposables.add(searchSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::filterData, throwable -> Log.e(TAG, "Search error", throwable)));

        MealsRepository repository = MealsRepository.getInstance(getContext());
        presenter = new ExploreListPresenterImpl(this, repository);
        presenter.observeNetwork(requireContext());

        loadData();
    }







    private void loadData() {
        if ("category".equals(type)) {
            presenter.getCategories();
        } else if ("area".equals(type)) {
            presenter.getAreas();
        } else if ("ingredient".equals(type)) {
            presenter.getIngredients();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void showData(List<?> data) {
        this.originalData = (List<Object>) data;
        updateAdapter(originalData);
    }

    private void filterData(String query) {
        if (originalData == null || originalData.isEmpty()) {
            return;
        }

        List<Object> filteredList;
        if (query.isEmpty()) {
            filteredList = originalData;
        } else {
            String lowerCaseQuery = query.toLowerCase();
            filteredList = originalData.stream()
                    .filter(item -> getItemName(item).toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList());
        }
        updateAdapter(filteredList);
    }

    private String getItemName(Object item) {
        if (item instanceof Category) {
            return ((Category) item).getStrCategory();
        } else if (item instanceof Area) {
            return ((Area) item).getStrArea();
        } else if (item instanceof Ingredient) {
            return ((Ingredient) item).getStrIngredient();
        }
        return "";
    }

    private void updateAdapter(List<Object> list) {
        adapter.setList(list.stream().limit(26).collect(Collectors.toList()));
    }

    @Override
    public void showError(String message) {
        if (isNetworkAvailable && getActivity() != null) {
            CustomAlertDialog.showError(getActivity(), message);
        }
    }

    @Override
    public void showLoading() {
        if (binding != null) {
            binding.progressBarExplore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (binding != null) {
            binding.progressBarExplore.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoInternet() {
        isNetworkAvailable = false;
        if (binding != null) {
            binding.layoutNoInternetExplore.noInternetOverlay.setVisibility(View.VISIBLE);
            binding.groupExploreContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideNoInternet() {
        isNetworkAvailable = true;
        if (binding != null) {
            binding.layoutNoInternetExplore.noInternetOverlay.setVisibility(View.GONE);
            binding.groupExploreContent.setVisibility(View.VISIBLE);
            loadData();
        }
    }

    @Override
    public void onItemClick(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("filterType", type);
        bundle.putString("filterValue", name);
        Navigation.findNavController(requireView()).navigate(R.id.action_exploreListFragment_to_mealListFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
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
