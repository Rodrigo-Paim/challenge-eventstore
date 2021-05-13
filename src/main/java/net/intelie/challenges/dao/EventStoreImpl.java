package net.intelie.challenges.dao;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import net.intelie.challenges.entity.Event;
import net.intelie.challenges.util.DateUtils;
import net.intelie.challenges.util.EventIterator;
import net.intelie.challenges.util.EventIteratorImpl;


public class EventStoreImpl implements EventStore {


	private static volatile Queue<Event> events;

	private static Queue<Event> getInstance() {

		if (events == null) { 
			synchronized (EventStoreImpl.class) {

					events = new ConcurrentLinkedQueue<Event>();
			}

		}

		return events;
	}

	@Override
	public synchronized void insert(Event event) {
		getInstance().add(event);

	}

	@Override
	public synchronized void removeAll(String type) {
		getInstance().removeIf((Event event) -> event.type() == type);
	}

	@Override
	public synchronized EventIterator query(String type, long startTime, long endTime) {

		return new EventIteratorImpl(getInstance().parallelStream()
				.filter((Event event) -> (event.type() == type
						&& DateUtils.isDateBeetween(event.timestamp(), startTime, endTime)))
				.collect(Collectors.toList()));
	}

}
