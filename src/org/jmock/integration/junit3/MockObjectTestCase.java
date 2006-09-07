package org.jmock.integration.junit3;

import org.jmock.Mockery;
import org.jmock.core.Action;
import org.jmock.core.Imposteriser;
import org.jmock.core.MockObjectNamingScheme;
import org.jmock.internal.ExpectationCapture;

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
