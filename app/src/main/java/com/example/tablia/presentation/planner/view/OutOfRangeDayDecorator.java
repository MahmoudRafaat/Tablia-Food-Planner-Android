package com.example.tablia.presentation.planner.view;

import android.content.Context;
import android.text.style.ForegroundColorSpan;
import androidx.core.content.ContextCompat;
import com.example.tablia.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class OutOfRangeDayDecorator implements DayViewDecorator {

    private final Calendar startOfWeek;
    private final Calendar endOfWeek;
    private final int color;

    public OutOfRangeDayDecorator(Context context) {
        this.color = ContextCompat.getColor(context, R.color.text_grey);
        startOfWeek = Calendar.getInstance();
        startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.getFirstDayOfWeek());
        startOfWeek.set(Calendar.HOUR_OF_DAY, 0);
        startOfWeek.set(Calendar.MINUTE, 0);
        startOfWeek.set(Calendar.SECOND, 0);
        startOfWeek.set(Calendar.MILLISECOND, 0);

        endOfWeek = (Calendar) startOfWeek.clone();
        endOfWeek.add(Calendar.DAY_OF_YEAR, 6);
        endOfWeek.set(Calendar.HOUR_OF_DAY, 23);
        endOfWeek.set(Calendar.MINUTE, 59);
        endOfWeek.set(Calendar.SECOND, 59);
        endOfWeek.set(Calendar.MILLISECOND, 999);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        Calendar date = day.getCalendar();
        return date.before(startOfWeek) || date.after(endOfWeek);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
        view.addSpan(new ForegroundColorSpan(color));
    }
}