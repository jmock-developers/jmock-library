/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.AssertionFailedError;
import org.jmock.core.stub.VoidStub;

/**
 * @since 1.0
 */
public class InvocationMocker
        implements Invokable, StubMatchersCollection
{
    public interface Describer
    {
        public boolean hasDescription();

        public void describeTo( StringBuffer buffer,
                                List matchers, Stub stub, String name );
    }

    private String name = null;
    private List matchers = new ArrayList();
    private Stub stub = VoidStub.INSTANCE;
    private Describer describer;


    public InvocationMocker() {
        this(DEFAULT_DESCRIBER);
    }

    public InvocationMocker( Describer describer ) {
        this.describer = describer;
    }

    public boolean matches( Invocation invocation ) {
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            if (!((InvocationMatcher)i.next()).matches(invocation)) {
                return false;
            }
        }
        return true;
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            ((InvocationMatcher)i.next()).invoked(invocation);
        }
        return stub.invoke(invocation);
    }

    public void verify() {
        try {
            Iterator i = matchers.iterator();
            while (i.hasNext()) {
                ((InvocationMatcher)i.next()).verify();
            }
        }
        catch (AssertionFailedError error) {
            AssertionFailedError newError = new AssertionFailedError(error.getMessage() + "\n" + toString());
            newError.fillInStackTrace();
            throw newError;
        }
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void addMatcher( InvocationMatcher matcher ) {
        matchers.add(matcher);
    }

    public void setStub( Stub stub ) {
        this.stub = stub;
    }

    public String toString() {
        return describeTo(new StringBuffer()).toString();
    }

    public boolean hasDescription() {
        return describer.hasDescription();
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        describer.describeTo(buffer,
                             Collections.unmodifiableList(matchers),
                             stub,
                             name);

        return buffer;
    }

    public static final Describer DEFAULT_DESCRIBER = new Describer()
    {
        public boolean hasDescription() {
            return true;
        }

        public void describeTo( StringBuffer buffer,
                                List matchers, Stub stub, String name ) {
            Iterator it = matchers.iterator();
            while (it.hasNext()) {
                InvocationMatcher matcher = (InvocationMatcher)it.next();

                if (matcher.hasDescription()) {
                    matcher.describeTo(buffer).append(", ");
                }
            }

            stub.describeTo(buffer);

            if (name != null) {
                buffer.append(" [").append(name).append("]");
            }
        }
    };
}
