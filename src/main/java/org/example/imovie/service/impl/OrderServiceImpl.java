package org.example.imovie.service.impl;

import org.example.imovie.entity.*;
import org.example.imovie.repository.OrderRepository;
import org.example.imovie.repository.ScheduleRepository;
import org.example.imovie.repository.StatisticsRepository;
import org.example.imovie.repository.UserRepository;
import org.example.imovie.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ScheduleRepository scheduleRepository,
                            UserRepository userRepository,
                            StatisticsRepository statisticsRepository) {
        this.orderRepository = orderRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    @Transactional
    public Order saveOrder(Integer scheduleId, Long userId, Integer ticketNum) {
        // Validate schedule
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check available quota
        if (schedule.getQuota() < ticketNum) {
            throw new RuntimeException("Not enough tickets available. Remaining: " + schedule.getQuota());
        }

        // Decrement quota
        schedule.setQuota(schedule.getQuota() - ticketNum);
        scheduleRepository.save(schedule);

        // Create order
        Order order = new Order();
        order.setTicketNo(generateTicketNo());
        order.setSchedule(schedule);
        order.setPrice(schedule.getPrice() * ticketNum);
        order.setQuality(ticketNum);
        order.setUser(user);
        order.setStatus(OrderStatus.New);
        order.setOrderTime(new Date());

        Order savedOrder = orderRepository.save(order);

        // Update statistics
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

        return savedOrder;
    }

    @Override
    public Page<Order> getOrderList(Long userId, Pageable page) {
        return orderRepository.findByUserId(userId, page);
    }

    private String generateTicketNo() {
        return "TK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
