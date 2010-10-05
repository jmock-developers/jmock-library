package org.jmock.example.gettingstarted;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Subscriber> subscribers = new ArrayList<Subscriber>();

    public void add(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.receive(message);
        }
    }
}
