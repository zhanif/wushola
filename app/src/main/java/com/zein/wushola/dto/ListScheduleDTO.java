package com.zein.wushola.dto;

import com.google.gson.annotations.SerializedName;
import com.zein.wushola.entity.Schedule;
import lombok.Data;

import java.util.List;

@Data
public class ListScheduleDTO {
    private boolean status;

    @SerializedName("request")
    private RequestDetails requestDetails;

    private ScheduleData data;

    @Data
    public static class RequestDetails {
        private String path;
    }

    @Data
    public static class ScheduleData {
        private int id;
        private String lokasi;
        private String daerah;
        private List<Schedule> jadwal;
    }
}
