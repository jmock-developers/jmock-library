/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.website;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


public class PublisherTest extends MockObjectTestCase
{

    public void testOneSubscriberReceivesAMessage() {
        // setup
        Mock mockSubscriber = mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add((Subscriber)mockSubscriber.proxy());

        Message message = new Message();

        // expectations
        mockSubscriber.expects(once()).method("receive").with(eq(message)).isVoid();

        // execute
        publisher.publish(message);
    }
}
