package com.example.tablia.presentation.favorites.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.databinding.ItemPopularMealBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.ViewHolder> {

    private List<Meal> favorites = new ArrayList<>();
    private OnFavoriteClickListener listener;

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    public void setFavorites(List<Meal> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPopularMealBinding binding = ItemPopularMealBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = favorites.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPopularMealBinding binding;

        public ViewHolder(ItemPopularMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onMealClick(favorites.get(position));
                    }
                }
            });

            binding.btnFavPopular.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(favorites.get(position));
                    }
                }
            });
        }

        void bind(Meal meal) {
            binding.tvPopularMealName.setText(meal.getStrMeal());
            Glide.with(itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .into(binding.ivPopularMeal);
            binding.btnFavPopular.setImageResource(R.drawable.ic_heart_filled);
        }
    }

    public interface OnFavoriteClickListener {
        void onMealClick(Meal meal);
        void onRemoveClick(Meal meal);
    }
}
