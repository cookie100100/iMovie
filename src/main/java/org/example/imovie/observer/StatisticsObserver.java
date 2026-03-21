package org.example.imovie.observer;

public interface StatisticsObserver extends Runnable {
    void update(Integer scheduleId, Integer ticketNum);
}
