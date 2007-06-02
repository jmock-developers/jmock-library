package org.jmock.test.acceptance.junit4;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestWithNonPublicBeforeMethod {
    Mockery context = new Mockery();
    boolean beforeWasCalled = false;
    
    @Before void before() {
        beforeWasCalled = true;
    }
    
    @Test public void beforeShouldBeCalled() { 
        Assert.assertTrue("before was called", beforeWasCalled);
    }
}
