package org.example.imovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_statistics")
@Getter
@Setter
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quality;

    private Integer amount;

    @OneToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
