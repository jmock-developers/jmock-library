/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.website;


public class Publisher
{
    private Subscriber subscriber;

    public void add( Subscriber newSubscriber ) {
        this.subscriber = newSubscriber;
    }

    public void publish( Message message ) {
        subscriber.receive(message);
    }
}
