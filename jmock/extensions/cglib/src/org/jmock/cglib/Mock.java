/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import org.jmock.core.CoreMock;
import org.jmock.core.InvocationDispatcher;
import org.jmock.core.OrderedInvocationDispatcher;


public class Mock extends org.jmock.Mock
{
    public Mock( Class mockedType ) {
        this(mockedType, CoreMock.mockNameFromClass(mockedType));
    }

    public Mock( Class mockedType, String name ) {
        this(mockedType, name, new OrderedInvocationDispatcher(new OrderedInvocationDispatcher.LIFOInvokablesCollection()) );
    }

    public Mock( Class mockedType, String name, InvocationDispatcher dispatcher ) {
        super( new CGLIBCoreMock(mockedType, name, dispatcher), dispatcher );
    }
}
