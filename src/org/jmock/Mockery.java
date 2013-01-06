package org.jmock;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.jmock.api.*;
import org.jmock.internal.*;
import org.jmock.lib.CamelCaseNamingScheme;
import org.jmock.lib.IdentityExpectationErrorTranslator;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.lib.concurrent.Synchroniser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A Mockery represents the context, or neighbourhood, of the object(s) under test.  
 * 
 * The neighbouring objects in that context are mocked out. The test specifies the 
 * expected interactions between the object(s) under test and its  neighbours and 
 * the Mockery checks those expectations while the test is running.
 * 
 * @author npryce
 * @author smgf
 * @author named by Ivan Moore.
 */
public class Mockery implements SelfDescribing {
    private Imposteriser imposteriser = JavaReflectionImposteriser.INSTANCE;
    private ExpectationErrorTranslator expectationErrorTranslator = IdentityExpectationErrorTranslator.INSTANCE;
    private MockObjectNamingScheme namingScheme = CamelCaseNamingScheme.INSTANCE;
    private ThreadingPolicy threadingPolicy = new SingleThreadedPolicy();

    private final Set<String> mockNames = new HashSet<String>();
    private final ReturnDefaultValueAction defaultAction = new ReturnDefaultValueAction(imposteriser);
    private final List<Invocation> actualInvocations = new ArrayList<Invocation>();
    private final InvocationDispatcher dispatcher = new InvocationDispatcher();

    private Error firstError = null;

    /* 
     * Policies
     */
    
    /**
     * Sets the result returned for the given type when no return value has been explicitly
     * specified in the expectation.
     * 
     * @param type
     *    The type for which to return <var>result</var>.
     * @param result
     *    The value to return when a method of return type <var>type</var>
     *    is invoked for which an explicit return value has has not been specified.
     */
    public void setDefaultResultForType(Class<?> type, Object result) {
        defaultAction.addResult(type, result);
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
        this.defaultAction.setImposteriser(imposteriser);
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
    
    /**
     * Changes the policy by which the Mockery copes with multiple threads.
     * 
     *  The default policy throws an exception if the Mockery is called from different
     *  threads.
     *  
     *  @see Synchroniser
     */
    public void setThreadingPolicy(ThreadingPolicy threadingPolicy) {
        this.threadingPolicy = threadingPolicy;
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
        if (mockNames.contains(name)) {
            throw new IllegalArgumentException("a mock with name " + name + " already exists");
        }
        
        final MockObject mock = new MockObject(typeToMock, name);
        mockNames.add(name);
        
        Invokable invokable =
            threadingPolicy.synchroniseAccessTo(
                new ProxiedObjectIdentity(
                    new InvocationDiverter<CaptureControl>(
                        CaptureControl.class, mock, mock)));
        
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
     * 
     * This method can be called multiple times per test and the expectations defined in
     * each block are combined as if they were defined in same order within a single block.
     */
	public void checking(ExpectationBuilder expectations) {
	    expectations.buildExpectations(defaultAction, dispatcher);
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
	    if (firstError != null) {
	        throw firstError;
	    }
	    else if (!dispatcher.isSatisfied()) {
            throw expectationErrorTranslator.translate(
                ExpectationError.notAllSatisfied(this));
        }
	}
    
    public void describeTo(Description description) {
        description.appendDescriptionOf(dispatcher);
        describeHistory(description);
    }

    private void describeMismatch(Invocation invocation, Description description) {
        dispatcher.describeMismatch(invocation, description);
        describeHistory(description);
    }
    
    private void describeHistory(Description description) {
        description.appendText("\nwhat happened before this:");
        final List<Invocation> invocationsSoFar = new ArrayList<Invocation>(actualInvocations);
        if (invocationsSoFar.isEmpty()) {
            description.appendText(" nothing!");
        }
        else {
            description.appendList("\n  ", "\n  ", "\n", invocationsSoFar);
        }
    }

    private Object dispatch(Invocation invocation) throws Throwable {
        if (firstError != null) {
            throw firstError;
        }
        
        try {
            Object result = dispatcher.dispatch(invocation);
            actualInvocations.add(invocation);
            return result;
        }
        catch (ExpectationError e) {
            firstError = expectationErrorTranslator.translate(mismatchDescribing(e));
            firstError.setStackTrace(e.getStackTrace());
            throw firstError;
        }
        catch (Throwable t) {
            actualInvocations.add(invocation);
            throw t;
        }
    }
    
    private ExpectationError mismatchDescribing(final ExpectationError e) {
        ExpectationError filledIn = new ExpectationError(e.getMessage(), new SelfDescribing() {
            public void describeTo(Description description) {
                describeMismatch(e.invocation, description);
            }
        }, e.invocation);
        filledIn.setStackTrace(e.getStackTrace());
        return filledIn;
    }

    private class MockObject implements Invokable, CaptureControl {
        private Class<?> mockedType;
        private String name;
        
        public MockObject(Class<?> mockedType, String name) {
            this.name = name;
            this.mockedType = mockedType;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
        public Object invoke(Invocation invocation) throws Throwable {
            return dispatch(invocation);
        }

        public Object captureExpectationTo(ExpectationCapture capture) {
            return imposteriser.imposterise(
                new ObjectMethodExpectationBouncer(new InvocationToExpectationTranslator(capture, defaultAction)), 
                mockedType);
        }
    }

}
