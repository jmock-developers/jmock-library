/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core;

import org.jmock.core.CoreMock;
import org.jmock.core.DynamicMock;
import org.jmock.core.InvocationDispatcher;


public class CoreMockTest extends AbstractDynamicMockTest
{
    protected DynamicMock createDynamicMock( String name, InvocationDispatcher dispatcher ) {
        return new CoreMock( DummyInterface.class, name, dispatcher );
    }

    protected Class mockedType() {
        return DummyInterface.class;
    }
}
