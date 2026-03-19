package org.example.imovie.repository;

import org.example.imovie.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    Optional<Statistics> findByScheduleId(Integer scheduleId);
}
