package com.marcos.common.exceptions;

public class EventAlreadyProcessedException extends RuntimeException {

    public EventAlreadyProcessedException(String eventId) {
        super("Event already processed: " + eventId);
    }
}