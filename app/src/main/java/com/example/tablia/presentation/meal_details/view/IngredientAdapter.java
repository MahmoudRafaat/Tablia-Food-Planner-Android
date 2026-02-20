package com.example.tablia.presentation.meal_details.view;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Pair<String, String>> ingredients;

    public IngredientAdapter(List<Pair<String, String>> ingredients) {
        this.ingredients = ingredients;
    }

    public void setList(List<Pair<String, String>> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, String> ingredient = ingredients.get(position);
        holder.tvName.setText(ingredient.first);
        holder.tvMeasure.setText(ingredient.second);

        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.first + ".png";
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.ivIngredient);
    }

    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIngredient;
        TextView tvName, tvMeasure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIngredient = itemView.findViewById(R.id.iv_ingredient);
            tvName = itemView.findViewById(R.id.tv_ingredient_name);
            tvMeasure = itemView.findViewById(R.id.tv_ingredient_measure);
        }
    }
}
