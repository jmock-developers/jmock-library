/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import org.jmock.core.InvocationDispatcher;
import org.jmock.core.OrderedInvocationDispatcher;
import org.jmock.core.Verifiable;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;
import org.jmock.core.stub.VoidStub;
import org.jmock.easy.internal.Range;

public class MockControl 
	implements Verifiable
{
	private EasyCoreMock coreMock;
    private InvocationDispatcher dispatcher;

    public static final Range ONE = new Range(1);
    public static final Range ONE_OR_MORE = new Range(1, Integer.MAX_VALUE);
    public static final Range ZERO_OR_MORE = new Range(0, Integer.MAX_VALUE);

    public MockControl(EasyCoreMock coreMock, InvocationDispatcher dispatcher) {
		this.coreMock = coreMock;
        this.dispatcher = dispatcher;
	}
	
	public static MockControl createControl(Class mockedType) {
        InvocationDispatcher dispatcher = new OrderedInvocationDispatcher(new OrderedInvocationDispatcher.FIFOInvokablesCollection());
		return new MockControl(new EasyCoreMock(mockedType, dispatcher), dispatcher);
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

	public void setVoidCallable(Range range) {
        coreMock.setExpectedStub(range, new VoidStub());
    }

	public void setReturnValue(Object value, Range range) {
        coreMock.setExpectedStub(range, new ReturnStub(value));
    }

    public void setThrowable(Throwable throwable, Range range) {
        coreMock.setExpectedStub(range, new ThrowStub(throwable));
    }

    public void setDefaultReturnValue(Object value) {
        dispatcher.setDefaultStub(new ReturnStub(value));
    }

    public void setDefaultVoidCallable() {
        dispatcher.setDefaultStub(new VoidStub());
    }

}
