/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.test.unit.internal;

import org.hamcrest.StringDescription;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.internal.ReturnDefaultValueAction;
import org.jmock.lib.JavaReflectionImposteriser;
import org.jmock.test.unit.support.MethodFactory;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;


public class ReturnDefaultValueActionTests {
    static final Object[] NO_ARG_VALUES = new Object[0];
    static final MethodFactory METHOD_FACTORY = new MethodFactory();

    private final ReturnDefaultValueAction action = new ReturnDefaultValueAction();

    @Test public void
    writesDescriptionToStringBuffer() {
      assertThat(StringDescription.toString(action), containsString("returns a default value"));
    }

    @Test public void
    returnsUsefulDefaultResultsForBasicTypes() throws Throwable {
        returnsValueForType(boolean.class, Boolean.FALSE);
        returnsValueForType(void.class, null);
        returnsValueForType(byte.class, (byte) 0);
        returnsValueForType(short.class, (short) 0);
        returnsValueForType(int.class, 0);
        returnsValueForType(long.class, 0L);
        returnsValueForType(char.class, '\0');
        returnsValueForType(float.class, 0.0F);
        returnsValueForType(double.class, 0.0);
        returnsValueForType(Boolean.class, Boolean.FALSE);
        returnsValueForType(Byte.class, (byte) 0);
        returnsValueForType(Short.class, (short) 0);
        returnsValueForType(Integer.class, 0);
        returnsValueForType(Long.class, 0L);
        returnsValueForType(Character.class, '\0');
        returnsValueForType(Float.class, 0.0F);
        returnsValueForType(Double.class, 0.0);
        returnsValueForType(String.class, "");
        assertNotNull("return value for Object return type",
                      action.invoke(invocationReturning(Object.class)));
    }


    @Test public void
    returnsEmptyArrayForAllArrayTypes() throws Throwable {
        final int[] defaultArrayForPrimitiveType = (int[])action.invoke(invocationReturning(int[].class));
        assertEquals("empty array", 0, defaultArrayForPrimitiveType.length);

        Object[] defaultArrayForReferenceType =
                (Object[])action.invoke(invocationReturning(Object[].class));
        assertEquals("should be empty array", 0, defaultArrayForReferenceType.length);
    }

    // Inspired by http://www.c2.com/cgi/wiki?JavaNullProxy
    @Test public void
    imposteriserThatCanImposteriseReturnTypeReturnsNewMockObjectWithSameReturnDefaultValueAction() throws Throwable {
      final int intResult = -1;
      final Imposteriser imposteriser = new JavaReflectionImposteriser() {
            @Override
            public boolean canImposterise(Class<?> c) {
                return c == ReturnsAnInt.class;
            }
        };

      final ReturnDefaultValueAction imposterised = new ReturnDefaultValueAction(imposteriser);
      imposterised.addResult(int.class, intResult);
        
      final ReturnsAnInt result = (ReturnsAnInt)imposterised.invoke(invocationReturning(ReturnsAnInt.class));
      assertEquals(intResult, result.returnInt());
        
      assertNull("imposteriser cannot imposterise a Runnable",
          imposterised.invoke(invocationReturning(Runnable.class)));
    }

    @Test public void
    defaultResultsCanBeExplicitlyOverriddenByType() throws Throwable {
        final int newDefaultIntResult = 20;
        final String newDefaultStringResult = "hello";

        action.addResult(String.class, newDefaultStringResult);
        action.addResult(int.class, newDefaultIntResult);

        assertEquals("registered value for string result type",
                     newDefaultStringResult, action.invoke(invocationReturning(String.class)));
        assertEquals("registered value for int result type",
                     newDefaultIntResult, action.invoke(invocationReturning(int.class)));
    }

    @Test public void
    anExplicitlyRegisteredResultOverridesThePreviousResultForTheSameType() throws Throwable {
        action.addResult(String.class, "result1");
        action.addResult(String.class, "result2");

        assertEquals("result2", action.invoke(invocationReturning(String.class)));
    }

    @Test public void
    invocationWithAnUnsupportedReturnTypeReturnsNull() throws Throwable {
      assertNull(action.invoke(invocationReturning(UnsupportedReturnType.class)));
    }
    
    private void returnsValueForType(Class<?> resultType, Object resultValue) throws Throwable {
        assertEquals("for type: " + resultType.getSimpleName(),
                     resultValue, action.invoke(invocationReturning(resultType)));
    }

    public static Invocation invocationReturning(Class<?> resultType) {
        return new Invocation("INVOKED-OBJECT",
                              METHOD_FACTORY.newMethodReturning(resultType),
                              NO_ARG_VALUES);
    }

    private static class UnsupportedReturnType {
    }
    private static  interface ReturnsAnInt {
      int returnInt();
    }
}
