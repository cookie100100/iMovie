package org.example.imovie.service;

import org.example.imovie.entity.Schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
    public Page<Schedule> findSchedules(Pageable pageable);
    public Schedule findById(Integer scheduleId);
}
