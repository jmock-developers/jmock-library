/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.TestCase;
import org.jmock.stub.ReturnStub;
import org.jmock.stub.ThrowStub;

public class StubTest extends TestCase {

    public StubTest(String name) {
        super(name);
    }

    Invocation invocation = new Invocation(Void.class, "ignoredName", new Class[0], void.class, new Object[0]);

    public void testReturnStub() throws Throwable {
        final String RESULT = "result";

        assertSame("Should be the same result object", RESULT, new ReturnStub(RESULT).invoke(invocation));
    }

    public void testThrowStub() {
        final Throwable throwable = new DummyThrowable();

        try {
            new ThrowStub(throwable).invoke(invocation);
        } catch (Throwable t) {
            assertSame("Should be the same throwable", throwable, t);
        }
    }

}
