/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import org.jmock.core.InvocationDispatcher;
import org.jmock.core.Stub;
import org.jmock.core.Verifiable;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;
import org.jmock.core.stub.VoidStub;
import org.jmock.easy.internal.EasyInvocationDispatcher;
import org.jmock.easy.internal.InvocationMatch;
import org.jmock.easy.internal.Range;

public class MockControl 
	implements Verifiable
{
	private EasyCoreMock coreMock;
    private InvocationMatch invocationMatch;
    private InvocationDispatcher dispatcher;

    public static final Range ONE = new Range(1);
    public static final Range ONE_OR_MORE = new Range(1, Integer.MAX_VALUE);
    public static final Range ZERO_OR_MORE = new Range(0, Integer.MAX_VALUE);

    public MockControl(Class mockedType, EasyInvocationDispatcher dispatcher, InvocationMatch invocationMatch) {
		this.coreMock = new EasyCoreMock(mockedType, dispatcher, invocationMatch);
        this.dispatcher = dispatcher;
        this.invocationMatch = invocationMatch;
	}
	
	public static MockControl createControl(Class mockedType) {
		return new MockControl(mockedType, new EasyInvocationDispatcher(), new InvocationMatch());
	}

	public Object getMock() {
		return coreMock.proxy();
	}

	public void replay() {
		coreMock.replay();
	}

	public void verify() {
		dispatcher.verify();
	}

    public void setVoidCallable() {
        setVoidCallable(ONE);
    }

	public void setVoidCallable(Range range) {
        expectStub(range, new VoidStub());
    }

	public void setReturnValue(Range range, Object value) {
        expectStub(range, new ReturnStub(value));
    }

    public void setThrowable(Throwable throwable) {
    	setThrowable(throwable, ONE);
    }
    
    public void setThrowable(Throwable throwable, int times) {
        setThrowable(throwable, new Range(times));
    }

    public void setThrowable(Throwable throwable, int minCount, int maxCount) {
        setThrowable(throwable, new Range(minCount, maxCount));
    }
    
    public void setThrowable(Throwable throwable, Range range) {
        expectStub(range, new ThrowStub(throwable));
    }

    public void setDefaultReturnValue(Object value) {
        dispatcher.setDefaultStub(new ReturnStub(value));
    }

    public void setDefaultReturnValue(boolean value) {
    	setDefaultReturnValue(new Boolean(value));
    }
    
    public void setDefaultReturnValue(long value) {
        setDefaultReturnValue(convertLong(value));
    }
    
	public void setDefaultReturnValue(float value) {
        setDefaultReturnValue(new Float(value));
    }
    
    public void setDefaultReturnValue(double value) {
        setDefaultReturnValue(new Double(value));
    }
    
    public void setDefaultVoidCallable() {
        dispatcher.setDefaultStub(new VoidStub());
    }

	public void setDefaultThrowable(Throwable throwable) {
        dispatcher.setDefaultStub(new ThrowStub(throwable));
	}

    private Object convertLong(long value) {
        return invocationMatch.createNumberObjectForReturnValue(value);
    }

	private void expectStub(Range range, Stub stub) {
        invocationMatch.expectCountedCall(range, stub);
    }

	/**
	 * Records that the mock object will expect the last method call once, and
	 * will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>boolean</code>.
	 */
	public void setReturnValue(boolean value) {
        setReturnValue(new Boolean(value));
	}

	/**
	 * Records that the mock object will expect the last method call once, and
	 * will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>byte, short, char, int, or long</code>.
	 */
	public void setReturnValue(long value) {
        setReturnValue(convertLong(value));
	}

	/**
	 * Records that the mock object will expect the last method call once, and
	 * will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>float</code>.
	 */
	public void setReturnValue(float value) {
        setReturnValue(new Float(value));
	}

	/**
	 * Records that the mock object will expect the last method call once, and
	 * will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>double</code>.
	 */
	public void setReturnValue(double value) {
        setReturnValue(new Double(value));
	}

	/**
	 * Records that the mock object will expect the last method call once, and
	 * will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return an object.
	 * @throws IllegalArgumentException
	 *             if the provided return value is not compatible to the return
	 *             value of the last method called on the mock object.
	 */
	public void setReturnValue(Object value) {
        setReturnValue(ONE, value);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(boolean)}. For explanation,
	 * see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(boolean ignored, boolean value) {
	    setReturnValue(value);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(long)}. For explanation, see
	 * "Convenience Methods for Return Values" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(long ignored, long value) {
	    setReturnValue(convertLong(value));
	}

	/**
	 * Same as {@link MockControl#setReturnValue(float)}. For explanation, see
	 * "Convenience Methods for Return Values" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(float ignored, float value) {
	    setReturnValue(value);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(double)}. For explanation,
	 * see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(double ignored, double value) {
	    setReturnValue(value);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(Object)}. For explanation,
	 * see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(Object ignored, Object value) {
	    setReturnValue(value);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(boolean, Range)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(boolean ignored, boolean value, Range range) {
	    setReturnValue(value, range);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(long, Range)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(long ignored, long value, Range range) {
	    setReturnValue(convertLong(value), range);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(float, Range)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(float ignored, float value, Range range) {
	    setReturnValue(value, range);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(double, Range)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(double ignored, double value, Range range) {
	    setReturnValue(value, range);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(Object, Range)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(Object ignored, Object value, Range range) {
	    setReturnValue(value, range);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(boolean, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(boolean ignored, boolean value, int count) {
	    setReturnValue(value, count);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(long, int)}. For explanation,
	 * see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(long ignored, long value, int count) {
	    setReturnValue(value, count);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(float, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(float ignored, float value, int count) {
	    setReturnValue(value, count);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(double, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(double ignored, double value, int count) {
	    setReturnValue(value, count);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(Object, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(Object ignored, Object value, int count) {
	    setReturnValue(value, count);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(boolean, int, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(boolean ignored, boolean value, int min, int max) {
	    setReturnValue(value, min, max);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(long, int, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(long ignored, long value, int min, int max) {
	    setReturnValue(convertLong(value), min, max);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(float, int, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(float ignored, float value, int min, int max) {
	    setReturnValue(value, min, max);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(double, int, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(double ignored, double value, int min, int max) {
	    setReturnValue(value, min, max);
	}

	/**
	 * Same as {@link MockControl#setReturnValue(Object, int, int)}. For
	 * explanation, see "Convenience Methods for Return Values" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndReturn(Object ignored, Object value, int min, int max) {
	    setReturnValue(value, min, max);
	}

	/**
	 * Records that the mock object will expect the last method call the number
	 * of times specified by the range argument, and will react by returning
	 * the provided return value. Available range arguments are
	 * {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param range
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>boolean</code>.
	 */
	public void setReturnValue(boolean value, Range range) {
        setReturnValue(new Boolean(value), range);
	}

	/**
	 * Records that the mock object will expect the last method call the number
	 * of times specified by the range argument, and will react by returning
	 * the provided return value. Available range arguments are
	 * {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param range
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>double</code>.
	 */
	public void setReturnValue(double value, Range range) {
        setReturnValue(new Double(value), range);
	}

	/**
	 * Records that the mock object will expect the last method call the number
	 * of times specified by the range argument, and will react by returning
	 * the provided return value. Available range arguments are
	 * {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param range
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>float</code>.
	 */
	public void setReturnValue(float value, Range range) {
        setReturnValue(new Float(value), range);
	}

	/**
	 * Records that the mock object will expect the last method call the number
	 * of times specified by the range argument, and will react by returning
	 * the provided return value. Available range arguments are
	 * {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param range
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>byte, short, char, int, or long</code>.
	 */
	public void setReturnValue(long value, Range range) {
        setReturnValue(convertLong(value), range);
	}

	/**
	 * Records that the mock object will expect the last method call the number
	 * of times specified by the range argument, and will react by returning
	 * the provided return value. Available range arguments are
	 * {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param range
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return an object.
	 * @throws IllegalArgumentException
	 *             if the provided return value is not compatible to the return
	 *             value of the last method called on the mock object.
	 */
	public void setReturnValue(Object value, Range range) {
        expectStub(range, new ReturnStub(value));
	}

	/**
	 * Records that the mock object will expect the last method call a fixed
	 * number of times, and will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @param times
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>boolean</code>.
	 */
	public void setReturnValue(boolean value, int times) {
        setReturnValue(value, new Range(times));
	}

	/**
	 * Records that the mock object will expect the last method call a fixed
	 * number of times, and will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @param times
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>double</code>.
	 */
	public void setReturnValue(double value, int times) {
        setReturnValue(value, new Range(times));
	}

	/**
	 * Records that the mock object will expect the last method call a fixed
	 * number of times, and will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @param times
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>float</code>.
	 */
	public void setReturnValue(float value, int times) {
        setReturnValue(value, new Range(times));
	}

	/**
	 * Records that the mock object will expect the last method call a fixed
	 * number of times, and will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @param times
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>byte, short, char, int, or long</code>.
	 */
	public void setReturnValue(long value, int times) {
        setReturnValue(convertLong(value), new Range(times));
	}

	/**
	 * Records that the mock object will expect the last method call a fixed
	 * number of times, and will react by returning the provided return value.
	 * 
	 * @param value
	 *            the return value.
	 * @param times
	 *            the number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return an object.
	 * @throws IllegalArgumentException
	 *             if the provided return value is not compatible to the return
	 *             value of the last method called on the mock object.
	 */
	public void setReturnValue(Object value, int times) {
        setReturnValue(value, new Range(times));
	}

	/**
	 * Records that the mock object will expect the last method call between
	 * <code>minCount</code> and <code>maxCount</code> times, and will
	 * react by returning the provided return value. Available range arguments
	 * are {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param minCount
	 *            the minimum number of times that the call is expected.
	 * @param maxCount
	 *            the maximum number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>boolean</code>.
	 */
	public void setReturnValue(boolean value, int minCount, int maxCount) {
        setReturnValue(value, new Range(minCount, maxCount));
	}

	/**
	 * Records that the mock object will expect the last method call between
	 * <code>minCount</code> and <code>maxCount</code> times, and will
	 * react by returning the provided return value. Available range arguments
	 * are {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param minCount
	 *            the minimum number of times that the call is expected.
	 * @param maxCount
	 *            the maximum number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>byte, short, char, int, or long</code>.
	 */
	public void setReturnValue(long value, int minCount, int maxCount) {
        setReturnValue(convertLong(value), new Range(minCount, maxCount));
	}

	/**
	 * Records that the mock object will expect the last method call between
	 * <code>minCount</code> and <code>maxCount</code> times, and will
	 * react by returning the provided return value. Available range arguments
	 * are {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param minCount
	 *            the minimum number of times that the call is expected.
	 * @param maxCount
	 *            the maximum number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>float</code>.
	 */
	public void setReturnValue(float value, int minCount, int maxCount) {
        setReturnValue(value, new Range(minCount, maxCount));
	}

	/**
	 * Records that the mock object will expect the last method call between
	 * <code>minCount</code> and <code>maxCount</code> times, and will
	 * react by returning the provided return value. Available range arguments
	 * are {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param minCount
	 *            the minimum number of times that the call is expected.
	 * @param maxCount
	 *            the maximum number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return <code>double</code>.
	 */
	public void setReturnValue(double value, int minCount, int maxCount) {
        setReturnValue(value, new Range(minCount, maxCount));
	}

	/**
	 * Records that the mock object will expect the last method call between
	 * <code>minCount</code> and <code>maxCount</code> times, and will
	 * react by returning the provided return value. Available range arguments
	 * are {@link MockControl#ZERO_OR_MORE},{@link MockControl#ONE}and
	 * {@link MockControl#ONE_OR_MORE}.
	 * 
	 * @param value
	 *            the return value.
	 * @param minCount
	 *            the minimum number of times that the call is expected.
	 * @param maxCount
	 *            the maximum number of times that the call is expected.
	 * @throws IllegalStateException
	 *             if the mock object is in replay state, if no method was
	 *             called on the mock object before. or if the last method
	 *             called on the mock does not return an object.
	 * @throws IllegalArgumentException
	 *             if the provided return value is not compatible to the return
	 *             value of the last method called on the mock object.
	 */
	public void setReturnValue(Object value, int minCount, int maxCount) {
        setReturnValue(value, new Range(minCount, maxCount));
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
	 * see "Convenience Methods for Throwables" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(boolean ignored, Throwable throwable) {
	    setThrowable(throwable);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
	 * see "Convenience Methods for Throwables" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(long ignored, Throwable throwable) {
	    setThrowable(throwable);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
	 * see "Convenience Methods for Throwables" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(float ignored, Throwable throwable) {
	    setThrowable(throwable);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
	 * see "Convenience Methods for Throwables" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(double ignored, Throwable throwable) {
	    setThrowable(throwable);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
	 * see "Convenience Methods for Throwables" in the EasyMock documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(Object ignored, Throwable throwable) {
	    setThrowable(throwable);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Range, Throwable)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(boolean ignored, Throwable throwable, Range range) {
	    setThrowable(throwable, range);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Range, Throwable)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(long ignored, Throwable throwable, Range range) {
	    setThrowable(throwable, range);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Range, Throwable)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(float ignored, Throwable throwable, Range range) {
	    setThrowable(throwable, range);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Range, Throwable)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(double ignored, Throwable throwable, Range range) {
	    setThrowable(throwable, range);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Range, Throwable)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(Object ignored, Throwable throwable, Range range) {
	    setThrowable(throwable, range);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(boolean ignored, Throwable throwable, int count) {
	    setThrowable(throwable, count);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(long ignored, Throwable throwable, int count) {
	    setThrowable(throwable, count);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(float ignored, Throwable throwable, int count) {
	    setThrowable(throwable, count);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(double ignored, Throwable throwable, int count) {
	    setThrowable(throwable, count);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(Object ignored, Throwable throwable, int count) {
	    setThrowable(throwable, count);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(boolean ignored, Throwable throwable, int min,
	        int max) {
	    setThrowable(throwable, min, max);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(long ignored, Throwable throwable, int min,
	        int max) {
	    setThrowable(throwable, min, max);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(float ignored, Throwable throwable, int min,
	        int max) {
	    setThrowable(throwable, min, max);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(double ignored, Throwable throwable, int min,
	        int max) {
	    setThrowable(throwable, min, max);
	}

	/**
	 * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
	 * explanation, see "Convenience Methods for Throwables" in the EasyMock
	 * documentation.
	 * 
	 * @param ignored
	 *            an ignored value.
	 */
	public void expectAndThrow(Object ignored, Throwable throwable, int min,
	        int max) {
	    setThrowable(throwable, min, max);
	}

}
