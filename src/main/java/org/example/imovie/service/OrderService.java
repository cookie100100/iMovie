package org.example.imovie.service;

import org.example.imovie.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order saveOrder(Integer scheduleId, Long userId, Integer ticketNum);
    Page<Order> getOrderList(Long userId, Pageable page);
}
