/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import org.jmock.core.InvocationDispatcher;
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

	public void setVoidCallable(Range range) {
        invocationMatch.expectCallCount(range, new VoidStub());
    }

	public void setReturnValue(Object value, Range range) {
        invocationMatch.expectCallCount(range, new ReturnStub(value));
    }

    public void setThrowable(Throwable throwable, Range range) {
        invocationMatch.expectCallCount(range, new ThrowStub(throwable));
    }

    public void setDefaultReturnValue(Object value) {
        dispatcher.setDefaultStub(new ReturnStub(value));
    }

    public void setDefaultVoidCallable() {
        dispatcher.setDefaultStub(new VoidStub());
    }

}
