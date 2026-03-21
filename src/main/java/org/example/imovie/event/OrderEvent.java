package org.example.imovie.event;

import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {

    private final Integer scheduleId;
    private final Integer ticketNum;

    public OrderEvent(Object source, Integer scheduleId, Integer ticketNum) {
        super(source);
        this.scheduleId = scheduleId;
        this.ticketNum = ticketNum;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public Integer getTicketNum() {
        return ticketNum;
    }
}
