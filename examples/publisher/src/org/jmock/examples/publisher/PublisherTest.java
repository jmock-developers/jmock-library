package org.jmock.examples.publisher;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class PublisherTest extends MockObjectTestCase {

    public void testOneSubscriberReceivesAMessage() {
        // setup
        Mock mockSubscriber = mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add((Subscriber) mockSubscriber.proxy());

        Message message = new Message();

        // expectations
        mockSubscriber.expect(once()).method("receive").with(eq(message)).isVoid();
        
        // execute
        publisher.publish(message);
        
        // verify
        mockSubscriber.verify();
    }
}
