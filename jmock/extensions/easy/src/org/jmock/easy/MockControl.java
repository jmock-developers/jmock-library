/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import org.jmock.core.Verifiable;

public class MockControl 
	implements Verifiable
{
	private EasyCoreMock coreMock;

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

}
