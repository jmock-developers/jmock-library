package org.jmock.integration.junit4;

import java.lang.reflect.Field;

import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.model.FrameworkMethod;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Statement;


/**
 * A test {@link Runner} that asserts that all expectations have been met after
 * the test has finished and before the fixture is torn down.
 * 
 * @author nat
 * 
 */
public class JMock extends BlockJUnit4ClassRunner {
    private Field mockeryField;

    public JMock(Class<?> testClass) throws InitializationError {
        super(testClass);
        mockeryField = findMockeryField(testClass);
        mockeryField.setAccessible(true);
    }
    
    @Override
    protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object test, Statement next) {
        return verify(method, test, 
                      super.possiblyExpectingExceptions(method, test, next));
    }
    
    protected Statement verify(
        @SuppressWarnings("unused") FrameworkMethod method, 
        final Object test, 
        final Statement link) 
    {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                link.evaluate();
                assertMockeryIsSatisfied(test);
            }
        };
    }
    
    protected Mockery mockeryOf(Object test) {
        try {
            Mockery mockery = (Mockery)mockeryField.get(test);
            if (mockery == null) {
                throw new IllegalStateException("Mockery named '" + mockeryField.getName() + "' is null");
            }
            return mockery;
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot get value of field " + mockeryField.getName(), e);
        }
    }
    
    protected void assertMockeryIsSatisfied(Object test) {
        mockeryOf(test).assertIsSatisfied();
    }
    
    static Field findMockeryField(Class<?> testClass) throws InitializationError {
        for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (Mockery.class.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
        }
        
        throw new InitializationError("no Mockery found in test class " + testClass);
    }
}
