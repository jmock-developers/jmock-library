/*
 * Created on 15-Jun-2004
 */
package org.jmock.core.internal;

import java.util.List;

import org.jmock.core.*;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.matcher.NoArgumentsMatcher;
import org.jmock.core.stub.CustomStub;
import org.jmock.core.stub.ReturnStub;


public class HiddenInvocationMocker extends InvocationMocker {
	public HiddenInvocationMocker(String methodName, InvocationMatcher arguments, Stub stub) {
		super(NO_DESCRIPTION);
        addMatcher(new MethodNameMatcher(methodName));
        addMatcher(arguments);
        setStub(stub);
    }

	public static class ToString extends HiddenInvocationMocker {
		public ToString(String name) {
			super("toString", NoArgumentsMatcher.INSTANCE, new ReturnStub(name));
        }
    }
    
    public static class Equals extends HiddenInvocationMocker {
    	public Equals(Object proxy) {
    		super("equals", new ArgumentsMatcher(new Constraint[] {new IsAnything()}), new IsSameAsProxyStub(proxy));
        }
    }
    
    public static class HashCode extends HiddenInvocationMocker {
    	public HashCode() {
    		super("hashCode", NoArgumentsMatcher.INSTANCE, new HashCodeStub());
        }
    }

    private static final InvocationMocker.Describer NO_DESCRIPTION =
        new InvocationMocker.Describer()
        {
            public boolean hasDescription() {
                return false;
            }

            public void describeTo( StringBuffer buffer, List matchers, Stub stub, String name ) {
            }
        };

    private static class IsSameAsProxyStub extends CustomStub
    {
        private Object proxy;
        
        private IsSameAsProxyStub(Object proxy) {
            super("returns whether equal to proxy");
            this.proxy = proxy;
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return new Boolean(invocation.parameterValues.get(0) == proxy);
        }
    }

    private static class HashCodeStub extends CustomStub
    {
        private HashCodeStub() {
            super("returns hashCode for proxy");
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return new Integer(invocation.invokedObject.hashCode());
        }
    }
}
