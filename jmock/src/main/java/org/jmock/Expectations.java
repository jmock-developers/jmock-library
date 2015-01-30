package org.jmock;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.core.*;
import org.jmock.api.Action;
import org.jmock.internal.*;
import org.jmock.lib.action.*;
import org.jmock.syntax.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    /**
     * Syntactic sugar for specifying arguments that are matchers for primitive types
     * or are untyped matchers.
     */
    protected final WithClause with = new WithClause() {
        public boolean booleanIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return false;
        }

        public byte byteIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public char charIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public double doubleIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public float floatIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public int intIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public long longIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public short shortIs(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return 0;
        }

        public <T> T is(Matcher<?> matcher) {
            addParameterMatcher(matcher);
            return null;
        }
    };
    
    
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
     * @deprecated Use {@link #oneOf(Object) oneOf} instead.
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
    
    /**
     * For Matchers with primitive types use the <em>with</em> field, for example:
     * <pre>with.intIs(equalTo(34));</pre>
     * For untyped matchers use:
     * <pre>with.&lt;T&gt;is(equalTo(anObject));</pre>
     */
    public <T> T with(Matcher<T> matcher) {
        addParameterMatcher(matcher);
        return null;
    }
    
    public boolean with(boolean value) {
        addParameterMatcher(equal(value));
        return false;
    }
    
    public byte with(byte value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public short with(short value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public char with(char value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public int with(int value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public long with(long value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public float with(float value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public double with(double value) {
        addParameterMatcher(equal(value));
        return 0;
    }
    
    public <T> T with(T value) {
        addParameterMatcher(equal(value));
        return value;
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
    
    public static <T> Matcher<T> any(Class<T> type) {
        return CoreMatchers.any(type);
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
