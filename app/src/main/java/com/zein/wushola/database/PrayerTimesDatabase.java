package com.zein.wushola.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.zein.wushola.dao.ScheduleDao;
import com.zein.wushola.entity.Schedule;

@Database(entities = {Schedule.class}, version = 1)
public abstract class PrayerTimesDatabase extends RoomDatabase {
    public abstract ScheduleDao scheduleDao();
    private static volatile PrayerTimesDatabase INSTANCE;

    public static PrayerTimesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PrayerTimesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PrayerTimesDatabase.class,
                            "prayer_times_db"
                    ).allowMainThreadQueries()
                     .build();
                }
            }
        }
        return INSTANCE;
    }
}
