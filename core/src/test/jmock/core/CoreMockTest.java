/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.*;
import org.jmock.expectation.AssertMo;
import test.jmock.core.testsupport.MockInvocationDispatcher;
import test.jmock.core.testsupport.MockInvokable;
import test.jmock.core.testsupport.MockStub;


public class CoreMockTest extends AbstractDynamicMockTest
{
    protected DynamicMock createDynamicMock( String name, InvocationDispatcher dispatcher ) {
        return new CoreMock( DummyInterface.class, name, dispatcher );
    }

    protected Class mockedType() {
        return DummyInterface.class;
    }
}
