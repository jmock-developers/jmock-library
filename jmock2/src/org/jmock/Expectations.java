package org.jmock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsSame;
import org.jmock.api.Action;
import org.jmock.internal.Cardinality;
import org.jmock.internal.ChangeStateSideEffect;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.internal.ExpectationCollector;
import org.jmock.internal.InStateOrderingConstraint;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.State;
import org.jmock.internal.StatePredicate;
import org.jmock.lib.action.ActionSequence;
import org.jmock.lib.action.DoAllAction;
import org.jmock.lib.action.ReturnEnumerationAction;
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
    
    public void buildExpectations(Action defaultAction, ExpectationCollector collector) {
        checkLastExpectationWasFullySpecified();
        
        for (InvocationExpectationBuilder builder : builders) {
            collector.add(builder.toExpectation(defaultAction));
        }
    }
    
    protected InvocationExpectationBuilder currentBuilder() {
        if (currentBuilder == null) {
            throw new IllegalStateException("no expectations have been specified " +
                "(did you forget to to specify the cardinality of the first expectation?)");
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
    
    // Makes the entire expectation more readable than one
    public <T> T oneOf(T mockObject) {
        return exactly(1).of(mockObject);
    }
    
    /**
     * This will eventually be deprecated. Use {@link #oneOf(Object) oneOf} instead.
     */
    public <T> T one (T mockObject) {
        return oneOf(mockObject);
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
    
    public MethodClause allowing(Matcher<?> mockObjectMatcher) {
        return atLeast(0).of(mockObjectMatcher);
    }
    
    public <T> T allowing(T mockObject) {
        return atLeast(0).of(mockObject);
    }
    
    public <T> T ignoring(T mockObject) {
        return allowing(mockObject);
    }
    
    public MethodClause ignoring(Matcher<?> mockObjectMatcher) {
        return allowing(mockObjectMatcher);
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
        return false;
    }
    
    public byte with(Matcher<Byte> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public short with(Matcher<Short> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public char with(Matcher<Character> matcher) {
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
    
    public boolean with(boolean value) {
        return with(new IsEqual<Boolean>(value));
    }
    
    public byte with(byte value) {
        return with(new IsEqual<Byte>(value));
    }
    
    public short with(short value) {
        return with(new IsEqual<Short>(value));
    }
    
    public char with(char value) {
        return with(new IsEqual<Character>(value));
    }
    
    public int with(int value) {
        return with(new IsEqual<Integer>(value));
    }
    
    public long with(long value) {
        return with(new IsEqual<Long>(value));
    }
    
    public float with(float value) {
        return with(new IsEqual<Float>(value));
    }
    
    public double with(double value) {
        return with(new IsEqual<Double>(value));
    }
    
    public <T> T with(T value) {
        return with(new IsEqual<T>(value));
    }
    
    public void will(Action action) {
        currentBuilder().setAction(action);
    }
    
    /* Common constraints
     */
    
    public static <T> Matcher<T> equal(T value) {
        return new IsEqual<T>(value);
    }
    
    public static <T> Matcher<T> same(T value) {
        return new IsSame<T>(value);
    }
    
    public static <T> Matcher<T> anything() {
        return new IsAnything<T>();
    }
    
    /**
     * @deprecated 
     *  use {@link #aNonNull} or {@link #any} until type inference actually works in a future version of Java
     */
    @Deprecated
    public static Matcher<Object> a(Class<?> type) {
        return new IsInstanceOf(type);
    }

    /**
     * @deprecated 
     *  use {@link #aNonNull} or {@link #any} until type inference actually works in a future version of Java
     */
    @Deprecated
    public static Matcher<Object> an(Class<?> type) {
        return new IsInstanceOf(type);
    }
    
    public static <T> Matcher<T> aNull(@SuppressWarnings("unused") Class<T> type) {
        return new IsNull<T>();
    }
    
    public static <T> Matcher<T> aNonNull(@SuppressWarnings("unused") Class<T> type) {
        return new IsNot<T>(new IsNull<T>());
    }
    
    /* Common actions
     */
    
    public static Action returnValue(Object result) {
        return new ReturnValueAction(result);
    }
    
    public static Action throwException(Throwable throwable) {
        return new ThrowAction(throwable);
    }
    
    public static Action returnIterator(Collection<?> collection) {
        return new ReturnIteratorAction(collection);
    }
    
    public static <T> Action returnIterator(T ... items) {
        return new ReturnIteratorAction(items);
    }
    
    public static Action returnEnumeration(Collection<?> collection) {
        return new ReturnEnumerationAction(collection);
    }
    
    public static <T> Action returnEnumeration(T ... items) {
        return new ReturnEnumerationAction(items);
    }
    
    public static Action doAll(Action...actions) {
        return new DoAllAction(actions);
    }
    
    public static Action onConsecutiveCalls(Action...actions) {
        return new ActionSequence(actions);
    }
    
    /* Naming and ordering
     */
    
    public void when(StatePredicate predicate) {
        currentBuilder().addOrderingConstraint(new InStateOrderingConstraint(predicate));
    }
    
    public void then(State state) {
        currentBuilder().addSideEffect(new ChangeStateSideEffect(state));
    }
    
    public void inSequence(Sequence sequence) {
        currentBuilder().addInSequenceOrderingConstraint(sequence);
    }

    public void inSequences(Sequence... sequences) {
        for (Sequence sequence : sequences) {
            inSequence(sequence);
        }
    }
}
