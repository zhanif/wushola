package com.zein.wushola.context;

import android.content.Context;
import com.zein.wushola.database.PrayerTimesDatabase;
import com.zein.wushola.dto.ListScheduleDTO;
import com.zein.wushola.entity.Schedule;
import com.zein.wushola.service.PrayerTimesService;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class PrayerContext {
    private static final String BASE_URL = "https://api.myquran.com/";
    private final PrayerTimesDatabase db;

    public PrayerContext(Context context) {
        this.db = PrayerTimesDatabase.getInstance(context);
    }

    public SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format, new Locale("id", "ID"));
    }

    public String getCurrentTime() {
        return this.getSimpleDateFormat("HH:mm").format(new Date());
    }

    public String getCurrentDate() {
        return this.getSimpleDateFormat("YYYY-MM-dd").format(new Date());
    }

    public String getPrayerTime(String prayerTime, String date) {
        Schedule schedule = db.scheduleDao().getByDate(date);

        if (schedule == null) return "--:--";
        Map<String, String> prayerTimes = Map.of(
            "imsak", schedule.getImsak(),
            "subuh", schedule.getSubuh(),
            "dzuhur", schedule.getDzuhur(),
            "ashar", schedule.getAshar(),
            "maghrib", schedule.getMaghrib(),
            "isya", schedule.getIsya()
        );

        return prayerTimes.getOrDefault(prayerTime, "--:--");
    }

    public String[] getLabels() {
        return new String[]{"imsak", "subuh", "dzuhur", "ashar", "maghrib", "isya"};
    }

    public void reloadAPI(String number, int year, int month) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PrayerTimesService timesService = retrofit.create(PrayerTimesService.class);
        timesService.getPrayerTimes(number, String.valueOf(year), String.valueOf(month)).enqueue(new Callback<ListScheduleDTO>() {
            @Override
            public void onResponse(@NotNull Call<ListScheduleDTO> call, Response<ListScheduleDTO> response) {
             if (response.isSuccessful() && response.body() != null) {
                    List<Schedule> jadwal = response.body().getData().getJadwal();
                    db.scheduleDao().truncate();
                    db.scheduleDao().insertAll(jadwal);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListScheduleDTO> call, @NotNull Throwable throwable) {
                System.out.println("Failed to fetch data");
                System.out.println(throwable.getMessage());
            }
        });
    }
}
