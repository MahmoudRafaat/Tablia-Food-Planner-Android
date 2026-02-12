package com.example.tablia.presentation.search.explore.view;

import java.util.List;

public interface ExploreListView {
    void showData(List<?> data);

    void showError(String message);

    void showLoading();

    void hideLoading();
}
