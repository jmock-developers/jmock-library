package org.jmock.integration.junit4;

import org.jmock.Mockery;
import org.jmock.auto.internal.Mockomatic;
import org.jmock.internal.AllDeclaredFields;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;


/**
 * A test {@link Runner} that asserts that all expectations have been met after
 * the test has finished and before the fixture is torn down.
 * @deprecated For JUnit 4 use {@link JUnitRuleMockery}
 * @author nat steve.freeman
 * 
 */
@Deprecated
public class JMock extends BlockJUnit4ClassRunner {
    private Field mockeryField;

    public JMock(Class<?> testClass) throws InitializationError {
        super(testClass);
        mockeryField = findMockeryField(testClass);
        mockeryField.setAccessible(true);
    }
    
    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        Mockomatic mockomatic = new Mockomatic(mockeryOf(test));
        mockomatic.fillIn(test);
        return test;
    }
    
    @Override @Deprecated
    protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object test, Statement next) {
        return verify(method, test, super.possiblyExpectingExceptions(method, test, next));
    }
    
    protected Statement verify(
        @SuppressWarnings("unused") FrameworkMethod method, 
        final Object test, 
        final Statement next) 
    {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                next.evaluate();
                assertMockeryIsSatisfied(test);
            }
        };
    }
    
    protected void assertMockeryIsSatisfied(Object test) {
        mockeryOf(test).assertIsSatisfied();
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
    
    static Field findMockeryField(Class<?> testClass) throws InitializationError {
        Field mockeryField = null;
        
        for (Field field : AllDeclaredFields.in(testClass)) {
            if (Mockery.class.isAssignableFrom(field.getType())) {
                if (mockeryField != null) {
                    throw new InitializationError("more than one Mockery found in test class " + testClass);
                }
                mockeryField = field;
            }
        }
        
        if (mockeryField == null) {
            throw new InitializationError("no Mockery found in test class " + testClass);
        }
        
        return mockeryField;
    }
}
