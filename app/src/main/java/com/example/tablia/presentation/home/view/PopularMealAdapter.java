package com.example.tablia.presentation.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class PopularMealAdapter extends RecyclerView.Adapter<PopularMealAdapter.ViewHolder> {

    private final OnMealClickListener listener;
    private List<Meal> meals = new ArrayList<>();

    public PopularMealAdapter(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Meal> meals) {
        if (meals != null) {
            this.meals = meals;
        } else {
            this.meals = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvName.setText(meal.getStrMeal());
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .into(holder.ivMeal);

        holder.btnRemoveFavorite.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals != null ? meals.size() : 0;
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvName;
        ImageButton btnRemoveFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.ivPopularMeal);
            tvName = itemView.findViewById(R.id.tvPopularMealName);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }
}
