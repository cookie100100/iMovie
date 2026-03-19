package org.example.imovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="film")
@Getter
@Setter
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length=50)
    private String name;

    @Column(length=20)
    private String classify;

    @Column(length=20)
    private String director;

    @Column(length=20)
    private String hero;

    @Column(length=20)
    private String heroine;

    private Date production;

    @OneToMany
    @JoinColumn(name="f_id")
    private List<Schedule> schedules;

}
