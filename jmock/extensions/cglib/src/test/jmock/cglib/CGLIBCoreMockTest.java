/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.cglib;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.cglib.CGLIBCoreMock;
import org.jmock.core.Invocation;
import org.jmock.core.LIFOInvocationDispatcher;
import org.jmock.core.DynamicMock;
import org.jmock.core.InvocationDispatcher;
import org.jmock.expectation.AssertMo;
import test.jmock.core.DummyInterface;
import test.jmock.core.DummyThrowable;
import test.jmock.core.AbstractDynamicMockTest;
import test.jmock.core.testsupport.MockInvocationDispatcher;
import test.jmock.core.testsupport.MockInvokable;
import test.jmock.core.testsupport.MockStub;


public class CGLIBCoreMockTest extends AbstractDynamicMockTest
{
    static class ConcreteType implements DummyInterface {
        public void noArgVoidMethod() throws Throwable {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public String noArgMethod() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String oneArgMethod( String arg1 ) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String twoArgMethod( String arg1, String arg2 ) throws Throwable {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void method() {
        }
    }

    protected DynamicMock createDynamicMock( String name, InvocationDispatcher dispatcher ) {
        return new CGLIBCoreMock( ConcreteType.class, name, dispatcher );
    }
    
    protected Class mockedType() {
        return ConcreteType.class;
    }
}
