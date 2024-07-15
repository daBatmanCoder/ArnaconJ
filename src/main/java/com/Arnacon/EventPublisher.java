package com.Arnacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventPublisher {


    // A map to hold lists of consumers (subscribers) for each type of event
    private Map<Class<?>, List<Consumer<?>>> subscribers = new HashMap<>();

    /**
     * Subscribe to an event with a specific event type.
     *
     * @param eventType the class of the event to subscribe to
     * @param listener a consumer that handles the event
     * @param <T> the type of the event
     */
    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        // Get or create the list of subscribers for this event type
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Publish an event to all subscribers of this event type.
     *
     * @param event the event to publish
     * @param <T> the type of the event
     */
    public <T> void publish(T event) {
        // Find all subscribers for this particular event's class
        List<Consumer<?>> eventSubscribers = subscribers.get(event.getClass());
        if (eventSubscribers != null) {
            eventSubscribers.forEach(subscriber -> {
                // Cast each subscriber to the correct consumer type and accept the event
                @SuppressWarnings("unchecked")
                Consumer<T> consumer = (Consumer<T>) subscriber;
                consumer.accept(event);
            });
        }
    }
}
