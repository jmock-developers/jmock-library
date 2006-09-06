package org.jmock.integration.junit3;

import junit.framework.AssertionFailedError;

import org.hamcrest.StringDescription;
import org.jmock.Mockery;
import org.jmock.core.Action;
import org.jmock.core.ExpectationError;
import org.jmock.core.ExpectationErrorTranslator;
import org.jmock.core.Imposteriser;
import org.jmock.core.MockObjectNamingScheme;
import org.jmock.internal.ExpectationCapture;

public abstract class MockObjectTestCase extends VerifyingTestCase {
    private static final ExpectationErrorTranslator JUNIT_ERROR_TRANSLATOR = new ExpectationErrorTranslator() {
        public Error translate(ExpectationError e) {
            return new AssertionFailedError(StringDescription.toString(e));
        }
    };
    
    private final Mockery context = new Mockery();
    
    {
        context.setExpectationErrorTranslator(JUNIT_ERROR_TRANSLATOR);
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

    public void expects(ExpectationCapture capture) {
        context.expects(capture);
    }

    public <T> T mock(Class<T> typeToMock, String name) {
        return context.mock(typeToMock, name);
    }

    public <T> T mock(Class<T> typeToMock) {
        return context.mock(typeToMock);
    }
}
