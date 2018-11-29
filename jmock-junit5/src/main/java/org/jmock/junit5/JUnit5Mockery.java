package org.jmock.junit5;

import java.lang.reflect.Field;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.auto.internal.Mockomatic;
import org.jmock.internal.AllDeclaredFields;
import org.jmock.lib.AssertionErrorTranslator;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.auto.service.AutoService;

/**
 * A <code>JUnit5Mockery</code> is a JUnit Extension that manages JMock
 * expectations and allowances, and asserts that expectations have been met
 * after each test has finished. To use it, add a (non-private) field to the test class 
 * For example,
 * 
 * <pre>
 * public class ATestWithSatisfiedExpectations {
 *     &#64;RegisterExtension
 *     final JUnitRuleMockery context = new JUnitRuleMockery();
 *     
 *     &#64;Mock
 *     private final Runnable runnable;
 * 
 *     &#64;Test
 *     public void doesSatisfyExpectations() {
 *         context.checking(new Expectations() {
 *             {
 *                 oneOf(runnable).run();
 *             }
 *         });
 * 
 *         runnable.run();
 *     }
 * }
 * </pre>
 *
 * Note that the Rule field must be declared public and as a
 * <code>JUnitRuleMockery</code> (not a <code>Mockery</code>) for JUnit to
 * recognise it, as it's checked statically.
 * 
 * @author olibye
 */
@AutoService(Extension.class)
public class JUnit5Mockery extends Mockery
        implements Extension, BeforeEachCallback, AfterEachCallback {

    private final Mockomatic mockomatic = new Mockomatic(this);

    public JUnit5Mockery() {
        setExpectationErrorTranslator(AssertionErrorTranslator.INSTANCE);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (context.getTestClass().isPresent()) {
            Class<?> testCaseClass = context.getTestClass().get();
            List<Field> allFields = AllDeclaredFields.in(testCaseClass);
            fillInAutoMocks(context.getRequiredTestInstance(), allFields);

            checkMockery(context, testCaseClass);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        assertIsSatisfied();
    }

    private void fillInAutoMocks(final Object target, List<Field> allFields) {
        mockomatic.fillIn(target, allFields);
    }

    private static void checkMockery(ExtensionContext context, Class<?> testCaseClass) {
        Field mockeryField = findMockeryField(testCaseClass, context);
        try {
            // private extension fields are not called
            // field will at least be default scope if we're called.
            mockeryField.setAccessible(true);
            if(mockeryField.get(context.getRequiredTestInstance()) == null) {
                throw new IllegalStateException("JUnit5Mockery field should not be null");
            }
        } catch (IllegalArgumentException e) {
            throw new ExtensionConfigurationException("Could not check the mockery", e);
        } catch (IllegalAccessException e) {
            throw new ExtensionConfigurationException("Could not check the mockery", e);
        }
    }

    private static Field findMockeryField(Class<?> testClass, ExtensionContext context) {
        Field mockeryField = null;

        for (Field field : AllDeclaredFields.in(testClass)) {
            if (Mockery.class.isAssignableFrom(field.getType())) {
                if (mockeryField != null) {
                    throw new ExtensionConfigurationException("more than one Mockery found in test class " + testClass);
                }
                mockeryField = field;
            }
        }

        if (mockeryField == null) {
            throw new ExtensionConfigurationException("no Mockery found in test class " + testClass);
        }

        return mockeryField;
    }
}
