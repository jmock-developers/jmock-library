/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.stub;

import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.stub.VoidStub;
import test.jmock.core.testsupport.MethodFactory;


public class VoidStubTest
        extends TestCase
{
    Invocation invocation;
    VoidStub voidStub;

    public void setUp() {
        MethodFactory methodFactory = new MethodFactory();
        invocation = new Invocation("INVOKED-OBJECT", methodFactory.newMethodReturning(void.class), new Object[0]);
        voidStub = new VoidStub();
    }

    public void testReturnsNullWhenInvoked() throws Throwable {
        assertNull("Should return null",
                   new VoidStub().invoke(invocation));
    }

    public void testIncludesVoidInDescription() {
        StringBuffer buffer = new StringBuffer();

        voidStub.describeTo(buffer);

        String description = buffer.toString();

        assertTrue("contains 'void' in description",
                   description.indexOf("void") >= 0);
    }
}
