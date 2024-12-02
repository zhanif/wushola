package com.zein.wushola.service;

import com.zein.wushola.dto.ListScheduleDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PrayerTimesService {
    @GET("v2/sholat/jadwal/{kota}/{tahun}/{bulan}")
    Call<ListScheduleDTO> getPrayerTimes(@Path("kota") String kota, @Path("tahun") String tahun, @Path("bulan") String bulan);
}
