package org.example.imovie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String ticketNo;
    private String filmName;
    private String theaterName;
    private Date showTime;
    private Integer price;
    private Integer quantity;
    private String status;
    private Date orderTime;
}
