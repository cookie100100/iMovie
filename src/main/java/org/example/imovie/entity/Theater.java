package org.example.imovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="theater")
@Getter
@Setter
public class Theater {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(length=40)
    private String name;

    @Column(length=200)
    private String address;

    @Column(length=15)
    private String phone;

    private Integer capacity;

    @OneToMany
    @JoinColumn(name="theater_id")
    private List<Schedule> schedules;
}

