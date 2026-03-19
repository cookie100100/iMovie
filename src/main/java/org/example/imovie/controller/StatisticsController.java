package org.example.imovie.controller;

import org.example.imovie.dto.SalesStatisticsResponse;
import org.example.imovie.entity.Statistics;
import org.example.imovie.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<List<SalesStatisticsResponse>> getAllStatistics() {
        List<SalesStatisticsResponse> stats = statisticsService.getAllStatistics()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<SalesStatisticsResponse> getStatisticsBySchedule(@PathVariable Integer scheduleId) {
        Statistics statistics = statisticsService.getByScheduleId(scheduleId)
                .orElseThrow(() -> new RuntimeException("No statistics found for schedule: " + scheduleId));

        return ResponseEntity.ok(toResponse(statistics));
    }

    private SalesStatisticsResponse toResponse(Statistics stats) {
        return new SalesStatisticsResponse(
                stats.getSchedule().getId(),
                stats.getSchedule().getFilm().getName(),
                stats.getSchedule().getTheater().getName(),
                stats.getSchedule().getShowTime(),
                stats.getQuality(),
                stats.getAmount()
        );
    }
}
