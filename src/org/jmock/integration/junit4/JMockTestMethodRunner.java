package org.jmock.integration.junit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmock.Mockery;
import org.junit.internal.runners.TestMethodRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class JMockTestMethodRunner extends TestMethodRunner {
    private Mockery mockery;
    
    public JMockTestMethodRunner(Object test, Method method, RunNotifier notifier, Description description, Mockery mockery) {
        super(test, method, notifier, description);
        this.mockery = mockery;
    }
    
    @Override
    protected void executeMethodBody() throws IllegalAccessException, InvocationTargetException {
        super.executeMethodBody();
        mockery.assertIsSatisfied();
    }
}
