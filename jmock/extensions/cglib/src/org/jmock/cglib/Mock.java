/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import org.jmock.core.DynamicMock;
import org.jmock.core.OrderedInvocationDispatcher;


public class Mock extends org.jmock.Mock
{
    public Mock( Class mockedType ) {
        this(new CGLIBCoreMock(mockedType));
    }

    public Mock( Class mockedType, String name ) {
        this(new CGLIBCoreMock(mockedType, name));
    }

    public Mock( DynamicMock coreMock ) {
        super(coreMock, 
                new OrderedInvocationDispatcher(new OrderedInvocationDispatcher.LIFOInvokablesCollection()));
    }
}
