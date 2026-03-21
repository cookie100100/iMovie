package org.example.imovie.controller;

import org.example.imovie.dto.ResultJsonObject;
import org.example.imovie.dto.ScheduleResponse;
import org.example.imovie.entity.Schedule;
import org.example.imovie.service.ScheduleService;
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
    public ResponseEntity<ResultJsonObject> getSchedules(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ResultJsonObject.success(
                scheduleService.findSchedules(PageRequest.of(page, size, Sort.by("showTime").ascending()))
                        .map(this::toScheduleResponse)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultJsonObject> getScheduleById(@PathVariable("id") Integer id) {
        Schedule schedule = scheduleService.findById(id);
        return ResponseEntity.ok(ResultJsonObject.success(toScheduleResponse(schedule)));
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
