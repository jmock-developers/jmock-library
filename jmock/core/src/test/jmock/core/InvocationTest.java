/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.expectation.AssertMo;

import test.jmock.core.testsupport.MethodFactory;


public class InvocationTest extends TestCase
{

	final Object INVOKED = new Object()
	{
		public String toString() {
			return "INVOKED";
		}
	};
	final String METHOD_NAME = "methodName";
	final Class[] ARG_TYPES = new Class[]{int.class, boolean.class};
	final Class RETURN_TYPE = String.class;
	final Object[] ARG_VALUES = {new Integer(0), Boolean.TRUE};
	final Class[] EXCEPTION_TYPES = new Class[]{InterruptedException.class, SecurityException.class};

	MethodFactory methodFactory;
	Method method;

	public InvocationTest( String name ) {
		super(name);
	}

	public void setUp() throws Exception {
		methodFactory = new MethodFactory();
		method = methodFactory.newMethod(METHOD_NAME, ARG_TYPES, RETURN_TYPE, EXCEPTION_TYPES);
	}

	public void testCanBeConstructedFromAMethodObject() throws Exception {
		Invocation invocation = new Invocation(INVOKED, method, ARG_VALUES);

		assertSame("invokedObject object", INVOKED, invocation.invokedObject);
		assertEquals("invokedObject invokedMethod", method, invocation.invokedMethod);
		assertEquals("name", method.getName(), invocation.invokedMethod.getName());
		assertEquals("parameter types",
		             Arrays.asList(method.getParameterTypes()),
		             Arrays.asList(invocation.invokedMethod.getParameterTypes()));
		assertEquals("return type",
		             method.getReturnType(), invocation.invokedMethod.getReturnType());
		assertEquals("argument values",
		             Arrays.asList(ARG_VALUES), invocation.parameterValues);
	}

	public void testConstructorInterpretsNullParameterValueArrayAsZeroArguments() {
		Invocation invocation = new Invocation(INVOKED, method, null);

		assertEquals("expected no parameters values",
		             0, invocation.parameterValues.size());
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
		assertFalse("should not be equal if different invokedObject object",
		            invocation1.equals(differentTarget));
		assertFalse("should not be equal if different invokedMethod",
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

		AssertMo.assertIncludes("Should contain invokedMethod name", METHOD_NAME, result);
		AssertMo.assertIncludes("Should contain firstArg", "arg1", result);
		AssertMo.assertIncludes("Should contain second Arg", "arg2", result);
	}

	public void testToStringWithStringArray() throws Exception {
		Invocation invocation = new Invocation(INVOKED,
		                                       methodFactory.newMethod(METHOD_NAME, new Class[]{String[].class}, void.class, EXCEPTION_TYPES),
		                                       new Object[]{new String[]{"arg1", "arg2"}});
		String result = invocation.toString();

		AssertMo.assertIncludes("Should contain invokedMethod name", METHOD_NAME, result);
		AssertMo.assertIncludes("Should contain args as an array", "[<arg1>, <arg2>]", result);
	}

	public void testToStringWithPrimitiveArray() throws Exception {
		Invocation invocation = new Invocation(INVOKED,
		                                       methodFactory.newMethod(METHOD_NAME, new Class[]{long[].class}, void.class, EXCEPTION_TYPES),
		                                       new Object[]{new long[]{1, 2}});
		String result = invocation.toString();

		AssertMo.assertIncludes("Should contain invokedMethod name", METHOD_NAME, result);
		AssertMo.assertIncludes("Should contain args as an array", "[<1>, <2>]", result);
	}

	public void testMethodToStringWithNullArg() throws Exception {
		Invocation invocation = new Invocation(INVOKED, methodFactory.newMethod(METHOD_NAME, new Class[]{String.class}, void.class, EXCEPTION_TYPES),
		                                       new Object[]{null});
		String result = invocation.toString();

		AssertMo.assertIncludes("Should contain invokedMethod name", METHOD_NAME, result);
		AssertMo.assertIncludes("Should contain firstArg", "<null>", result);
	}

	public void testMethodToStringWithObjectArg() throws Exception {
		final String argAsString = "TO_STRING_RESULT";
		Object arg = new Object() { public String toString() { return argAsString; } };

		Invocation invocation = new Invocation(INVOKED, methodFactory.newMethod(METHOD_NAME, new Class[]{String.class}, void.class, EXCEPTION_TYPES),
		                                       new Object[]{arg});
		String result = invocation.toString();

		AssertMo.assertIncludes("Should contain invokedMethod name", METHOD_NAME, result);
		AssertMo.assertIncludes("Should contain firstArg", argAsString, result);
	}
}
