package org.example.imovie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class SalesStatisticsResponse {
    private Integer scheduleId;
    private String filmName;
    private String theaterName;
    private Date showTime;
    private Integer ticketsSold;
    private Integer totalRevenue;
}
