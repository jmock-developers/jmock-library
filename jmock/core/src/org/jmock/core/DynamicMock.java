/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;


public interface DynamicMock
{
    Class getMockedType();
    Object proxy();
    void reset();
}

