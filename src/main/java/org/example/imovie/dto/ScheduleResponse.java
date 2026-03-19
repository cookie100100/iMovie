package org.example.imovie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleResponse {
    private Integer id;
    private String filmName;
    private String theaterName;
    private Date showTime;
    private Integer price;
    private Integer availableQuota;
}
