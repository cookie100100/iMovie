package org.example.imovie.repository;

import org.example.imovie.entity.Statistics;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    Optional<Statistics> findByScheduleId(Integer scheduleId);

    @EntityGraph(attributePaths = {"schedule", "schedule.film", "schedule.theater"})
    @Query("SELECT st FROM Statistics st")
    List<Statistics> findAllWithDetails();

    @EntityGraph(attributePaths = {"schedule", "schedule.film", "schedule.theater"})
    @Query("SELECT st FROM Statistics st WHERE st.schedule.id = :scheduleId")
    Optional<Statistics> findByScheduleIdWithDetails(@Param("scheduleId") Integer scheduleId);
}
