package org.example.imovie.service.impl;

import org.example.imovie.entity.Statistics;
import org.example.imovie.repository.StatisticsRepository;
import org.example.imovie.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    @Override
    public Optional<Statistics> getByScheduleId(Integer scheduleId) {
        return statisticsRepository.findByScheduleId(scheduleId);
    }
}
