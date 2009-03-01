package org.jmock.test.unit.lib.concurrent;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.concurrent.Blitzer;
import org.jmock.lib.concurrent.ThreadSafeImposteriser;
import org.junit.After;
import org.junit.Test;

public class ThreadSafeImposteriserTests {
    Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(new ThreadSafeImposteriser(new JavaReflectionImposteriser()));
    }};
    
    Blitzer blitzer = new Blitzer(4, 1000);
    
    Runnable mockObject = mockery.mock(Runnable.class, "mockObject");
    
    @Test(timeout=250)
    public void allowsMultipleThreadsToCallMockObjects() throws InterruptedException {
        mockery.checking(new Expectations() {{
            exactly(blitzer.totalActionCount()).of(mockObject).run();
        }});
        
        blitzer.blitz(mockObject);
        
        mockery.assertIsSatisfied();
    }
    
    @After
    public void cleanUp() {
        blitzer.shutdown();
    }
}
