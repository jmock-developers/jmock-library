/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.Iterator;
import java.util.List;


public class FIFOInvocationDispatcher
    extends AbstractInvocationDispatcher
{
    protected Iterator dispatchOrder( List invokablesList ) {
        return invokablesList.iterator();
    }
}
