/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

public abstract class MockObjectTestCase extends org.jmock.MockObjectTestCase
{
    protected org.jmock.Mock newMock( Class mockedType, String roleName ) {
        return new Mock(mockedType, roleName);
    }
}
