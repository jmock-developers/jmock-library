/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import org.jmock.core.stub.TestFailureStub;


public class LIFOInvocationDispatcher
    extends AbstractInvocationDispatcher
{
    protected Iterator dispatchOrder( List invokables ) {
        final ListIterator i = invokables.listIterator(this.invokables.size());
        return new Iterator() {
            public boolean hasNext() {
                return i.hasPrevious();
            }

            public Object next() {
                return i.previous();
            }

            public void remove() {
                throw new UnsupportedOperationException("immutable list");
            }
        };
    }
}
