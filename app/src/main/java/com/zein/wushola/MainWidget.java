package com.zein.wushola;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Color;
import android.widget.RemoteViews;
import com.zein.wushola.context.PrayerContext;

import java.util.Date;

public class MainWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        refresh(context, appWidgetManager, appWidgetIds);
    }

    @SuppressLint("ResourceType")
    public void refresh(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PrayerContext prayerContext = new PrayerContext(context);
        String currentTime = prayerContext.getCurrentTime();
        String currentDate = prayerContext.getCurrentDate();
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
            String[] prayerTimes = prayerContext.getLabels();
            boolean isColorChanged = false;

            views.setTextViewText(R.id.currentDateTime, prayerContext.getSimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()) + " - Cimahi");

            for (String prayer : prayerTimes) {
                String time = prayerContext.getPrayerTime(prayer, currentDate);
                int labelResId = context.getResources().getIdentifier(prayer + "Label", "id", context.getPackageName());
                int timeResId = context.getResources().getIdentifier(prayer + "Time", "id", context.getPackageName());
                views.setTextViewText(timeResId, time);

                if (currentTime.compareTo(time) < 0 && !isColorChanged) {
                    views.setTextColor(labelResId, Color.parseColor(context.getString(R.color.aqua)));
                    views.setTextColor(timeResId, Color.parseColor(context.getString(R.color.aqua)));
                    isColorChanged = true;
                }
                else {
                    views.setTextColor(labelResId, Color.WHITE);
                    views.setTextColor(timeResId, Color.WHITE);
                }
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
