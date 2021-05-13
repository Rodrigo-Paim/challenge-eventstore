package net.intelie.challenges.dao;

import net.intelie.challenges.entity.Event;
import net.intelie.challenges.util.EventIterator;


public interface EventStore {

    void insert(Event event);


    void removeAll(String type);


    EventIterator query(String type, long startTime, long endTime);
}
