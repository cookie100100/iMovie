package org.example.imovie.service.impl;

import org.example.imovie.dto.RespCode;
import org.example.imovie.entity.*;
import org.example.imovie.event.OrderEvent;
import org.example.imovie.exception.BusinessException;
import org.example.imovie.repository.OrderRepository;
import org.example.imovie.repository.ScheduleRepository;
import org.example.imovie.repository.UserRepository;
import org.example.imovie.service.OrderService;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository,
            ScheduleRepository scheduleRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Order saveOrder(Integer scheduleId, Long userId, Integer ticketNum) {
        // Validate schedule
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(RespCode.SCHEDULE_NOT_FOUND));

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(RespCode.USER_NOT_FOUND));

        // Check available quota
        if (schedule.getQuota() < ticketNum) {
            throw new BusinessException(RespCode.INSUFFICIENT_TICKETS);
        }

        // Decrement quota
        schedule.setQuota(schedule.getQuota() - ticketNum);
        scheduleRepository.save(schedule);

        // Create order
        Order order = new Order();
        order.setTicketNo(generateTicketNo());
        order.setSchedule(schedule);
        order.setPrice(schedule.getPrice() * ticketNum);
        order.setQuantity(ticketNum);
        order.setUser(user);
        order.setStatus(OrderStatus.New);
        order.setOrderTime(new Date());

        Order savedOrder = orderRepository.save(order);

        // Notify observers
        notifyObservers(scheduleId, ticketNum);

        return savedOrder;
    }

    private void notifyObservers(Integer scheduleId, Integer ticketNum) {
        eventPublisher.publishEvent(new OrderEvent(this, scheduleId, ticketNum));
    }

    @Override
    public Page<Order> getOrderList(Long userId, Pageable page) {
        return orderRepository.findByUserId(userId, page);
    }

    private String generateTicketNo() {
        return "TK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
