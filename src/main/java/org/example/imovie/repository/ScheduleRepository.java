package org.example.imovie.repository;

import org.example.imovie.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    @EntityGraph(attributePaths = {"film", "theater"})
    @Query("SELECT s FROM Schedule s")
    Page<Schedule> findAllWithDetails(Pageable pageable);

    @EntityGraph(attributePaths = {"film", "theater"})
    @Query("SELECT s FROM Schedule s WHERE s.id = :id")
    Optional<Schedule> findByIdWithDetails(@Param("id") Integer id);
}
