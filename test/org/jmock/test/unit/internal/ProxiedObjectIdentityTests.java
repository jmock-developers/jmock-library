package org.jmock.test.unit.internal;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.jmock.api.Invocation;
import org.jmock.internal.FakeObjectMethods;
import org.jmock.internal.ProxiedObjectIdentity;
import org.jmock.test.acceptance.MockedType;
import org.jmock.test.unit.support.StubInvokable;

public class ProxiedObjectIdentityTests extends TestCase {
    String name = "name";
    StubInvokable next = new StubInvokable();
    FakeObjectMethods id = new ProxiedObjectIdentity(next);

    Object invokedObject = "invokedObject";
    Object otherObject = "otherObject";

    public ProxiedObjectIdentityTests() {
        next.toStringResult = name;
    }
    
    public void testImplementsEqualsByComparingReferences() throws Throwable {
        Method equals = Object.class.getMethod("equals", Object.class);

        assertEquals("should equal same object", 
                     Boolean.TRUE,
                     id.invoke(new Invocation(invokedObject, equals, invokedObject)));

        assertEquals("should not equal another object", 
                     Boolean.FALSE,
                     id.invoke(new Invocation(invokedObject, equals, otherObject)));

        assertEquals("should not equal null", 
                     Boolean.FALSE,
                     id.invoke(new Invocation(invokedObject, equals, (Object)null)));
    }
    
    public void testImplementsHashCodeToReturnIdentityHashCode() throws Throwable {
        Method hashCode = Object.class.getMethod("hashCode");
        
        assertEquals(System.identityHashCode(invokedObject), id.invoke(new Invocation(invokedObject, hashCode)));
    }
    
    public void testDelegatesToStringToNextInvokable() throws Throwable {
        Method toString = Object.class.getMethod("toString");

        assertEquals("an Invocation of toString", next.toStringResult, id.invoke(new Invocation(invokedObject, toString)));
        assertEquals("directly invoked toString", next.toStringResult, id.toString());
    }

    public void testPassesOtherInvocationsToNextInvokable() throws Throwable {
        Method doSomething = MockedType.class.getMethod("doSomething");

        id.invoke(new Invocation(invokedObject, doSomething));
        
        assertTrue("should have invoked next", next.wasInvoked);
    }
    
    public static class ClassOverridingToString {
        @Override
        public String toString() {
            return "a different toString";
        }
    }
    
    public void testPerformsObjectMethodsEvenWhenTheyAreOverridden() throws Throwable {
        Method overriddenToString = ClassOverridingToString.class.getMethod("toString");
        
        assertEquals("an Invocation of overridden toString", 
                     next.toStringResult, id.invoke(new Invocation(invokedObject, overriddenToString)));
    }
}
