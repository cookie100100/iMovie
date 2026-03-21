package org.example.imovie.service.impl;

import org.example.imovie.dto.RespCode;
import org.example.imovie.entity.Schedule;
import org.example.imovie.entity.Statistics;
import org.example.imovie.event.OrderEvent;
import org.example.imovie.exception.BusinessException;
import org.example.imovie.observer.StatisticsObserver;
import org.example.imovie.repository.ScheduleRepository;
import org.example.imovie.repository.StatisticsRepository;
import org.example.imovie.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Service
public class StatisticsServiceImpl implements StatisticsService, StatisticsObserver {

    private final StatisticsRepository statisticsRepository;
    private final ScheduleRepository scheduleRepository;
    private final ExecutorService executorService;

    private Integer scheduleId;
    private Integer ticketNum;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository,
                                 ScheduleRepository scheduleRepository,
                                 ExecutorService executorService) {
        this.statisticsRepository = statisticsRepository;
        this.scheduleRepository = scheduleRepository;
        this.executorService = executorService;
    }

    // ==================== StatisticsService (read) ====================

    @Override
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAllWithDetails();
    }

    @Override
    public Optional<Statistics> getByScheduleId(Integer scheduleId) {
        return statisticsRepository.findByScheduleIdWithDetails(scheduleId);
    }

    // ==================== StatisticsObserver (event listener) ====================

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        update(event.getScheduleId(), event.getTicketNum());
    }

    @Override
    public void update(Integer scheduleId, Integer ticketNum) {
        this.scheduleId = scheduleId;
        this.ticketNum = ticketNum;
        executorService.submit(this);
    }

    @Override
    @Transactional
    public void run() {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(RespCode.SCHEDULE_NOT_FOUND));

        Statistics statistics = statisticsRepository.findByScheduleId(scheduleId)
                .orElseGet(() -> {
                    Statistics newStats = new Statistics();
                    newStats.setSchedule(schedule);
                    newStats.setQuality(0);
                    newStats.setAmount(0);
                    return newStats;
                });

        statistics.setQuality(statistics.getQuality() + ticketNum);
        statistics.setAmount(statistics.getAmount() + (schedule.getPrice() * ticketNum));
        statisticsRepository.save(statistics);
    }
}
