/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.cglib;

import org.jmock.cglib.CGLIBCoreMock;
import org.jmock.core.DynamicMock;
import org.jmock.core.InvocationDispatcher;

import test.jmock.core.AbstractDynamicMockTest;
import test.jmock.core.DummyInterface;


public class CGLIBCoreMockTest extends AbstractDynamicMockTest
{
    public static class ConcreteType implements DummyInterface {
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
