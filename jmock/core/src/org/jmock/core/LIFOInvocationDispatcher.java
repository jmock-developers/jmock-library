/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class LIFOInvocationDispatcher
    extends AbstractInvocationDispatcher
{
    protected Iterator dispatchOrder( List invokablesList ) {
        final ListIterator i = invokablesList.listIterator(this.invokables.size());
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
