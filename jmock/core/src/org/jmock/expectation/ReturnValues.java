/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.util.Collection;
import java.util.Vector;
import junit.framework.AssertionFailedError;


/**
 * Sequence values as required by MockMaker
 * This is a generic class that should have been introduced to the mockobjects code stream instead of
 * being separately included in org.mockobjects.
 * It is possibly similar to a ReturnObjectList?
 */
public class ReturnValues
{
    private String myName;
    protected Vector myContents = new Vector();
    private boolean myKeepUsingLastReturnValue = false;

    public ReturnValues() {
        this("Generate me with a useful name!", true);
    }

    public ReturnValues( String name, boolean keepUsingLastReturnValue ) {
        myName = name;
        myKeepUsingLastReturnValue = keepUsingLastReturnValue;
    }

    public ReturnValues( boolean keepUsingLastReturnValue ) {
        this("Generate me with a useful name!", keepUsingLastReturnValue);
    }

    public void add( Object element ) {
        myContents.addElement(element);
    }

    public void addAll( Collection returnValues ) {
        myContents.addAll(returnValues);
    }

    public Object getNext() {
        if (myContents.isEmpty()) {
            throw new AssertionFailedError(getClass().getName() + "[" + myName + "] was not setup with enough values");
        }
        return pop();
    }

    public boolean isEmpty() {
        return myContents.size() == 0;
    }

    protected Object pop() {
        Object result = myContents.firstElement();
        boolean shouldNotRemoveElement = myContents.size() == 1 && myKeepUsingLastReturnValue;
        if (!shouldNotRemoveElement) {
            myContents.removeElementAt(0);
        }
        return result;
    }
}
