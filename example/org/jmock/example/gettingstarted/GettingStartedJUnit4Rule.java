package org.jmock.example.gettingstarted;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class GettingStartedJUnit4Rule {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void oneSubscriberReceivesAMessage() {
        // set up
        final Subscriber subscriber = context.mock(Subscriber.class);

        Publisher publisher = new Publisher();
        publisher.add(subscriber);

        final String message = "message";

        // expectations
        context.checking(new Expectations() {{
            oneOf(subscriber).receive(message);
        }});

        // execute
        publisher.publish(message);
    }
}