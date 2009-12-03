package org.jmock.integration.junit3;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.api.Imposteriser;
import org.jmock.api.MockObjectNamingScheme;
import org.jmock.auto.internal.Mockomatic;
import org.jmock.internal.ExpectationBuilder;

/**
 * A {@link junit.framework.TestCase} that supports testing with mock objects.
 * It wraps a {@link org.jmock.Mockery} and automatically asserts that
 * all expectations have been met at the end of the test before 
 * {@link junit.framework.TestCase#tearDown()} is called.
 * 
 * @author npryce
 */
public abstract class MockObjectTestCase extends VerifyingTestCase {
    private final Mockery context = new Mockery();
    
    public MockObjectTestCase() {
        super();
        initialise();
    }
    
    public MockObjectTestCase(String name) {
        super(name);
        initialise();
    }
    
    private void initialise() {
        context.setExpectationErrorTranslator(JUnit3ErrorTranslator.INSTANCE);
        
        addVerifier(new Runnable() {
            public void run() { 
                context.assertIsSatisfied(); 
            }
        });
        
        Mockomatic mockomatic = new Mockomatic(context);
        mockomatic.fillIn(this);
    }

    public Mockery context() {
        return context;
    }
    
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
        context.setDefaultResultForType(type, result);
    }
    
    /**
     * Changes the imposteriser used to adapt mock objects to the mocked type.
     * 
     * The default imposteriser allows a test to mock interfaces but not
     * classes, so you'll have to plug a different imposteriser into the
     * Mockery if you want to mock classes.
     */
    public void setImposteriser(Imposteriser imposteriser) {
        context.setImposteriser(imposteriser);
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
        context.setNamingScheme(namingScheme);
    }
    
    /**
     * Specify expectations upon the mock objects in the test.
     * 
     */
    public void checking(ExpectationBuilder expectations) {
        context.checking(expectations);
    }
    
    /**
     * Create a mock object of type T with an explicit name.
     * 
     * @param typeToMock
     *  The type to be mocked
     * @param name
     *  The name of the new mock object that is used to identify the mock object
     *  in error messages
     * @return
     *  A new mock object of type
     */
    public <T> T mock(Class<T> typeToMock, String name) {
        return context.mock(typeToMock, name);
    }

    /**
     * Create a mock object of type T with a name derived from its type.
     * 
     * @param typeToMock
     *  The type to be mocked
     * @return
     *  A new mock object of type
     */
    public <T> T mock(Class<T> typeToMock) {
        return context.mock(typeToMock);
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
        return context.sequence(name);
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
        return context.states(name);
    }
}
