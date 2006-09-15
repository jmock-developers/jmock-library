package org.jmock.integration.junit3;

import org.jmock.Mockery;
import org.jmock.core.Action;
import org.jmock.core.Imposteriser;
import org.jmock.core.MockObjectNamingScheme;
import org.jmock.internal.ExpectationCapture;

/**
 * A {@link junit.framework.TestCase} that supports testing with mock objects.
 * It wraps a {@link org.jmock.Mockery} and automatically asserts that
 * all expectations have been met at the end of the test before 
 * {@link junit.framework.TestCase#tearDown()} is called.
 * 
 * @author npryce
 *
 */
public abstract class MockObjectTestCase extends VerifyingTestCase {
    private final Mockery context = new Mockery();
    
    {
        context.setExpectationErrorTranslator(JUnit3ErrorTranslator.INSTANCE);
        addVerifier(new Runnable() {
            public void run() { 
                context.assertIsSatisfied(); 
            }
        });
    }
    
    public MockObjectTestCase() {
        super();
    }
    
    public MockObjectTestCase(String name) {
        super(name);
    }

    public void setDefaultAction(Action defaultAction) {
        context.setDefaultAction(defaultAction);
    }

    public void setImposteriser(Imposteriser imposteriser) {
        context.setImposteriser(imposteriser);
    }

    public void setNamingScheme(MockObjectNamingScheme namingScheme) {
        context.setNamingScheme(namingScheme);
    }

    /**
     * Specify expectations upon the mock objects in the test.
     * 
     * @param capture
     */
    public void expects(ExpectationCapture capture) {
        context.expects(capture);
    }

    /**
     * Create a mock object of type T with an explicit name.
     * 
     * @param <T>
     * @param typeToMock
     * @param name
     * @return
     */
    public <T> T mock(Class<T> typeToMock, String name) {
        return context.mock(typeToMock, name);
    }

    /**
     * Create a mock object of type T with a name derived from
     * its type.
     * 
     * @param <T>
     * @param typeToMock
     * @return
     */
    public <T> T mock(Class<T> typeToMock) {
        return context.mock(typeToMock);
    }
}
