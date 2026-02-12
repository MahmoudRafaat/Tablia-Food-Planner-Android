package com.example.tablia.presentation.home.view;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class PopularMealAdapter extends RecyclerView.Adapter<PopularMealAdapter.ViewHolder> {

    private List<Meal> meals = new ArrayList<>();
    private final OnMealClickListener listener;

    public interface OnMealClickListener {
        void onFavoriteClick(Meal meal);
        void onMealClick(Meal meal);
    }

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

        updateFavoriteUI(holder, meal.isFavorite());

        holder.btnFav.setOnClickListener(v -> listener.onFavoriteClick(meal));
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    private void updateFavoriteUI(ViewHolder holder, boolean isFavorite) {
        if (isFavorite) {
            holder.btnFav.setImageResource(R.drawable.ic_heart_filled);
            holder.btnFav.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary)));
        } else {
            holder.btnFav.setImageResource(R.drawable.ic_heart);
            holder.btnFav.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary)));
        }
    }

    @Override
    public int getItemCount() {
        return meals != null ? meals.size() : 0;
    }

    public void updateFavoriteStatus(String mealId, boolean isFavorite) {
        if (meals != null) {
            for (int i = 0; i < meals.size(); i++) {
                if (meals.get(i).getIdMeal().equals(mealId)) {
                    meals.get(i).setFavorite(isFavorite);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvName;
        ImageButton btnFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.ivPopularMeal);
            tvName = itemView.findViewById(R.id.tvPopularMealName);
            btnFav = itemView.findViewById(R.id.btnFavPopular);
        }
    }
}
