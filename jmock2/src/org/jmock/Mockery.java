package org.jmock;

import org.jmock.api.Action;
import org.jmock.api.Expectation;
import org.jmock.api.ExpectationError;
import org.jmock.api.ExpectationErrorTranslator;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.api.MockObjectNamingScheme;
import org.jmock.internal.CaptureControl;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.internal.ExpectationCapture;
import org.jmock.internal.InvocationDispatcher;
import org.jmock.internal.InvocationDiverter;
import org.jmock.internal.NamedSequence;
import org.jmock.internal.ProxiedObjectIdentity;
import org.jmock.lib.CamelCaseNamingScheme;
import org.jmock.lib.IdentityExpectationErrorTranslator;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.action.ReturnDefaultValueAction;


/**
 * A Mockery represents the context, or neighbourhood, of the object(s) under test.  
 * 
 * The neighbouring objects in that context are mocked out. The test specifies the 
 * expected interactions between the object(s) under test and its  neighbours and 
 * the Mockery checks those expectations while the test is running.
 * 
 * @author npryce
 * @author named by Ivan Moore.
 * @author smgf
 */
public class Mockery {
    private Imposteriser imposteriser = new JavaReflectionImposteriser();
    private Action defaultAction = new ReturnDefaultValueAction(imposteriser);
    private ExpectationErrorTranslator expectationErrorTranslator = IdentityExpectationErrorTranslator.INSTANCE;
    private MockObjectNamingScheme namingScheme = CamelCaseNamingScheme.INSTANCE;
    
    private InvocationDispatcher dispatcher = new InvocationDispatcher();
    private ExpectationCapture capture = null;
    private Throwable firstError = null;
    
    
    /* 
     * Policies
     */
    
    /**
     * Changes the action used be expectations for which no action has been explicity
     * defined.  
     * 
     * The default default action returns a value that is compatible with the method's
     * return type but may be null.
     */
    public void setDefaultAction(Action defaultAction) {
        this.defaultAction = defaultAction;
    }
    
    /**
     * Changes the imposteriser used to adapt mock objects to the mocked type.
     * 
     * The default imposteriser allows a test to mock interfaces but not
     * classes, so you'll have to plug a different imposteriser into the
     * Mockery if you want to mock classes.
     */
    public void setImposteriser(Imposteriser imposteriser) {
        this.imposteriser = imposteriser;
    }
    
    /**
     * Changes the naming scheme used to generate names for mock objects that 
     * have not been explicitly named in the test.
     * 
     * The default naming scheme names mock objects by lower-casing the first
     * letter of the class name, so a mock object of type BananaSplit will be
     * called "bananaSplit" if it is not explicitly named in the test.
     */
    public void setNamingScheme(MockObjectNamingScheme namingScheme) {
        this.namingScheme = namingScheme;
    }
    
    /**
     * Changes the expectation error translator used to translate expectation
     * errors into errors that report test failures.
     * 
     * By default, expectation errors are not translated and are thrown as
     * errors of type {@link ExpectationError}.  Plug in a new expectation error
     * translator if you want your favourite test framework to report expectation 
     * failures using its own error type.
     */
    public void setExpectationErrorTranslator(ExpectationErrorTranslator expectationErrorTranslator) {
        this.expectationErrorTranslator = expectationErrorTranslator;
    }
    
    /*
     * API
     */
    
    /**
     * Creates a mock object of type <var>typeToMock</var> and generates a name for it.
     */
    public <T> T mock(Class<T> typeToMock) {
		return mock(typeToMock, namingScheme.defaultNameFor(typeToMock));
	}
    
    /**
     * Creates a mock object of type <var>typeToMock</var> with the given name.
     */
    public <T> T mock(Class<T> typeToMock, String name) {
        MockObject mock = new MockObject(name);
        ProxiedObjectIdentity invokable = 
            new ProxiedObjectIdentity(
                new InvocationDiverter<CaptureControl>(
                    CaptureControl.class, mock, mock));
        
        return imposteriser.imposterise(invokable, typeToMock, CaptureControl.class);
    }
    
    /** 
     * Returns a new sequence that is used to constrain the order in which 
     * expectations can occur.
     * 
     * @param name
     *     The name of the sequence.
     * @return
     *     A new sequence with the given name.
     */
    public Sequence sequence(String name) {
        return new NamedSequence(name);
    }
    
    /** 
     * Returns a new state machine that is used to constrain the order in which 
     * expectations can occur.
     * 
     * @param name
     *     The name of the state machine.
     * @return
     *     A new state machine with the given name.
     */
    public States states(String name) {
        return dispatcher.newStateMachine(name);
    }
    
    /**
     * Specifies the expected invocations that the object under test will perform upon
     * objects in its context during the test.
     * 
     * The builder is responsible for interpreting high-level, readable API calls to 
     * construct an expectation.
     */
	public void checking(ExpectationBuilder expectations) {
	    expectations.buildExpectations(defaultAction, dispatcher);
        capture = null;
    }
	
    /**
     * Adds an expected invocation that the object under test will perform upon
     * objects in its context during the test.
     * 
     * This method allows a test to define an expectation explicitly, bypassing the
     * high-level API, if desired.
     */
    public void addExpectation(Expectation expectation) {
        dispatcher.add(expectation);
    }
	
    /**
     * Fails the test if there are any expectations that have not been met.
     */
	public void assertIsSatisfied() {
        firstError = null;
        if (!dispatcher.isSatisfied()) {
            throw expectationErrorTranslator.translate(new ExpectationError("not all expectations were satisfied", dispatcher));
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
                return dispatcher.dispatch(invocation);
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
    
    private class MockObject implements Invokable, CaptureControl {
        private String name;
        
        public MockObject(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
        public Object invoke(Invocation invocation) throws Throwable {
            return dispatch(invocation);
        }
        
        public void startCapturingExpectations(ExpectationCapture newCapture) {
            capture = newCapture;
        }
        
        public void stopCapturingExpectations() {
            capture = null;
        }
    }
}
