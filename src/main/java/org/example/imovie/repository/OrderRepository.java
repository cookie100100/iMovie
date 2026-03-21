package org.example.imovie.repository;

import org.example.imovie.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"schedule", "schedule.film", "schedule.theater"})
    Page<Order> findByUserId(Long userId, Pageable pageable);
}
