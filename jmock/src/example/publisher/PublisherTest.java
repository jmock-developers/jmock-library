package publisher;

import junit.framework.TestCase;

import org.jmock.dynamock.Mock;

public class PublisherTest extends TestCase {

    public void testOneSubscriberReceivesAMessage() {
        // setup
        Mock mockSubscriber = new Mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add((Subscriber) mockSubscriber.proxy());

        Message message = new Message();

        // expectations
        mockSubscriber.expectVoid("receive", message);
        
        // execute
        publisher.publish(message);
        
        // verify
        mockSubscriber.verify();
    }


}
