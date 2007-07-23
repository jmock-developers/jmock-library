/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.test.unit.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.TestCase;


public class MethodFactoryTests extends TestCase
{
    private class ReturnType
    {
    }

    static final String METHOD_NAME = "METHOD_NAME";
    static final Class<?>[] ARG_TYPES = new Class[]{int.class, Object.class, double[].class, String[].class};
    static final Class<?> RETURN_TYPE = ReturnType.class;
    static final Class<?>[] EXCEPTION_TYPES = new Class[]{InterruptedException.class};

    MethodFactory factory;


    @Override
    public void setUp() {
        factory = new MethodFactory();
    }

    public void testCreatesMethodInNewNamedClass() {
        Method method = factory.newMethod(METHOD_NAME, ARG_TYPES, RETURN_TYPE, EXCEPTION_TYPES);

        assertTrue("is public", Modifier.isPublic(method.getModifiers()));
        assertEquals("invokedMethod name", METHOD_NAME, method.getName());
        assertAllSame("arg types", ARG_TYPES, method.getParameterTypes());
        assertSame("return type", RETURN_TYPE, method.getReturnType());
        assertAllSame("exception types", EXCEPTION_TYPES, method.getExceptionTypes());
    }

    public void testCreatesMethodThatReturnsAType() {
        Method method = factory.newMethodReturning(RETURN_TYPE);

        assertSame("return type", RETURN_TYPE, method.getReturnType());
    }

    private void assertAllSame( String message, Class<?>[] expected, Class<?>[] actual ) {
        assertEquals(message + ": number of elements ", expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertSame(message + ": element " + i, expected[i], actual[i]);
        }
    }
}
