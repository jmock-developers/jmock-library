/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.timedcache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.AssertionFailedError;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;


public class StubsSequence
        implements Stub
{
    List stubs;
    Iterator iterator;

    static public List asList( Stub stub1, Stub stub2 ) {
        ArrayList list = new ArrayList();
        list.add(stub1);
        list.add(stub2);
        return list;
    }

    static public List asList( Stub stub1, Stub stub2, Stub stub3 ) {
        ArrayList list = new ArrayList();
        list.add(stub1);
        list.add(stub2);
        list.add(stub3);
        return list;
    }

    public StubsSequence( List stubs ) {
        this.stubs = stubs;
        iterator = this.stubs.iterator();
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        if (iterator.hasNext()) {
            return ((Stub)iterator.next()).invoke(invocation);
        }
        throw new AssertionFailedError("No more stubs available.");
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        Iterator sequence = stubs.iterator();
        String filler = "";
        while (sequence.hasNext()) {
            buffer.append(filler);
            ((Stub)sequence.next()).describeTo(buffer);
            filler = ", then ";
        }
        return buffer;
    }

}
