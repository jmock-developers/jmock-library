package org.jmock;

import org.jmock.core.Action;
import org.jmock.core.Expectation;
import org.jmock.core.ExpectationError;
import org.jmock.core.ExpectationErrorTranslator;
import org.jmock.core.Imposteriser;
import org.jmock.core.Invocation;
import org.jmock.core.Invokable;
import org.jmock.core.MockObjectNamingScheme;
import org.jmock.internal.ContextControl;
import org.jmock.internal.ExpectationCapture;
import org.jmock.internal.IdentityExpectationErrorTranslator;
import org.jmock.internal.InvocationDiverter;
import org.jmock.internal.UnspecifiedExpectation;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.NatsNamingScheme;
import org.jmock.lib.action.ReturnDefaultValueAction;


/**
 * Where all the mocks live.
 * 
 * Named by Ivan Moore.
 * 
 * @author npryce
 *
 */
public class Mockery {
    private Imposteriser imposteriser = new JavaReflectionImposteriser();
    private Action defaultAction = new ReturnDefaultValueAction(imposteriser);
    private ExpectationErrorTranslator expectationErrorTranslator = IdentityExpectationErrorTranslator.INSTANCE;
    private MockObjectNamingScheme namingScheme = NatsNamingScheme.INSTANCE;
    
    private Expectation expectation = new UnspecifiedExpectation();
    private ExpectationCapture capture = null;
    private Throwable firstError = null;
    
    
    /* 
     * Policies
     */
    
    public void setDefaultAction(Action defaultAction) {
        this.defaultAction = defaultAction;
    }
    
    public void setImposteriser(Imposteriser imposteriser) {
        this.imposteriser = imposteriser;
    }
    
    public void setNamingScheme(MockObjectNamingScheme namingScheme) {
        this.namingScheme = namingScheme;
    }
    
    public void setExpectationErrorTranslator(ExpectationErrorTranslator expectationErrorTranslator) {
        this.expectationErrorTranslator = expectationErrorTranslator;
    }
    
    
    public <T> T mock(Class<T> typeToMock) {
		return mock(typeToMock, namingScheme.defaultNameFor(typeToMock));
	}
    
    public <T> T mock(Class<T> typeToMock, String name) {
        MockObject mock = new MockObject(name);
        return imposteriser.imposterise(
            divert(Object.class, mock, 
            divert(ContextControl.class, mock, 
                   mock)), 
            typeToMock, ContextControl.class);
    }
    
    private <T> Invokable divert(Class<T> type, T receiver, Invokable next) {
        return new InvocationDiverter<T>(type, receiver, next);
    }
    
	public void expects(ExpectationCapture capture) {
        capture.stopCapturingExpectations();
	}
	
	public void assertIsSatisfied() {
        firstError = null;
        if (!expectation.isSatisfied()) {
            throw expectationErrorTranslator.translate(new ExpectationError("not all expectations were satisfied", expectation));
        }
	}
    
    private Object dispatch(Invocation invocation) throws Throwable {
        if (isCapturingExpectations()) {
            capture.createExpectationFrom(invocation);
            return defaultAction.invoke(invocation);
        }
        else if (firstError != null) {
            throw firstError;
        }
        else {
            try {
                check(invocation);
                return expectation.invoke(invocation);
            }
            catch (ExpectationError e) {
                firstError = expectationErrorTranslator.translate(e);
                firstError.setStackTrace(e.getStackTrace());
                throw firstError;
            }
        }
    }
    
    private boolean isCapturingExpectations() {
        return capture != null;
    }
    
    private void check(Invocation invocation) {
        if (!expectation.matches(invocation)) {
            throw new ExpectationError("unexpected invocation", expectation, invocation);
        }
    }
    
    private class MockObject implements Invokable, ContextControl {
        private String name;
        
        public MockObject(String name) {
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
        
        public void startCapturingExpectations(ExpectationCapture newCapture) {
            capture = newCapture;
            newCapture.setDefaultAction(defaultAction);
        }
        
        public void setExpectation(Expectation newExpectation) {
            expectation = newExpectation;
            capture = null;
        }
        
        public Object invoke(Invocation invocation) throws Throwable {
            return dispatch(invocation);
        }
    }
}
