package org.jmock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsSame;
import org.jmock.api.Action;
import org.jmock.internal.Cardinality;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.internal.ExpectationCollector;
import org.jmock.internal.ExpectationNamespace;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.lib.action.ActionSequence;
import org.jmock.lib.action.DoAllAction;
import org.jmock.lib.action.ReturnIteratorAction;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.ThrowAction;
import org.jmock.syntax.ActionClause;
import org.jmock.syntax.ArgumentConstraintPhrases;
import org.jmock.syntax.CardinalityClause;
import org.jmock.syntax.MethodClause;
import org.jmock.syntax.ReceiverClause;

/**
 * Provides most of the syntax of jMock's "domain-specific language" API.
 * The methods of this class don't make any sense on their own, so the
 * Javadoc is rather sparse.  Consult the documentation on the jMock 
 * website for information on how to use this API.
 * 
 * @author nat
 *
 */
public class Expectations implements ExpectationBuilder,
    CardinalityClause, ArgumentConstraintPhrases, ActionClause 
{
    private List<InvocationExpectationBuilder> builders = new ArrayList<InvocationExpectationBuilder>();
    private InvocationExpectationBuilder currentBuilder = null;
    
    private void initialiseExpectationCapture(Cardinality cardinality) {
        checkLastExpectationWasFullySpecified();
        
        currentBuilder = new InvocationExpectationBuilder();
        currentBuilder.setCardinality(cardinality);
        builders.add(currentBuilder);
    }
    
    public void buildExpectations(Action defaultAction, ExpectationCollector collector, ExpectationNamespace namespace) {
        checkLastExpectationWasFullySpecified();
        
        for (InvocationExpectationBuilder builder : builders) {
            collector.add(builder.toExpectation(defaultAction, namespace));
        }
    }
    
    protected InvocationExpectationBuilder currentBuilder() {
        if (currentBuilder == null) {
            throw new IllegalStateException("no expectations have been specified " +
                "(did you forget to to specify the cardinality of an expectation?)");
        }
        return currentBuilder;
    }
    
    private void checkLastExpectationWasFullySpecified() {
        if (currentBuilder != null) {
            currentBuilder.checkWasFullySpecified();
        }
    }
    
    /* 
     * Syntactic sugar
     */
    
    public ReceiverClause exactly(int count) {
        initialiseExpectationCapture(Cardinality.exactly(count));
        return currentBuilder;
    }
    
    public <T> T one (T mockObject) {
        return exactly(1).of(mockObject);
    }
    
    public ReceiverClause atLeast(int count) {
        initialiseExpectationCapture(Cardinality.atLeast(count));
        return currentBuilder;
    }
    
    public ReceiverClause between(int minCount, int maxCount) {
        initialiseExpectationCapture(Cardinality.between(minCount, maxCount));
        return currentBuilder;
    }
    
    public ReceiverClause atMost(int count) {
        initialiseExpectationCapture(Cardinality.atMost(count));
        return currentBuilder;
    }
    
    public MethodClause allowing(Matcher<Object> mockObjectMatcher) {
        return atLeast(0).of(mockObjectMatcher);
    }
    
    public <T> T allowing(T mockObject) {
        return atLeast(0).of(mockObject);
    }
    
    public <T> void ignoring(T mockObject) {
        atLeast(0).of(mockObject);
    }
    
    public <T> T never(T mockObject) {
        return exactly(0).of(mockObject);
    }
    
    private void addParameterMatcher(Matcher<?> matcher) {
        currentBuilder().addParameterMatcher(matcher);
    }

    public <T> T with(Matcher<T> matcher) {
        addParameterMatcher(matcher);
        return null;
    }

    public boolean with(Matcher<Boolean> matcher) {
        addParameterMatcher(matcher);
        return Boolean.FALSE;
    }
    
    public byte with(Matcher<Byte> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public short with(Matcher<Short> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public int with(Matcher<Integer> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public long with(Matcher<Long> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public float with(Matcher<Float> matcher) {
        addParameterMatcher(matcher);
        return 0.0f;
    }

    public double with(Matcher<Double> matcher) {
        addParameterMatcher(matcher);
        return 0.0;
    }
    
    public void will(Action action) {
        currentBuilder().setAction(action);
    }
    
    /* Common constraints
     */
    
    public <T> Matcher<T> equal(T value) {
        return new IsEqual<T>(value);
    }
    
    public <T> Matcher<T> same(T value) {
        return new IsSame<T>(value);
    }
    
    @SuppressWarnings("unused")
    public <T> Matcher<T> any(Class<T> type) {
        return new IsAnything<T>();
    }
    
    public <T> Matcher<T> anything() {
        return new IsAnything<T>();
    }
    
    public <T> Matcher<T> a(Class<T> type) {
        return new IsInstanceOf<T>(type);
    }
    
    public <T> Matcher<T> an(Class<T> type) {
        return new IsInstanceOf<T>(type);
    }
    
    /* Common actions
     */
    
    public Action returnValue(Object result) {
        return new ReturnValueAction(result);
    }
    
    public Action throwException(Throwable throwable) {
        return new ThrowAction(throwable);
    }
    
    public Action returnIterator(Collection<?> collection) {
        return new ReturnIteratorAction(collection);
    }
    
    public <T> Action returnIterator(T ... items) {
        return new ReturnIteratorAction(items);
    }
    
    public Action doAll(Action...actions) {
        return new DoAllAction(actions);
    }
    
    public Action onConsecutiveCalls(Action...actions) {
        return new ActionSequence(actions);
    }
    
    /* Naming and ordering
     */
    public void named(String name) {
        currentBuilder().setName(name);
    }
    
    public void before(String... subsequentExpectationNames) {
        for (String name : subsequentExpectationNames) {
            currentBuilder().addSubsequentExpectationName(name);
        }
    }
    
    public void after(String... precedingExpectationsNames) {
        for (String name : precedingExpectationsNames) {
            currentBuilder().addPrecedingExpectationName(name);
        }
    }
}
