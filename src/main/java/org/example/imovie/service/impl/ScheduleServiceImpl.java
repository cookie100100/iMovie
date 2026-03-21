package org.example.imovie.service.impl;

import org.example.imovie.dto.RespCode;
import org.example.imovie.entity.Schedule;
import org.example.imovie.exception.BusinessException;
import org.example.imovie.repository.ScheduleRepository;
import org.example.imovie.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Page<Schedule> findSchedules(Pageable pageable) {
        return scheduleRepository.findAllWithDetails(pageable);
    }

    @Override
    public Schedule findById(Integer scheduleId) {
        return scheduleRepository.findByIdWithDetails(scheduleId)
                .orElseThrow(() -> new BusinessException(RespCode.SCHEDULE_NOT_FOUND));
    }
}
