package publisher;

import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;

public class PublisherTest extends MockObjectTestCase {

    public void testOneSubscriberReceivesAMessage() {
        // setup
        Mock mockSubscriber = new Mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add((Subscriber) mockSubscriber.proxy());

        Message message = new Message();

        // expectations
        mockSubscriber.method("receive").with(eq(message)).isVoid()
            .expect(once());
        
        // execute
        publisher.publish(message);
        
        // verify
        mockSubscriber.verify();
    }
}
