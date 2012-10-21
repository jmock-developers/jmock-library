package org.jmock.example.announcer;

import java.util.EventListener;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit3.MockObjectTestCase;


public class AnnouncerTests extends MockObjectTestCase {
	public static class CheckedException extends Exception {}
	
	public interface Listener extends EventListener {
		public void eventA();
		public void eventB();
		public void eventWithArguments(int a, int b);
		public void badEvent() throws CheckedException;
	}
	
	Announcer<Listener> announcer = Announcer.to(Listener.class);
	
	Listener listener1 = mock(Listener.class, "listener1");
	Listener listener2 = mock(Listener.class, "listener2");
	
	@Override
    public void setUp() {
		announcer.addListener(listener1);
		announcer.addListener(listener2);
	}
	
	public void testAnnouncesToRegisteredListenersInOrderOfAddition() {
		final Sequence eventOrder = sequence("eventOrder");
		
		checking(new Expectations() {{
			oneOf (listener1).eventA(); inSequence(eventOrder);
			oneOf (listener2).eventA(); inSequence(eventOrder);
			oneOf (listener1).eventB(); inSequence(eventOrder);
			oneOf (listener2).eventB(); inSequence(eventOrder);
		}});
		
		announcer.announce().eventA();
		announcer.announce().eventB();
	}
	
	public void testPassesEventArgumentsToListeners() {
		checking(new Expectations() {{
			oneOf (listener1).eventWithArguments(1, 2);
			oneOf (listener2).eventWithArguments(1, 2);
			oneOf (listener1).eventWithArguments(3, 4);
			oneOf (listener2).eventWithArguments(3, 4);
		}});
		
		announcer.announce().eventWithArguments(1, 2);
		announcer.announce().eventWithArguments(3, 4);
	}
	
	public void testCanRemoveListeners() {
		announcer.removeListener(listener1);
		
		checking(new Expectations() {{
			oneOf (listener2).eventA();
		}});
		
		announcer.announce().eventA();
	}
	
	public void testDoesNotAllowListenersToThrowCheckedExceptions() throws Exception {
		checking(new Expectations() {{
			allowing (listener1).badEvent(); will(throwException(new CheckedException()));
		}});
		
		try {
			announcer.announce().badEvent();
			fail("should have thrown UnsupportedOperationException");
		}
		catch (UnsupportedOperationException expected) {}
	}
}
