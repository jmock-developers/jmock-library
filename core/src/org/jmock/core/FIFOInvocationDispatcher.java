/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import org.jmock.core.stub.TestFailureStub;


public class FIFOInvocationDispatcher
    extends AbstractInvocationDispatcher
{
    protected Iterator dispatchOrder( List invokables ) {
        return invokables.iterator();
    }
}
