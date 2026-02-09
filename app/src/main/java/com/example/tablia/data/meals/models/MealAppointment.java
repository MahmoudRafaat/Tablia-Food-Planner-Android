package com.example.tablia.data.meals.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_appointments")
public class MealAppointment implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private Meal meal;
    private long dateTimestamp;

    public MealAppointment() {
    }

    public MealAppointment(@NonNull String id, Meal meal, long dateTimestamp) {
        this.id = id;
        this.meal = meal;
        this.dateTimestamp = dateTimestamp;
    }

    protected MealAppointment(Parcel in) {
        id = in.readString();
        meal = in.readParcelable(Meal.class.getClassLoader());
        dateTimestamp = in.readLong();
    }

    public static final Creator<MealAppointment> CREATOR = new Creator<MealAppointment>() {
        @Override
        public MealAppointment createFromParcel(Parcel in) {
            return new MealAppointment(in);
        }

        @Override
        public MealAppointment[] newArray(int size) {
            return new MealAppointment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(meal, flags);
        dest.writeLong(dateTimestamp);
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public Meal getMeal() { return meal; }
    public void setMeal(Meal meal) { this.meal = meal; }

    public long getDateTimestamp() { return dateTimestamp; }
    public void setDateTimestamp(long dateTimestamp) { this.dateTimestamp = dateTimestamp; }
}
