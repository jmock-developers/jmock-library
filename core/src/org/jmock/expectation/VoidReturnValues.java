/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;


/**
 * Sequence of void values as required by MockMaker
 * This is a generic class that should have been introduced to the mockobjects code stream instead of
 * being separately included in org.mockobjects.
 * It is possibly similar to a ReturnObjectList?
 */
public class VoidReturnValues extends ReturnValues
{
    public VoidReturnValues( String name, boolean keepUsingLastReturnValue ) {
        super(name, keepUsingLastReturnValue);
    }

    public VoidReturnValues( boolean keepUsingLastReturnValue ) {
        super(keepUsingLastReturnValue);
    }

    public Object getNext() {
        return myContents.isEmpty() ? null : pop();
    }
}
