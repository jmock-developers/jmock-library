package org.jmock.cglib;

import org.jmock.core.DynamicMock;


public class Mock extends org.jmock.Mock
{
	public Mock( Class mockedType ) {
		this(new CGLIBCoreMock(mockedType));
	}

	public Mock( Class mockedType, String name ) {
		this(new CGLIBCoreMock(mockedType, name));
	}

	public Mock( DynamicMock coreMock ) {
		super(coreMock);
	}
}
