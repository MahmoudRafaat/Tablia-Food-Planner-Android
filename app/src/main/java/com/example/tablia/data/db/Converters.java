package com.example.tablia.data.db;

import androidx.room.TypeConverter;

import com.example.tablia.data.meals.models.Meal;
import com.google.gson.Gson;

public class Converters {
    @TypeConverter
    public static String fromMeal(Meal meal) {
        return meal == null ? null : new Gson().toJson(meal);
    }

    @TypeConverter
    public static Meal toMeal(String mealString) {
        return mealString == null ? null : new Gson().fromJson(mealString, Meal.class);
    }
}
