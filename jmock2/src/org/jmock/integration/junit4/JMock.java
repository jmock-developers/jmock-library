package org.jmock.integration.junit4;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestClassMethodsRunner;
import org.junit.internal.runners.TestMethodRunner;
import org.junit.runner.notification.RunNotifier;

/**
 * A JUnit 4 {@link org.junit.Runner} that automatically verifies jMock expectations after the test
 * has run.  
 * 
 * Use by passing JMock.class to the @RunWith annotation like this:
 * <pre>
 * @RunWith(JMock.class)
 * public class SomeMockObjectTests {
 *     Mockery context = new JUnit4Mockery();
 *     ...
 * }
 * </pre>
 * 
 * Note: the {@link org.jmock.Mockery} <em>must</em> be held in an instance variable of the class.
 * 
 * @author nat
 *
 */
public class JMock extends TestClassMethodsRunner {
    private final Field mockeryField;
    
    public JMock(Class<?> testClass) throws InitializationError {
        super(testClass);
        this.mockeryField = findMockeryField(testClass);
        mockeryField.setAccessible(true);
    }
    
    @Override
    protected TestMethodRunner createMethodRunner(Object test, Method method, RunNotifier notifier) {
        Mockery mockery = mockeryOfTest(test);
        return new JMockTestMethodRunner(test, method, notifier, methodDescription(method), mockery);
    }
    
    protected Mockery mockeryOfTest(Object test) {
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
    
    protected Field findMockeryField(Class testClass) throws InitializationError {
        Field[] fields = testClass.getDeclaredFields();
        
        for (Field field : fields) {
            if(Mockery.class.isAssignableFrom(field.getType())) {
                return field;
            }
        }
        
        throw new InitializationError("no Mockery found in test class " + testClass);
    }
}
