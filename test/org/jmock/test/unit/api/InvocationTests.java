/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.test.unit.api;

import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;

import org.jmock.api.Invocation;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.MethodFactory;


public class InvocationTests extends TestCase {
    final Object INVOKED = new Object() {
        @Override public String toString() { return "INVOKED"; }
    };
    final String METHOD_NAME = "methodName";
    final Class<?>[] ARG_TYPES = new Class[]{int.class, boolean.class};
    final Class<?> RETURN_TYPE = String.class;
    final Object[] ARG_VALUES = {new Integer(0), Boolean.TRUE};
    final Class<?>[] EXCEPTION_TYPES = new Class[]{InterruptedException.class, SecurityException.class};

    MethodFactory methodFactory;
    Method method;

    public InvocationTests( String name ) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        methodFactory = new MethodFactory();
        method = methodFactory.newMethod(METHOD_NAME, ARG_TYPES, RETURN_TYPE, EXCEPTION_TYPES);
    }

    public void testCanBeConstructedFromAMethodObject() throws Exception {
        Invocation invocation = new Invocation(INVOKED, method, ARG_VALUES);

        assertSame("invoked object", INVOKED, invocation.getInvokedObject());
        assertEquals("invoked method", method, invocation.getInvokedMethod());
        assertEquals("name", method.getName(), invocation.getInvokedMethod().getName());
        assertEquals("parameter types",
                     Arrays.asList(method.getParameterTypes()),
                     Arrays.asList(invocation.getInvokedMethod().getParameterTypes()));
        assertEquals("return type",
                     method.getReturnType(), invocation.getInvokedMethod().getReturnType());
        assertEquals("parameter count", ARG_VALUES.length, invocation.getParameterCount());
        assertEquals("parameter values",
                     Arrays.asList(ARG_VALUES), Arrays.asList(invocation.getParametersAsArray()));
    }

    public void testConstructorInterpretsNullParameterValueArrayAsZeroArguments() {
        Invocation invocation = new Invocation(INVOKED, method);

        assertEquals("expected no parameters values",
                     0, invocation.getParameterCount());
    }

    public void testTestsForEqualityOnTargetAndMethodSignatureAndArguments() {
        Invocation invocation1 = new Invocation(INVOKED, method, ARG_VALUES);
        Invocation invocation2 = new Invocation(INVOKED, method, ARG_VALUES);
        Invocation differentTarget = new Invocation("OTHER TARGET", method, ARG_VALUES);

        Invocation differentMethod = new Invocation(INVOKED,
                                                    methodFactory.newMethod("OTHER_" + METHOD_NAME, ARG_TYPES, RETURN_TYPE, EXCEPTION_TYPES),
                                                    ARG_VALUES);
        Invocation differentArgValues = new Invocation(INVOKED, method,
                                                       new Object[]{new Integer(1), Boolean.FALSE});

        assertTrue("should be equal to itself", invocation1.equals(invocation1));
        assertTrue("identical calls should be equal", invocation1.equals(invocation2));

        assertFalse("should not be equal to object that is not an Invocation",
                    invocation1.equals(new Object()));
        assertFalse("should not be equal to null",
                    invocation1.equals(null));
        assertFalse("should not be equal if different invoked object",
                    invocation1.equals(differentTarget));
        assertFalse("should not be equal if different method",
                    invocation1.equals(differentMethod));
        assertFalse("should not be equal if different argumentValues",
                    invocation1.equals(differentArgValues));
    }

    public void testFollowsEqualsHashcodeProtocol() {
        Invocation invocation1 = new Invocation(INVOKED, method, ARG_VALUES);
        Invocation invocation2 = new Invocation(INVOKED, method, ARG_VALUES);

        assertEquals("should have equal hash codes",
                     invocation1.hashCode(), invocation2.hashCode());
    }

    public void testToStringWithTwoArguments() throws Exception {
        Invocation invocation = new Invocation(INVOKED,
                                               methodFactory.newMethod(METHOD_NAME, new Class[]{String.class, String.class}, void.class,
                                                                       EXCEPTION_TYPES),
                                               new Object[]{"arg1", "arg2"});
        String result = invocation.toString();

        AssertThat.stringIncludes("Should contain object name", INVOKED.toString(), result);
        AssertThat.stringIncludes("Should contain method name", METHOD_NAME, result);
        AssertThat.stringIncludes("Should contain firstArg", "arg1", result);
        AssertThat.stringIncludes("Should contain second Arg", "arg2", result);
    }

    public void testToStringWithStringArray() throws Exception {
        Invocation invocation = new Invocation(INVOKED,
                                               methodFactory.newMethod(METHOD_NAME, new Class[]{String[].class}, void.class, EXCEPTION_TYPES),
                                               new Object[]{new String[]{"arg1", "arg2"}});
        String result = invocation.toString();

        AssertThat.stringIncludes("Should contain method name", METHOD_NAME, result);
        AssertThat.stringIncludes("Should contain args as an array", "[\"arg1\", \"arg2\"]", result);
    }

    public void testToStringWithPrimitiveArray() throws Exception {
        Invocation invocation = new Invocation(INVOKED,
                                               methodFactory.newMethod(METHOD_NAME, new Class[]{long[].class}, void.class, EXCEPTION_TYPES),
                                               new Object[]{new long[]{1, 2}});
        String result = invocation.toString();

        AssertThat.stringIncludes("Should contain invokedMethod name", METHOD_NAME, result);
        AssertThat.stringIncludes("Should contain args as an array", "[<1L>, <2L>]", result);
    }

    public void testMethodToStringWithNullArg() throws Exception {
        Invocation invocation = new Invocation(INVOKED, methodFactory.newMethod(METHOD_NAME, new Class[]{String.class}, void.class, EXCEPTION_TYPES),
                                               new Object[]{null});
        String result = invocation.toString();

        AssertThat.stringIncludes("Should contain invokedMethod name", METHOD_NAME, result);
        AssertThat.stringIncludes("Should contain firstArg", "null", result);
    }

    public void testMethodToStringWithObjectArg() throws Exception {
        final String argAsString = "TO_STRING_RESULT";
        Object arg = new Object()
        {
            @Override public String toString() {
                return argAsString;
            }
        };

        Invocation invocation = new Invocation(INVOKED, methodFactory.newMethod(METHOD_NAME, new Class[]{String.class}, void.class, EXCEPTION_TYPES),
                                               new Object[]{arg});
        String result = invocation.toString();

        AssertThat.stringIncludes("Should contain invokedMethod name", METHOD_NAME, result);
        AssertThat.stringIncludes("Should contain firstArg", argAsString, result);
    }

    public void testReturnTypeCheckFailsIfReturningValueFromVoidMethod() {
    	Invocation invocation = 
            new Invocation(INVOKED, methodFactory.newMethodReturning(void.class));
        
        try {
            invocation.checkReturnTypeCompatibility("string result");
        }
        catch (IllegalStateException ex) {
            AssertThat.stringIncludes("should describe error",
                                    "tried to return a value from a void method", ex.getMessage());
            return;
        }
        fail("should have failed");
    }
    
    public void testReturnTypeCheckFailsIfReturnedValueIsIncompatible() {
    	Invocation invocation = 
            new Invocation(INVOKED, methodFactory.newMethodReturning(int.class));

        try {
            invocation.checkReturnTypeCompatibility("string result");
        }
        catch (IllegalStateException ex) {
            AssertThat.stringIncludes("expected return type", int.class.toString(), ex.getMessage());
            AssertThat.stringIncludes("returned value type", String.class.getName(), ex.getMessage());
            return;
        }
        fail("should have failed");
    }

    public void testReturnTypeCheckFailsWhenReturningNullFromMethodWithPrimitiveReturnType() {
    	Invocation invocation = 
            new Invocation(INVOKED, methodFactory.newMethodReturning(int.class));
    
        try {
            invocation.checkReturnTypeCompatibility(null);
        }
        catch (IllegalStateException ex) {
            AssertThat.stringIncludes("expected return type", int.class.toString(), ex.getMessage());
            AssertThat.stringIncludes("null", String.valueOf((Object)null), ex.getMessage());
            return;
        }
        fail("should have failed");
    }

    public void testReturnTypeCheckAllowsReturningBoxedTypeFromMethodWithPrimitiveReturnType() {
        Invocation invocation = 
            new Invocation(INVOKED, methodFactory.newMethodReturning(int.class));
    
        invocation.checkReturnTypeCompatibility(new Integer(0));
    }

    public void testReturnTypeCheckAllowsReturningNullFromMethodWithNonPrimitiveReturnType() {
        Invocation invocation = 
            new Invocation(INVOKED, methodFactory.newMethodReturning(String.class));
        
        invocation.checkReturnTypeCompatibility(null);
    }
    
    public void testReturnTypeCheckAllowsReturningNullFromVoidMethod() {
        Invocation invocation = 
            new Invocation(INVOKED, methodFactory.newMethodReturning(void.class));
        
        invocation.checkReturnTypeCompatibility(null);
    }
    
    public interface TargetInterface {
        public String doSomething(String arg) throws TargetException;
    }
    
    public static class TargetException extends Exception {}
    
    public static class Target implements TargetInterface {
        public String receivedArg = null;
        public String result = null;
        public TargetException exception = null;
        
        public String doSomething(String arg) throws TargetException {
            receivedArg = arg;
            if (exception != null) {
                throw exception;
            }
            else {
                return result;
            }
        }
    }
    
    public void testCanApplyInvocationToAnotherObject() throws Throwable {
        Target target = new Target();
        target.result = "result";
        
        Invocation invocation =
            new Invocation("receiver",
                           TargetInterface.class.getMethod("doSomething", String.class),
                           new Object[]{"arg"});
        
        String actualResult = (String)invocation.applyTo(target);
        
        assertEquals("received argument", "arg", target.receivedArg);
        assertEquals("result returned from apply", target.result, actualResult);
    }
    
    public void testUnwrapsInvocationTargetExceptionsFromAppliedInvocation() throws Throwable {
        Target target = new Target();
        target.exception = new TargetException();
        
        Invocation invocation =
            new Invocation("receiver",
                           TargetInterface.class.getMethod("doSomething", String.class),
                           new Object[]{"arg"});
        
        try {
            invocation.applyTo(target);
            fail("should have thrown TargetException");
        }
        catch (TargetException ex) {
            // expected
        }
    }
}
