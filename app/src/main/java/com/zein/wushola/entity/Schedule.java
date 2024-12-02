package com.zein.wushola.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "schedules")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String tanggal;
    private String imsak;
    private String subuh;
    private String terbit;
    private String dzuhur;
    private String ashar;
    private String maghrib;
    private String isya;
    private String date;
}
