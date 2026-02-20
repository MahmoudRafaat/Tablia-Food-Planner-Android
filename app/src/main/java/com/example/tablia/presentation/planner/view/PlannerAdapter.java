package com.example.tablia.presentation.planner.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablia.R;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {

    private final OnPlannerClickListener listener;
    private List<MealAppointment> appointments = new ArrayList<>();

    public PlannerAdapter(OnPlannerClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<MealAppointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planned_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealAppointment appointment = appointments.get(position);
        Meal meal = appointment.getMeal();

        if (meal != null) {
            holder.tvName.setText(meal.getStrMeal());
            Glide.with(holder.itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .into(holder.ivMeal);

            holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
        }

        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClick(appointment));
    }

    @Override
    public int getItemCount() {
        return appointments != null ? appointments.size() : 0;
    }

    public interface OnPlannerClickListener {
        void onMealClick(Meal meal);

        void onRemoveClick(MealAppointment appointment);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvName, tvDate;
        MaterialButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.iv_planned_meal);
            tvName = itemView.findViewById(R.id.tv_planned_meal_name);
            btnRemove = itemView.findViewById(R.id.btn_remove_plan);
        }
    }
}
