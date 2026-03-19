package org.example.imovie.service;

import org.example.imovie.entity.Statistics;

import java.util.List;
import java.util.Optional;

public interface StatisticsService {
    List<Statistics> getAllStatistics();
    Optional<Statistics> getByScheduleId(Integer scheduleId);
}
