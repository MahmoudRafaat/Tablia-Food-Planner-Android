package com.example.tablia.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.tablia.data.meals.datasource.local.MealDao;
import com.example.tablia.data.meals.models.Meal;
import com.example.tablia.data.meals.models.MealAppointment;


@Database(entities = {Meal.class, MealAppointment.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "meals_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract MealDao mealDao();
}
