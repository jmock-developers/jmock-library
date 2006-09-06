package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.jmock.core.Invocation;
import org.jmock.core.Invokable;
import org.jmock.internal.InvocationDiverter;

public class InvokerTests extends TestCase {
    public interface TargetInterface {
        void doSomething();
    }
    
    public class Target implements TargetInterface {
        public boolean wasInvoked = false;
        
        public void doSomething() {
            wasInvoked = true;
        }
    }
    
    public interface OtherInterface {
        void doSomethingElse();
    }
    
    public class StubInvokable implements Invokable {
        public boolean wasInvoked = false;
        
        public Object invoke(Invocation invocation) throws Throwable {
            wasInvoked = true;
            return null;
        }
    }

    Target target = new Target();
    StubInvokable next = new StubInvokable();
    
    InvocationDiverter<TargetInterface> invoker = 
        new InvocationDiverter<TargetInterface>(TargetInterface.class, target, next);

    
    public void testAppliesInvocationToGivenObjectIfInvokedMethodDeclaredInGivenClass() throws Throwable {
        Invocation invocation = 
            new Invocation("invokedObject", 
                           TargetInterface.class.getMethod("doSomething"), 
                           Invocation.NO_PARAMETERS);
        
        invoker.invoke(invocation);
        
        assertTrue("target should have been invoked", 
                   target.wasInvoked);
        assertTrue("next should not have been invoked",
                   !next.wasInvoked);
    }
    
    public void testPassesInvocationToNextIfInvocationNotDeclaredInGivenClass() throws Throwable {
        Invocation invocation = 
            new Invocation("invokedObject", 
                           OtherInterface.class.getMethod("doSomethingElse"), 
                           Invocation.NO_PARAMETERS);
        
        invoker.invoke(invocation);
        
        assertTrue("target should not have been invoked", 
                   !target.wasInvoked);
        assertTrue("next should have been invoked",
                   next.wasInvoked);
    }
}
