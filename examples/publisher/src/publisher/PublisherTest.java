package publisher;

import junit.framework.TestCase;

import org.jmock.builder.Mock;

public class PublisherTest extends TestCase {

    public void testOneSubscriberReceivesAMessage() {
        // setup
        Mock mockSubscriber = new Mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add((Subscriber) mockSubscriber.proxy());

        Message message = new Message();

        // expectations
        mockSubscriber.method("receive").passed(message).isVoid()
            .expectOnce();
        
        // execute
        publisher.publish(message);
        
        // verify
        mockSubscriber.verify();
    }
}
