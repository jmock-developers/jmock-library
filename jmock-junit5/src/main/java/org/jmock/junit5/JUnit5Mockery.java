package org.jmock.junit5;

import java.lang.reflect.Field;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.auto.internal.Mockomatic;
import org.jmock.internal.AllDeclaredFields;
import org.jmock.lib.AssertionErrorTranslator;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.auto.service.AutoService;

/**
 * A <code>JUnitRuleMockery</code> is a JUnit Rule that manages JMock
 * expectations and allowances, and asserts that expectations have been met
 * after each test has finished. To use it, add a field to the test class (note
 * that you don't have to specify <code>@RunWith(JMock.class)</code> any more).
 * For example,
 * 
 * <pre>
 * public class ATestWithSatisfiedExpectations {
 *     &#64;RegisterExtension
 *     public final JUnitRuleMockery context = new JUnitRuleMockery();
 *     private final Runnable runnable = context.mock(Runnable.class);
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
@AutoService(org.junit.jupiter.api.extension.Extension.class)
public class JUnit5Mockery extends Mockery
        implements Extension, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private final Mockomatic mockomatic = new Mockomatic(this);

    public JUnit5Mockery() {
        setExpectationErrorTranslator(AssertionErrorTranslator.INSTANCE);
    }

    private void fillInAutoMocks(final Object target, List<Field> allFields) {
        mockomatic.fillIn(target, allFields);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        if (context.getTestClass().isPresent()) {
            Class<?> testCaseClass = context.getTestClass().get();
            List<Field> allFields = AllDeclaredFields.in(testCaseClass);
            fillInAutoMocks(testCaseClass, allFields);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        assertIsSatisfied();
    }
}
