package com.zein.wushola.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.zein.wushola.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE date = :date LIMIT 1")
    Schedule getByDate(String date);

    @Query("SELECT * FROM schedules")
    Schedule[] getAll();

    @Query("DELETE FROM schedules")
    void truncate();

    @Insert
    void insertAll(List<Schedule> prayerTimes);
}
