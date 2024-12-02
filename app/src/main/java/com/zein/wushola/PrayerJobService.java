package com.zein.wushola;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

public class PrayerJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        String prayer = jobParameters.getExtras().getString("prayer");
        this.updateWidget(prayer);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void updateWidget(String prayer) {
        System.out.println("Updating for: " + prayer);

        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, MainWidget.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        new MainWidget().refresh(context, appWidgetManager, appWidgetIds);
    }
}
