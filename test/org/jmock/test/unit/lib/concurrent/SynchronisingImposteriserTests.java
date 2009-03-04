package org.jmock.test.unit.lib.concurrent;

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicInteger;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.concurrent.Blitzer;
import org.jmock.lib.concurrent.SynchronisingImposteriser;
import org.junit.After;
import org.junit.Test;

public class SynchronisingImposteriserTests {
    public interface Events {
        void action();
        void finished();
    }
    
    SynchronisingImposteriser imposteriser = new SynchronisingImposteriser(new JavaReflectionImposteriser());
    
    Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(imposteriser);
    }};
    
    Blitzer blitzer = new Blitzer(4, 1000);
    
    Events mockObject = mockery.mock(Events.class, "mockObject");
    
    @Test(timeout=250)
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
    
    @Test(timeout=250)
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
        
        imposteriser.waitUntil(threads.is("finished"));
    }

    @Test(timeout=250)
    public void canWaitForAStateMachineToEnterAGivenStateWithinSomeTimeout() throws InterruptedException {
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
        
        imposteriser.waitUntil(threads.is("finished"), 100);
    }

    @Test(timeout=250)
    public void failsTheTestIfStateMachineDoesNotEnterExpectedStateWithinTimeout() throws InterruptedException {
        States threads = mockery.states("threads");
        
        try {
            imposteriser.waitUntil(threads.is("finished"), 100);
        }
        catch (AssertionError e) {
            return;
        }
        
        fail("should have thrown AssertionError");
    }

    @After
    public void cleanUp() {
        blitzer.shutdown();
    }
}
