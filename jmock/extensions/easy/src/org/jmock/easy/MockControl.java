/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import org.jmock.core.Verifiable;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.VoidStub;
import org.jmock.core.stub.ThrowStub;
import org.jmock.easy.internal.Range;

public class MockControl 
	implements Verifiable
{
	private EasyCoreMock coreMock;

    public static final Range ONE = new Range(1);
    public static final Range ONE_OR_MORE = new Range(1, Integer.MAX_VALUE);
    public static final Range ZERO_OR_MORE = new Range(0, Integer.MAX_VALUE);

    public MockControl(EasyCoreMock coreMock) {
		this.coreMock = coreMock;
	}
	
	public static MockControl createControl(Class mockedType) {
		return new MockControl(new EasyCoreMock(mockedType));
	}

	public Object getMock() {
		return coreMock.proxy();
	}

	public void replay() {
		coreMock.replay();
	}

	public void verify() {
		coreMock.verify();
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
        coreMock.setDefaultStub(new ReturnStub(value));
    }

    public void setDefaultVoidCallable() {
        coreMock.setDefaultStub(new VoidStub());
    }

}
