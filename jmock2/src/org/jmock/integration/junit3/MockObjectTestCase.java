package org.jmock.integration.junit3;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Imposteriser;
import org.jmock.api.MockObjectNamingScheme;
import org.jmock.internal.ExpectationBuilder;

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
     */
    public void checking(ExpectationBuilder groupBuilder) {
        context.checking(groupBuilder);
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
