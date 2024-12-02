package com.zein.wushola;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zein.wushola.context.PrayerContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private PrayerContext prayerContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        prayerContext = new PrayerContext(getApplicationContext());

        this.registerEvents();
        this.scheduleUpdateForPrayerTimes();
    }

    private void registerEvents() {
        Button syncButton = findViewById(R.id.sync_button);

        try {
            syncButton.setOnClickListener(view -> {
                prayerContext.reloadAPI("1223", 2024, 12);
                Toast.makeText(this, "Berhasil mensinkronisasi jadwal sholat", Toast.LENGTH_SHORT).show();

                Context context = getApplicationContext();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName componentName = new ComponentName(context, MainWidget.class);

                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
                new MainWidget().refresh(context, appWidgetManager, appWidgetIds);
            });
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void scheduleUpdateForPrayerTimes() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        String[] prayerTimes = prayerContext.getLabels();
        for (String prayer : prayerTimes) {
            String prayerTime = prayerContext.getPrayerTime(prayer, prayerContext.getCurrentDate());
            long prayerMillis = getMillisFromTimeString(prayerTime);
            long currentTimeMillis = getMillisFromTimeString(prayerContext.getCurrentTime());
            long triggerTime = prayerMillis - currentTimeMillis;

            // Log trigger time to see if it's correct
            System.out.println("Trigger time for " + prayer + ": " + triggerTime);

            if (triggerTime > 0) {
                PersistableBundle extras = new PersistableBundle();
                extras.putString("prayer", prayer);

                // Create a job with the calculated trigger time
                JobInfo jobInfo = new JobInfo.Builder(prayer.hashCode(), new ComponentName(MainActivity.this, PrayerJobService.class))
                        .setPersisted(true)
                        .setExtras(extras)
                        .setMinimumLatency(triggerTime)
                        .build();

                // Schedule the job
                jobScheduler.schedule(jobInfo);
            }
        }
    }

    private long getMillisFromTimeString(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));
            Date date = sdf.parse(timeString);
            return date != null ? date.getTime() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}