/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;

import junit.framework.TestCase;

import org.jmock.core.CoreMock;
import org.jmock.core.Invocation;
import org.jmock.expectation.AssertMo;
import test.jmock.core.testsupport.MethodFactory;


public class InvocationTest extends TestCase {
    
    final Object INVOKED = new Object() { public String toString(){ return "INVOKED"; } };
    final String METHOD_NAME = "methodName";
	final Class[] ARG_TYPES = new Class[]{ int.class, boolean.class };
	final Class RETURN_TYPE = String.class;
    final Object[] ARG_VALUES = {new Integer(0), Boolean.TRUE};
	final Class[] EXCEPTION_TYPES = new Class[]{ InterruptedException.class, SecurityException.class };

	MethodFactory methodFactory;
	Method method;

    public InvocationTest(String name) {
        super(name);
    }

	public void setUp() throws Exception {
		methodFactory = new MethodFactory();
		method = methodFactory.newMethod( METHOD_NAME, ARG_TYPES, RETURN_TYPE, EXCEPTION_TYPES );
	}

    public void testCanBeConstructedFromAMethodObject() throws Exception {
        Invocation call = new Invocation( INVOKED, method, ARG_VALUES);

        assertSame( "invoked object", INVOKED, call.getInvokedObject() );
        assertEquals("name", method.getName(), call.getMethodName());
        assertEquals("parameter types",
                Arrays.asList(method.getParameterTypes()),
                call.getParameterTypes());
        assertEquals("return type",
                method.getReturnType(), call.getReturnType());
        assertEquals("argument values",
                Arrays.asList(ARG_VALUES), call.getParameterValues());
    }

    public void testConstructorInterpretsNullParameterValueArrayAsZeroArguments() {
        Invocation invocation = new Invocation( INVOKED, method, null);

        assertEquals( "expected no parameters values",
                      0, invocation.getParameterValues().size());
    }

    public void testTestsForEqualityOnTargetAndMethodSignatureAndArguments() {
        Invocation call1 = new Invocation(INVOKED, method, ARG_VALUES);
        Invocation call2 = new Invocation(INVOKED, method, ARG_VALUES);
        Invocation differentTarget = new Invocation("OTHER TARGET", method, ARG_VALUES);

	    Invocation differentMethod = new Invocation(
	            INVOKED,
	            methodFactory.newMethod( "OTHER_"+METHOD_NAME, ARG_TYPES, RETURN_TYPE, EXCEPTION_TYPES ),
	            ARG_VALUES);
        Invocation differentArgValues = new Invocation(INVOKED,method,
                new Object[]{new Integer(1), Boolean.FALSE});

        assertTrue("should be equal to itself", call1.equals(call1));
        assertTrue("identical calls should be equal", call1.equals(call2));

        assertFalse("should not be equal to object that is not an Invocation",
            call1.equals(new Object()));
        assertFalse("should not be equal to null", 
            call1.equals(null));
        assertFalse("should not be equal if different invoked object",
            call1.equals(differentTarget));
        assertFalse("should not be equal if different method",
            call1.equals(differentMethod));
        assertFalse("should not be equal if different argumentValues",
            call1.equals(differentArgValues));
    }

    public void testFollowsEqualsHashcodeProtocol() {
        Invocation call1 = new Invocation(INVOKED, method, ARG_VALUES);
        Invocation call2 = new Invocation(INVOKED, method, ARG_VALUES);

        assertEquals("should have equal hash codes",
                     call1.hashCode(), call2.hashCode());
    }

    public void testToStringWithTwoArguments() throws Exception {
        Invocation invocation = new Invocation(
            INVOKED,
            methodFactory.newMethod( METHOD_NAME, new Class[]{String.class, String.class}, void.class,
                                     EXCEPTION_TYPES ),
            new Object[]{"arg1", "arg2"} );
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", METHOD_NAME, result);
        AssertMo.assertIncludes("Should contain firstArg", "arg1", result);
        AssertMo.assertIncludes("Should contain second Arg", "arg2", result);
    }

    public void testToStringWithStringArray() throws Exception {
        Invocation invocation = new Invocation(
            INVOKED,
            methodFactory.newMethod( METHOD_NAME, new Class[]{String[].class}, void.class, EXCEPTION_TYPES ),
            new Object[]{new String[]{"arg1", "arg2"}} );
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", METHOD_NAME, result);
        AssertMo.assertIncludes("Should contain args as an array", "[<arg1>, <arg2>]", result);
    }

    public void testToStringWithPrimitiveArray() throws Exception {
        Invocation invocation = new Invocation(
            INVOKED,
            methodFactory.newMethod( METHOD_NAME, new Class[]{long[].class}, void.class, EXCEPTION_TYPES ),
            new Object[]{new long[]{1, 2}} );
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", METHOD_NAME, result);
        AssertMo.assertIncludes("Should contain args as an array", "[<1>, <2>]", result);
    }

    public void testMethodToStringWithNullArg() throws Exception {
        Invocation invocation = new Invocation(
            INVOKED, methodFactory.newMethod(METHOD_NAME, new Class[]{String.class}, void.class, EXCEPTION_TYPES ),
            new Object[]{null});
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", METHOD_NAME, result);
        AssertMo.assertIncludes("Should contain firstArg", "<null>", result);
    }

	public void testMethodToStringWithObjectArg() throws Exception {
		final String argAsString = "TO_STRING_RESULT";
		Object arg = new Object() {
			public String toString() { return argAsString; }
		};

	    Invocation invocation = new Invocation(
	        INVOKED, methodFactory.newMethod(METHOD_NAME, new Class[]{String.class}, void.class, EXCEPTION_TYPES ),
	        new Object[]{arg});
	    String result = invocation.toString();

	    AssertMo.assertIncludes("Should contain method name", METHOD_NAME, result);
	    AssertMo.assertIncludes("Should contain firstArg", argAsString, result);
	}
}
