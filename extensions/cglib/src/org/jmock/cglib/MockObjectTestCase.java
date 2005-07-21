/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import org.jmock.Mock;
import org.jmock.core.DynamicMock;


public abstract class MockObjectTestCase extends org.jmock.MockObjectTestCase
{
    protected MockObjectTestCase(String testName) {
        super(testName);
    }
    
    protected MockObjectTestCase() {
    }

    public Mock mock(Class mockedClass, String roleName, 
            		 Class[] constructorArgumentTypes, Object[] constructorArguments) 
    {
        Mock newMock = new Mock(newCoreMock(mockedClass, roleName, 
                							constructorArgumentTypes, constructorArguments));
        registerToVerify(newMock);
        return newMock;
    }

    public Mock mock(Class mockedClass, Class[] constructorArgumentTypes, Object[] constructorArguments) {
        return mock(mockedClass, defaultMockNameForType(mockedClass),
                    constructorArgumentTypes, constructorArguments);
    }
    
    protected DynamicMock newCoreMock( Class mockedType, String roleName ) {
        return new CGLIBCoreMock(mockedType, roleName);
    }
    
    protected DynamicMock newCoreMock( Class mockedClass, String roleName,
                                       Class[] constructorArgumentTypes, Object[] constructorArguments) 
    {
        return new CGLIBCoreMock(mockedClass, roleName, constructorArgumentTypes, constructorArguments);
    }
}
