package org.jmock.example.gettingstarted;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit3.MockObjectTestCase;

public class GettingStartedJUnit3Mockomatic extends MockObjectTestCase {
    @Mock Subscriber subscriber;
    
    public void testOneSubscriberReceivesAMessage() {
        // set up
        Publisher publisher = new Publisher();
        publisher.add(subscriber);

        final String message = "message";

        // expectations
        checking(new Expectations() {{
            oneOf(subscriber).receive(message);
        }});

        // execute
        publisher.publish(message);
    }
}