package org.jmock.integration.junit4;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A <code>JMockContext</code> is a JUnit Rule that manages JMock expectations 
 * and allowances, and asserts that expectations have been met after each test
 * has finished. To use it, add a field to the test class (note that you don't 
 * have to specify <code>@RunWith(JMock.class)</code> any more). For example,  
 * 
 * <pre>public class SatisfiesExpectations {
 *  &at;Rule public final JMockContext context = new JMockContext();
 *  private final Runnable runnable = context.mock(Runnable.class);
 *     
 *  &at;Test
 *  public void doesSatisfyExpectations() {
 *    context.checking(new Expectations() {{
 *      oneOf (runnable).run();
 *    }});
 *          
 *    runnable.run();
 *  }
 *}</pre>
 *
 * Note that the Rule field must be declared public and as a <code>JMockContext</code>
 * (not a <code>Mockery</code>) for JUnit to recognise it, as it's checked statically.
 * 
 * @author smgf
 */

public class JMockContext extends JUnit4Mockery implements MethodRule {

    @Override
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                assertIsSatisfied();
            }
        };
    }
}
