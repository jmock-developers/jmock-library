package org.jmock.integration.junit4;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.Runner;


/**
 * A test {@link Runner} that asserts that all expectations have been met after
 * the test has finished and before the fixture is torn down.
 * 
 * @author nat
 * 
 */
public class JMock extends JUnit4ClassRunner {
    private Field mockeryField;

    public JMock(Class<?> testClass) throws InitializationError {
        super(testClass);
        mockeryField = findMockeryField(testClass);
        mockeryField.setAccessible(true);
    }

    @Override
    protected TestMethod wrapMethod(Method method) {
        return new TestMethod(method, getTestClass()) {
            @Override
            public void invoke(Object testFixture)
                throws IllegalAccessException, InvocationTargetException {
                try {
                    super.invoke(testFixture);
                    assertMockeryIsSatisfied(testFixture);
                }
                catch (InvocationTargetException e) {
                    Throwable actual = e.getTargetException();
                    Class<? extends Throwable> expectedType = this.getExpectedException();
                    
                    if (expectedType != null && expectedType.isInstance(actual)) {
                        assertMockeryIsSatisfied(testFixture);
                    }
                    
                    throw e;
                }
            }
        };
    }
    
    private void assertMockeryIsSatisfied(Object testFixture) {
        mockeryOf(testFixture).assertIsSatisfied();
    }
    
    protected Mockery mockeryOf(Object test) {
        try {
            Mockery mockery = (Mockery)mockeryField.get(test);
            if (mockery == null) {
                throw new IllegalStateException("Mockery named '"
                    + mockeryField.getName() + "' is null");
            }
            return mockery;
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot get value of field "
                + mockeryField.getName(), e);
        }
    }

    static Field findMockeryField(Class<?> testClass) throws InitializationError {
        for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (Mockery.class.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
        }
        
        throw new InitializationError("no Mockery found in test class "
            + testClass);
    }
}
