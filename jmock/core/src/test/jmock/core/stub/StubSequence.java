package test.jmock.core.stub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import junit.framework.AssertionFailedError;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;


public class StubSequence
        implements Stub
{
    List stubs;
    Iterator iterator;


    public StubSequence( Stub[] stubs ) {
        this(Arrays.asList(stubs));
    }

    public StubSequence( List stubs ) {
        this.stubs = new ArrayList(stubs);
        this.iterator = this.stubs.iterator();
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        if (iterator.hasNext()) {
            return ((Stub)iterator.next()).invoke(invocation);
        } else {
            throw new AssertionFailedError("no more stubs available");
        }
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        for (int i = 0; i < stubs.size(); i++) {
            if (i > 0) buffer.append(", and then ");
            ((Stub)stubs.get(i)).describeTo(buffer);
        }

        return buffer;
    }
}
