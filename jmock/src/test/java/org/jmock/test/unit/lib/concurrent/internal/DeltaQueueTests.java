package org.jmock.test.unit.lib.concurrent.internal;

import org.jmock.lib.concurrent.internal.DeltaQueue;

import junit.framework.TestCase;


public class DeltaQueueTests extends TestCase {
    DeltaQueue<String> deltaQueue = new DeltaQueue<String>();
    
    String elementA = "a";
    String elementB = "b";
    String elementC = "c";
    
    
    public void testIsCreatedEmpty() {
        assertTrue("is empty", deltaQueue.isEmpty());
    }
    
    public void testCanScheduleAnElement() {
        final long delay = 10L;
        
        deltaQueue.add(delay, elementA);
        
        assertTrue("is not empty", !deltaQueue.isEmpty());
        
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", delay, deltaQueue.delay());
    }
    
    public void testTicksDownTimeUntilScheduledElement() {
        deltaQueue.add(10L, elementA);
        
        assertEquals(0L, deltaQueue.tick(1L));
        
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", 9L, deltaQueue.delay());
        
        assertEquals(0L, deltaQueue.tick(4L));
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", 5L, deltaQueue.delay());
        
        assertEquals(0L, deltaQueue.tick(4L));
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", 1L, deltaQueue.delay());

        assertEquals(0L, deltaQueue.tick(1L));
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", 0L, deltaQueue.delay());
    }
    
    public void testReturnsTimeAfterElementIfTickGreaterThanDelay() {
        deltaQueue.add(10L, elementA);
        
        assertEquals(5L, deltaQueue.tick(15L));
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", 0L, deltaQueue.delay());
    }
    
    public void testCanPopElementWhenDelayIsZero() {
        deltaQueue.add(10L, elementA);
        
        deltaQueue.tick(10L);
        assertSame("popped", elementA, deltaQueue.pop());
        assertTrue("is empty", deltaQueue.isEmpty());
    }
    
    public void testCanScheduleMultipleElementsInAnyOrder() {
        deltaQueue.add(10L, elementB);
        deltaQueue.add(5L, elementA);
        deltaQueue.add(12L, elementC);
        
        assertSame("next", elementA, deltaQueue.next());
        assertEquals("delay", 5L, deltaQueue.delay());
        
        deltaQueue.tick(5L);
        assertSame("popped A", elementA, deltaQueue.pop());
        
        assertSame("next", elementB, deltaQueue.next());
        assertEquals("delay", 5L, deltaQueue.delay());
        
        deltaQueue.tick(5L);
        assertSame("popped B", elementB, deltaQueue.pop());
        
        assertSame("next", elementC, deltaQueue.next());
        assertEquals("delay", 2L, deltaQueue.delay());
        
        deltaQueue.tick(2L);
        assertSame("popped C", elementC, deltaQueue.pop());
        
        assertTrue("is empty", deltaQueue.isEmpty());
    }
    
    public void testReportsScheduleAsString() {
        deltaQueue.add(10L, elementB);
        deltaQueue.add(5L, elementA);
        deltaQueue.add(12L, elementC);
        
        assertEquals("DeltaQueue[+5: a, +5: b, +2: c]", deltaQueue.toString());
    }
    
    public void testTickingDownAnEmptyDeltaQueueDoesNothingButConsumesAllOfTheTickedTime() {
        assertEquals(0L, deltaQueue.tick(1L));
        assertEquals(0L, deltaQueue.tick(2L));
        assertEquals(0L, deltaQueue.tick(19L));
    }
    
    public void testElementsScheduledWithSameDelayAreExecutedInTheOrderThatTheyWereScheduled() {
        deltaQueue.add(1L, elementA);
        deltaQueue.add(1L, elementB);
        deltaQueue.add(1L, elementC);
        
        deltaQueue.tick(1L);
        
        assertSame(elementA, deltaQueue.pop());
        assertSame(elementB, deltaQueue.pop());
        assertSame(elementC, deltaQueue.pop());
    }
    
    public void testCanRemoveScheduledElements() {
        deltaQueue.add(1L, elementA);
        deltaQueue.add(2L, elementB);
        deltaQueue.add(3L, elementC);
        
        assertTrue(deltaQueue.remove(elementB));
        
        deltaQueue.tick(1L);
        assertSame(elementA, deltaQueue.pop());
        
        deltaQueue.tick(2L);
        assertSame(elementC, deltaQueue.pop());
    }
    
    public void testCanRemoveHead() {
        deltaQueue.add(1L, elementA);
        deltaQueue.add(2L, elementB);
        deltaQueue.add(3L, elementC);
        
        deltaQueue.remove(elementA);
        
        deltaQueue.tick(2L);
        assertSame(elementB, deltaQueue.pop());
        
        deltaQueue.tick(1L);
        assertSame(elementC, deltaQueue.pop());
    }
    
    public void testCanRemoveTail() {
        deltaQueue.add(1L, elementA);
        deltaQueue.add(2L, elementB);
        deltaQueue.add(3L, elementC);
       
        deltaQueue.remove(elementC);
        
        deltaQueue.tick(1L);
        assertSame(elementA, deltaQueue.pop());
        
        deltaQueue.tick(1L);
        assertSame(elementB, deltaQueue.pop());
        
        assertTrue("is empty", deltaQueue.isEmpty());
    }
    
    public void testReturnsFalseIfElementAlreadyRemoved() {
        deltaQueue.add(1L, elementA);
        deltaQueue.add(2L, elementB);
        
        assertFalse(deltaQueue.remove(elementC));
    }
}
