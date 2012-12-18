package testdata.jmock.acceptance.junit4;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JMock.class)
public class JUnit4TestWithNonPublicBeforeMethod {
    @SuppressWarnings("unused")
    private Mockery context = new Mockery();
    
    public boolean beforeWasCalled = false;
    
    @Before void before() {
        beforeWasCalled = true;
    }
    
    @Test public void beforeShouldBeCalled() { 
        assertTrue("before was called", beforeWasCalled);
    }
}
