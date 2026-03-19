package org.example.imovie.controller;

import org.example.imovie.dto.ScheduleResponse;
import org.example.imovie.entity.Schedule;
import org.example.imovie.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<Page<ScheduleResponse>> getSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ScheduleResponse> schedules = scheduleService.findSchedules(
                PageRequest.of(page, size, Sort.by("showTime").ascending())
        ).map(this::toScheduleResponse);

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Integer id) {
        Schedule schedule = scheduleService.findById(id);
        return ResponseEntity.ok(toScheduleResponse(schedule));
    }

    private ScheduleResponse toScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getFilm().getName(),
                schedule.getTheater().getName(),
                schedule.getShowTime(),
                schedule.getPrice(),
                schedule.getQuota()
        );
    }
}
