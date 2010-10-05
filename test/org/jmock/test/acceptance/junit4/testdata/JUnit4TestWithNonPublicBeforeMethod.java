package org.jmock.test.acceptance.junit4.testdata;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestWithNonPublicBeforeMethod {
    @SuppressWarnings("unused")
    private Mockery context = new Mockery();
    
    public boolean beforeWasCalled = false;
    
    @Before void before() {
        beforeWasCalled = true;
    }
    
    @Test public void beforeShouldBeCalled() { 
        Assert.assertTrue("before was called", beforeWasCalled);
    }
}
