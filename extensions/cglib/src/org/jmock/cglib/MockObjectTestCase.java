/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import org.jmock.core.DynamicMock;


public abstract class MockObjectTestCase extends org.jmock.MockObjectTestCase
{
    protected DynamicMock newCoreMock( Class mockedType, String roleName ) {
        return new CGLIBCoreMock(mockedType, roleName);
    }
}
