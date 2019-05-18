package org.jmock.junit5.unit.lib.concurrent.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicInteger;

import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.junit5.JUnit5Mockery;
import org.jmock.junit5.extensions.ExpectationTimeout;
import org.jmock.lib.concurrent.Blitzer;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class SynchroniserTests {
    public interface Events {
        void action();
        void finished();
    }
    
    Synchroniser synchroniser = new Synchroniser();

    // Not an extension, to allow checking the mockery is *not* satisfied
    JUnit5Mockery mockery = new JUnit5Mockery() {{
        setThreadingPolicy(synchroniser);
    }};
    
    Blitzer blitzer = new Blitzer(16, 4);
    
    Events mockObject = mockery.mock(Events.class, "mockObject");
    
    @Test
    @ExpectationTimeout(timeout=250)
    public void allowsMultipleThreadsToCallMockObjects() throws InterruptedException {
        mockery.checking(new Expectations() {{
            exactly(blitzer.totalActionCount()).of(mockObject).action();
        }});
        
        blitzer.blitz(new Runnable() {
            public void run() {
                mockObject.action();
            }
        });
        
        mockery.assertIsSatisfied();
    }
    
    @Test
    @ExpectationTimeout(timeout=250)
    public void canWaitForAStateMachineToEnterAGivenState() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(blitzer.totalActionCount());
        
        final States threads = mockery.states("threads");
        
        mockery.checking(new Expectations() {{
            exactly(blitzer.totalActionCount()).of(mockObject).action();
                when(threads.isNot("finished"));
                
            oneOf(mockObject).finished();
                then(threads.is("finished"));
        }});
        
        blitzer.blitz(new Runnable() {
            public void run() {
                mockObject.action();
                if (counter.decrementAndGet() == 0) {
                    mockObject.finished();
                }
            }
        });
        
        synchroniser.waitUntil(threads.is("finished"));
        mockery.assertIsSatisfied();
    }

    @Test
    @ExpectationTimeout(timeout=250)
    public void canWaitForAStateMachineToEnterAGivenStateWithinSomeTimeout() throws InterruptedException {
        final States threads = mockery.states("threads");
        
        mockery.checking(new Expectations() {{
            exactly(blitzer.totalActionCount()).of(mockObject).action();
                when(threads.isNot("finished"));
                
            oneOf(mockObject).finished();
                then(threads.is("finished"));
        }});
        
        blitzer.blitz(new Runnable() {
            AtomicInteger counter = new AtomicInteger(blitzer.totalActionCount());
            
            public void run() {
                mockObject.action();
                if (counter.decrementAndGet() == 0) {
                    mockObject.finished();
                }
            }
        });
        
        synchroniser.waitUntil(threads.is("finished"), 100);
    }

    @Test
    @ExpectationTimeout(timeout=250)
    public void failsTheTestIfStateMachineDoesNotEnterExpectedStateWithinTimeout() throws InterruptedException {
        States threads = mockery.states("threads");
        
        try {
            synchroniser.waitUntil(threads.is("finished"), 100);
        }
        catch (AssertionError e) {
            return;
        }
        
        fail("should have thrown AssertionError");
    }
    
    @Test
    public void throwsExpectationErrorIfExpectationFailsWhileWaitingForStateMachine() throws InterruptedException {
        final States threads = mockery.states("threads");
        
        // This will cause an expectation error, and nothing will make
        // the "threads" state machine transition to "finished" 
        
        blitzer.blitz(new Runnable() {
            public void run() {
                mockObject.action();
            }
        });
        
        try {
            synchroniser.waitUntil(threads.is("finished"), 100);
            fail("should have thrown AssertionError");
        }
        catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("action()"));
        }
    }

    @Test
    public void throwsExpectationErrorIfExpectationFailsWhileWaitingForStateMachineEvenIfWaitSucceeds() throws InterruptedException {
        final States threads = mockery.states("threads");
        
        mockery.checking(new Expectations() {{
            oneOf(mockObject).finished();
                then(threads.is("finished"));
        }});
        
        blitzer.blitz(new Runnable() {
            AtomicInteger counter = new AtomicInteger(blitzer.totalActionCount());
            
            public void run() {
                if (counter.decrementAndGet() == 0) {
                    mockObject.finished();
                }
                else {
                    mockObject.action();
                }
            }
        });
        
        try {
            synchroniser.waitUntil(threads.is("finished"), 100);
            fail("should have thrown AssertionError");
        }
        catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("action()"));
        }
    }
    
    @AfterEach
    public void cleanUp() {
        blitzer.shutdown();
    }
}
