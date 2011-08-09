package org.jmock;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsSame;
import org.jmock.api.Action;
import org.jmock.internal.BoxingUtils;
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
import org.jmock.syntax.WithClause;

/**
 * Provides most of the syntax of jMock's "domain-specific language" API.
 * The methods of this class don't make any sense on their own, so the
 * Javadoc is rather sparse.  Consult the documentation on the jMock
 * website for information on how to use this API.
 *
 * @author nat
 */
public abstract class Expectations implements ExpectationBuilder,
        CardinalityClause, ArgumentConstraintPhrases, ActionClause {
    private List<InvocationExpectationBuilder> builders = new ArrayList<InvocationExpectationBuilder>();
    private InvocationExpectationBuilder currentBuilder = null;
    private boolean isBuildNow = false;

    private List<Object> objectsFromWith = new ArrayList<Object>();
    private int lastVerifiedPosInObjects = -1;
    private int currentPosIsObjectsFromWith = -1;

    private class IncompatibleClass {
    }


    protected abstract void expect() throws Exception;

    private void build() {
        while (true) {
            try {
                builders.clear();
                currentBuilder = null;
                expect();
                return; // all is right
            } catch (ClassCastException e) {
                lastVerifiedPosInObjects++;
                Object expectedClass = createExpectedArrayOrBoxingPrimitiveOrReturnNull(e);
                objectsFromWith.set(lastVerifiedPosInObjects, expectedClass);
                currentPosIsObjectsFromWith = -1;
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Object createExpectedArrayOrBoxingPrimitiveOrReturnNull(ClassCastException e) {
        /*
        should be processed correctly:
        1) Objects
        2) Primitives
        3) Arrays
        */
        final String message = e.getMessage();
        final int pos = message.indexOf("cannot be cast to ") + "cannot be cast to ".length();
        final String className = message.substring(pos);

        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isArray()) {
                return Array.newInstance(clazz.getComponentType(), 0);
            } else if (BoxingUtils.isWrapperType(clazz)) {
                if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                    return false;
                }
                if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                    return (byte) 0;
                }
                if (clazz.equals(Character.class) || clazz.equals(char.class)) {
                    return (char) 0;
                }
                if (clazz.equals(Short.class) || clazz.equals(short.class)) {
                    return (short) 0;
                }
                if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                    return 0;
                }
                if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                    return 0L;
                }
                if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                    return 0.0f;
                }
                if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                    return 0.0d;
                }

                throw new IllegalStateException("Unknown wrapper type: "+clazz);
            } else {
                return null;
            }
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException(e1);
        }
    }

    private Object getObjectFromWith() {
        currentPosIsObjectsFromWith++;
        if (objectsFromWith.size() == currentPosIsObjectsFromWith) {
            objectsFromWith.add(new IncompatibleClass());
        }
        return objectsFromWith.get(currentPosIsObjectsFromWith);
    }

    private void checkWeBuildingNow() {
        if (!isBuildNow) {
            throw new IllegalStateException("You should specify expectations only in 'expect(){...} method'");
        }
    }

    protected final WithClause with = new WithClause() {
        public boolean booleanIs(Matcher<?> matcher) {
            return (Boolean) with(matcher);// ClassCastException is expected here
        }

        public byte byteIs(Matcher<?> matcher) {
            return (Byte) with(matcher); // ClassCastException is expected here
        }

        public char charIs(Matcher<?> matcher) {
            return (Character) with(matcher);// ClassCastException is expected here
        }

        public double doubleIs(Matcher<?> matcher) {
            return (Double) with(matcher);// ClassCastException is expected here
        }

        public float floatIs(Matcher<?> matcher) {
            return (Float) with(matcher);// ClassCastException is expected here
        }

        public int intIs(Matcher<?> matcher) {
            return (Integer) with(matcher);// ClassCastException is expected here
        }

        public long longIs(Matcher<?> matcher) {
            return (Long) with(matcher);// ClassCastException is expected here
        }

        public short shortIs(Matcher<?> matcher) {
            return (Short) with(matcher);// ClassCastException is expected here
        }

        public <T> T is(Matcher<?> matcher) {
            return (T) with(matcher);// ClassCastException may be thrown here
        }
    };


    {
        isBuildNow = true;
        build();
        isBuildNow = false;
    }


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
        checkWeBuildingNow();
        initialiseExpectationCapture(Cardinality.exactly(count));
        return currentBuilder;
    }

    // Makes the entire expectation more readable than one
    public <T> T oneOf(T mockObject) {
        checkWeBuildingNow();
        return exactly(1).of(mockObject);
    }

    /**
     * @deprecated Use {@link #oneOf(Object) oneOf} instead.
     */
    public <T> T one(T mockObject) {
        checkWeBuildingNow();
        return oneOf(mockObject);
    }

    public ReceiverClause atLeast(int count) {
        checkWeBuildingNow();
        initialiseExpectationCapture(Cardinality.atLeast(count));
        return currentBuilder;
    }

    public ReceiverClause between(int minCount, int maxCount) {
        checkWeBuildingNow();
        initialiseExpectationCapture(Cardinality.between(minCount, maxCount));
        return currentBuilder;
    }

    public ReceiverClause atMost(int count) {
        checkWeBuildingNow();
        initialiseExpectationCapture(Cardinality.atMost(count));
        return currentBuilder;
    }

    public MethodClause allowing(Matcher<?> mockObjectMatcher) {
        checkWeBuildingNow();
        return atLeast(0).of(mockObjectMatcher);
    }

    public <T> T allowing(T mockObject) {
        checkWeBuildingNow();
        return atLeast(0).of(mockObject);
    }

    public <T> T ignoring(T mockObject) {
        checkWeBuildingNow();
        return allowing(mockObject);
    }

    public MethodClause ignoring(Matcher<?> mockObjectMatcher) {
        checkWeBuildingNow();
        return allowing(mockObjectMatcher);
    }

    public <T> T never(T mockObject) {
        checkWeBuildingNow();
        return exactly(0).of(mockObject);
    }

    private void addParameterMatcher(Matcher<?> matcher) {
        checkWeBuildingNow();
        currentBuilder().addParameterMatcher(matcher);
    }

    /**
     * Alternatively, use with.<T>is instead, which will work with untyped Hamcrest matchers
     */
    public <T> T with(Matcher<T> matcher) {
        checkWeBuildingNow();
        addParameterMatcher(matcher);
        return (T) getObjectFromWith();
    }

    public <T> T with(T value) {
        checkWeBuildingNow();
        return with(equal(value));
    }

    public void will(Action action) {
        checkWeBuildingNow();
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
     * @deprecated use {@link #aNonNull} or {@link #any} until type inference actually works in a future version of Java
     */
    @Deprecated
    public static Matcher<Object> a(Class<?> type) {
        return new IsInstanceOf(type);
    }

    /**
     * @deprecated use {@link #aNonNull} or {@link #any} until type inference actually works in a future version of Java
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

    public static <T> Action returnIterator(T... items) {
        return new ReturnIteratorAction(items);
    }

    public static Action returnEnumeration(Collection<?> collection) {
        return new ReturnEnumerationAction(collection);
    }

    public static <T> Action returnEnumeration(T... items) {
        return new ReturnEnumerationAction(items);
    }

    public static Action doAll(Action... actions) {
        return new DoAllAction(actions);
    }

    public static Action onConsecutiveCalls(Action... actions) {
        return new ActionSequence(actions);
    }

    /* Naming and ordering
     */

    public void when(StatePredicate predicate) {
        checkWeBuildingNow();
        currentBuilder().addOrderingConstraint(new InStateOrderingConstraint(predicate));
    }

    public void then(State state) {
        checkWeBuildingNow();
        currentBuilder().addSideEffect(new ChangeStateSideEffect(state));
    }

    public void inSequence(Sequence sequence) {
        checkWeBuildingNow();
        currentBuilder().addInSequenceOrderingConstraint(sequence);
    }

    public void inSequences(Sequence... sequences) {
        checkWeBuildingNow();
        for (Sequence sequence : sequences) {
            inSequence(sequence);
        }
    }
}
