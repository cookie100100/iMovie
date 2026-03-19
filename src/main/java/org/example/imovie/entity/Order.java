package org.example.imovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "order")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_no", length = 20)
    private String ticketNo;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private Integer price;

    private Integer quality;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Date orderTime;

    private Date payTime;
}
