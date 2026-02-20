package com.example.tablia.presentation.planner.view;

import android.content.Context;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.example.tablia.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class WeekDayDecorator implements DayViewDecorator {

    // This decorator is redundant if we use CurrentWeekDecorator and OutOfRangeDayDecorator
    // efficiently. But let's keep it and make sure it doesn't conflict.
    // Actually, let's just remove its effect or make it a base decorator.
    
    public WeekDayDecorator(Context context) {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return false; // Disable it to avoid conflicts
    }

    @Override
    public void decorate(DayViewFacade view) {
    }
}