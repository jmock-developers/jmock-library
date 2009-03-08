package org.jmock.test.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Blitzer;

public class WarnAboutMultipleThreadsAcceptanceTests extends TestCase {
    List<Throwable> exceptionsOnBackgroundThreads = Collections.synchronizedList(new ArrayList<Throwable>());
    
    Blitzer blitzer = new Blitzer(1, Executors.newFixedThreadPool(1, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    exceptionsOnBackgroundThreads.add(e);
                }
            });
            return t;
        }
    }));
    
    public void testKillsThreadsThatTryToCallMockeryThatIsNotThreadSafe() throws InterruptedException {
        Mockery mockery = new Mockery();
        
        final MockedType mock = mockery.mock(MockedType.class, "mock");
        
        mockery.checking(new Expectations() {{
            allowing (mock).doSomething();
        }});
        
        blitzer.blitz(new Runnable() {
            public void run() {
                mock.doSomething();
            }            
        });
        
        assertThat(exceptionsOnBackgroundThreads.size(), equalTo(blitzer.totalActionCount()));
    }
    
    @Override
    public void tearDown() {
        blitzer.shutdown();
    }
}
