/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.framework;

import junit.framework.TestCase;

import org.jmock.dynamock.Mock;
import org.jmock.dynamic.framework.Invocation;
import org.jmock.expectation.AssertMo;

import java.lang.reflect.Method;
import java.util.Arrays;

public class InvocationTest
        extends TestCase {
    public String exampleMethod(int number, boolean flag) {
        return "hello, world";
    }

    final Class DECLARING_CLASS = Void.class;
    final String METHOD_NAME = "exampleMethod";
    final Class[] ARG_TYPES = {int.class, boolean.class};
    final Class RETURN_TYPE = String.class;
    final Object[] ARG_VALUES = {new Integer(0), Boolean.TRUE};


    public InvocationTest(String name) {
        super(name);
    }

    public void testCanBeConstructedWithExplicitCallDetails() {
        Invocation call = new Invocation(DECLARING_CLASS, METHOD_NAME, ARG_TYPES,
                RETURN_TYPE, ARG_VALUES);

        assertEquals("name", METHOD_NAME, call.getMethodName());
        assertEquals("parameter types",
                Arrays.asList(ARG_TYPES), call.getParameterTypes());
        assertEquals("return type",
                RETURN_TYPE, call.getReturnType());
        assertEquals("argument values",
                Arrays.asList(ARG_VALUES), call.getParameterValues());
    }

    public void testCanBeConstructedFromAMethodObject() throws Exception {
        Method method = getClass().getMethod(METHOD_NAME, ARG_TYPES);

        Invocation call = new Invocation(method, ARG_VALUES);

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
        Invocation call = new Invocation(DECLARING_CLASS, METHOD_NAME, new Class[0],
                RETURN_TYPE, null);

        assertEquals("expected no parameters values",
                0, call.getParameterValues().size());
    }

    public void testTestsForEqualityOnMethodSignatureAndArguments() {
        Invocation call1 = new Invocation(DECLARING_CLASS,
                METHOD_NAME, ARG_TYPES, RETURN_TYPE,
                ARG_VALUES);
        Invocation call2 = new Invocation(DECLARING_CLASS,
                METHOD_NAME, ARG_TYPES, RETURN_TYPE,
                ARG_VALUES);
        Invocation differentName = new Invocation(DECLARING_CLASS,
                "other" + METHOD_NAME, ARG_TYPES, RETURN_TYPE,
                ARG_VALUES);
        Invocation differentReturnType = new Invocation(DECLARING_CLASS,
                "other" + METHOD_NAME, ARG_TYPES, int.class,
                ARG_VALUES);
        Invocation differentArgTypes = new Invocation(DECLARING_CLASS,
                "other" + METHOD_NAME, new Class[]{double.class}, RETURN_TYPE,
                ARG_VALUES);
        Invocation differentArgValues = new Invocation(DECLARING_CLASS,
                "other" + METHOD_NAME, ARG_TYPES, RETURN_TYPE,
                new Object[]{new Integer(1), Boolean.FALSE});

        assertTrue("should be equal to itself", call1.equals(call1));
        assertTrue("identical calls should be equal", call1.equals(call2));

        assertFalse("should not be equal to object that is not an ActiveCall",
                call1.equals(new Object()));
        assertFalse("should not be equal to null", call1.equals(null));
        assertFalse("should not be equal if different name",
                call1.equals(differentName));
        assertFalse("should not be equal if different parameter types",
                call1.equals(differentArgTypes));
        assertFalse("should not be equal if different return type",
                call1.equals(differentReturnType));
        assertFalse("should not be equal if different argumentValues",
                call1.equals(differentArgValues));
    }

    public void testFollowsEqualsHashcodeProtocol() {
        Invocation call1 = new Invocation(DECLARING_CLASS,
                METHOD_NAME, ARG_TYPES, RETURN_TYPE,
                ARG_VALUES);
        Invocation call2 = new Invocation(DECLARING_CLASS,
                METHOD_NAME, ARG_TYPES, RETURN_TYPE,
                ARG_VALUES);

        assertEquals("should have equal hash codes",
                call1.hashCode(), call2.hashCode());
    }

    public void testToStringWithTwoArguments() throws Exception {
        Invocation invocation =
                new Invocation(DECLARING_CLASS, "methodName", new Class[]{String.class, String.class}, void.class,
                        new Object[]{"arg1", "arg2"});
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", "methodName", result);
        AssertMo.assertIncludes("Should contain firstArg", "arg1", result);
        AssertMo.assertIncludes("Should contain second Arg", "arg2", result);
    }

    public void testToStringWithStringArray() throws Exception {
        Invocation invocation =
                new Invocation(DECLARING_CLASS, "methodName", new Class[]{String[].class}, void.class,
                        new Object[]{new String[]{"arg1", "arg2"}});
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", "methodName", result);
        AssertMo.assertIncludes("Should contain args as an array", "[<arg1>, <arg2>]", result);
    }

    public void testToStringWithPrimitiveArray() throws Exception {
        Invocation invocation =
                new Invocation(DECLARING_CLASS, "methodName", new Class[]{long[].class}, void.class,
                        new Object[]{new long[]{1, 2}});
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", "methodName", result);
        AssertMo.assertIncludes("Should contain args as an array", "[<1>, <2>]", result);
    }

    public void testMethodToStringWithProxyArg() throws Exception {
        Mock mockDummyInterface = new Mock(DummyInterface.class, "DummyMock");

        Invocation invocation =
                new Invocation(DECLARING_CLASS, "methodName", new Class[]{String.class, DummyInterface.class}, void.class,
                        new Object[]{"arg1", mockDummyInterface.proxy()});
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", "methodName", result);
        AssertMo.assertIncludes("Should contain firstArg", "arg1", result);
        AssertMo.assertIncludes("Should contain second Arg", "DummyMock", result);
    }

    public void testMethodToStringWithNullArg() throws Exception {
        Invocation invocation =
                new Invocation(DECLARING_CLASS, "methodName", new Class[]{String.class}, void.class,
                        new Object[]{null});
        String result = invocation.toString();

        AssertMo.assertIncludes("Should contain method name", "methodName", result);
        AssertMo.assertIncludes("Should contain firstArg", "<null>", result);
    }

}
