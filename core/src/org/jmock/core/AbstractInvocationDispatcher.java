package org.jmock.core;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.jmock.core.stub.TestFailureStub;


public abstract class AbstractInvocationDispatcher implements InvocationDispatcher
{
    public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
    
    protected List invokables = new ArrayList();
    protected Stub defaultStub = new TestFailureStub("unexpected invocation");

    public void setDefaultStub( Stub defaultStub ) {
        this.defaultStub = defaultStub;
    }

    public void add( Invokable invokable ) {
        invokables.add(invokable);
    }

    public void verify() {
        Iterator i = invokables.iterator();
        while (i.hasNext()) {
            ((Verifiable)i.next()).verify();
        }
    }

    public void clear() {
        invokables.clear();
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        if (anyInvokableHasDescription()) {
            writeInvokablesTo(buffer);
        } else {
            buffer.append(NO_EXPECTATIONS_MESSAGE);
        }

        return buffer;
    }

    private void writeInvokablesTo( StringBuffer buffer ) {
        Iterator iterator = invokables.iterator();
        while (iterator.hasNext()) {
            Invokable invokable = (Invokable)iterator.next();
            if (invokable.hasDescription()) {
                invokable.describeTo(buffer).append("\n");
            }
        }
    }

    private boolean anyInvokableHasDescription() {
        Iterator iterator = invokables.iterator();
        while (iterator.hasNext()) {
            if (((Invokable)iterator.next()).hasDescription()) return true;
        }
        return false;
    }

    public Object dispatch( Invocation invocation ) throws Throwable {
        Iterator i = dispatchOrder(invokables);
        while (i.hasNext()) {
            Invokable invokable = (Invokable)i.next();
            if (invokable.matches(invocation)) {
                return invokable.invoke(invocation);
            }
        }

        return defaultStub.invoke(invocation);
    }

    protected abstract Iterator dispatchOrder( List invokablesList );
}
