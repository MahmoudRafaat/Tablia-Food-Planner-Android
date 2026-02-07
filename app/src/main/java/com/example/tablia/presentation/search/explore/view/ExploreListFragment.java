package com.example.tablia.presentation.search.explore.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tablia.R;
import com.example.tablia.data.meals.datasource.MealsRepository;
import com.example.tablia.databinding.FragmentExploreListBinding;
import com.example.tablia.presentation.search.explore.presenter.ExploreListPresenter;
import com.example.tablia.presentation.search.explore.presenter.ExploreListPresenterImpl;
import com.example.tablia.utils.CustomAlertDialog;

import java.util.List;

public class ExploreListFragment extends Fragment implements ExploreListView, ExploreAdapter.OnItemClickListener {

    private FragmentExploreListBinding binding;
    private ExploreListPresenter presenter;
    private ExploreAdapter adapter;
    private String type;

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

        initViews();
        setupRecyclerView();

        MealsRepository repository = MealsRepository.getInstance(getContext());
        presenter = new ExploreListPresenterImpl(this, repository);

        loadData();
    }

    private void initViews() {
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        if ("category".equals(type)) {
            binding.tvExploreTitle.setText(R.string.select_category);
        } else if ("area".equals(type)) {
            binding.tvExploreTitle.setText(R.string.select_country);
        } else if ("ingredient".equals(type)) {
            binding.tvExploreTitle.setText(R.string.select_ingredient);
        }
    }

    private void setupRecyclerView() {
        binding.rvExplore.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new ExploreAdapter(this);
        binding.rvExplore.setAdapter(adapter);
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
    public void showData(List<?> data) {
        adapter.setList(data);
    }

    @Override
    public void showError(String message) {
        if (getActivity() != null) {
            CustomAlertDialog.showError(getActivity(), message);
        }
    }

    @Override
    public void showLoading() {
        binding.progressBarExplore.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.progressBarExplore.setVisibility(View.GONE);
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
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter instanceof ExploreListPresenterImpl) {
            ((ExploreListPresenterImpl) presenter).dispose();
        }
    }
}
