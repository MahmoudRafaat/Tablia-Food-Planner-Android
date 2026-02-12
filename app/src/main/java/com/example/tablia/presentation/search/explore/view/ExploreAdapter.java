package com.example.tablia.presentation.search.explore.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.models.Area;
import com.example.tablia.data.meals.models.Category;
import com.example.tablia.data.meals.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private List<Object> items = new ArrayList<>();

    public ExploreAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<?> list) {
        this.items = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = items.get(position);
        String name;
        String imageUrl = "";

        if (item instanceof Category) {
            Category cat = (Category) item;
            name = cat.getStrCategory();
            imageUrl = cat.getStrCategoryThumb();
        } else if (item instanceof Area) {
            Area area = (Area) item;
            name = area.getStrArea();
            imageUrl = area.getFlagUrl();
        } else if (item instanceof Ingredient) {
            Ingredient ing = (Ingredient) item;
            name = ing.getStrIngredient();
            imageUrl = ing.getImageUrl();
        } else {
            name = "";
        }

        holder.tvName.setText(name);


        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_utensils)
                .into(holder.ivIcon);


        holder.itemView.setOnClickListener(v -> listener.onItemClick(name));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String name);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivExploreIcon);
            tvName = itemView.findViewById(R.id.tvExploreName);
        }
    }
}
