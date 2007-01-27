package org.jmock;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsSame;
import org.jmock.api.Action;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.internal.ExpectationCollector;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.lib.Cardinality;
import org.jmock.lib.action.ActionSequence;
import org.jmock.lib.action.DoAllAction;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.ThrowAction;
import org.jmock.syntax.ActionClause;
import org.jmock.syntax.ArgumentConstraintPhrases;
import org.jmock.syntax.CardinalityClause;
import org.jmock.syntax.MethodClause;
import org.jmock.syntax.ReceiverClause;

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
    
    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        checkLastExpectationWasFullySpecified();
        
        for (InvocationExpectationBuilder builder : builders) {
            builder.setDefaultAction(defaultAction);
            
            collector.add(builder.toExpectation());
        }
    }
    
    private void checkExpectationIsBeingBuilt() {
        if (currentBuilder == null) {
            throw new IllegalStateException("no expectations have been specified " +
                "(did you forget to to specify the cardinality of an expectation?)");
        }
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
        checkExpectationIsBeingBuilt();
        
        currentBuilder.addParameterMatcher(matcher);
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
        checkExpectationIsBeingBuilt();
        
        currentBuilder.setAction(action);
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
    
    public Action doAll(Action...actions) {
        return new DoAllAction(actions);
    }
    
    public Action onConsecutiveCalls(Action...actions) {
        return new ActionSequence(actions);
    }
    
    /* Naming and ordering
     */
    public void named(String name) {
        //throw new UnsupportedOperationException("not yet implemented");
    }
    
    public void before(String... precedingExpectations) {
        //throw new UnsupportedOperationException("not yet implemented");
    }
    
    public void after(String... subsequentExpectations) {
        //throw new UnsupportedOperationException("not yet implemented");
    }
}
