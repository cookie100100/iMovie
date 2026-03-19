package org.example.imovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="schedule")
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="theater_id")
    private Theater theater;

    @ManyToOne
    @JoinColumn(name="f_id")
    private Film film;

    private Date showTime;

    private Integer quota;

    private Integer price;

}
