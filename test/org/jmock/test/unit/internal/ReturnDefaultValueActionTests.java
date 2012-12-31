/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.test.unit.internal;

import junit.framework.AssertionFailedError;
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

    @SuppressWarnings("UnnecessaryBoxing")
    @Test public void
    returnsUsefulDefaultResultsForBasicTypes() throws Throwable {
        assertHasRegisteredValue(action, boolean.class, Boolean.FALSE);
        assertHasRegisteredValue(action, void.class, null);
        assertHasRegisteredValue(action, byte.class, new Byte((byte)0));
        assertHasRegisteredValue(action, short.class, new Short((short)0));
        assertHasRegisteredValue(action, int.class, new Integer(0));
        assertHasRegisteredValue(action, long.class, new Long(0L));
        assertHasRegisteredValue(action, char.class, new Character('\0'));
        assertHasRegisteredValue(action, float.class, new Float(0.0F));
        assertHasRegisteredValue(action, double.class, new Double(0.0));
        assertHasRegisteredValue(action, Boolean.class, Boolean.FALSE);
        assertHasRegisteredValue(action, Byte.class, new Byte((byte)0));
        assertHasRegisteredValue(action, Short.class, new Short((short)0));
        assertHasRegisteredValue(action, Integer.class, new Integer(0));
        assertHasRegisteredValue(action, Long.class, new Long(0L));
        assertHasRegisteredValue(action, Character.class, new Character('\0'));
        assertHasRegisteredValue(action, Float.class, new Float(0.0F));
        assertHasRegisteredValue(action, Double.class, new Double(0.0));
        assertHasRegisteredValue(action, String.class, "");
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

    public interface InterfaceType {
        int returnInt();
    }

    // Inspired by http://www.c2.com/cgi/wiki?JavaNullProxy
    @Test public void
    imposteriserThatCanImposteriseReturnTypeReturnsNewMockObjectWithSameReturnDefaultValueAction() throws Throwable {
      final int intResult = -1;
      final Imposteriser imposteriser = new JavaReflectionImposteriser() {
            @Override
            public boolean canImposterise(Class<?> c) {
                return c == InterfaceType.class;
            }
        };

      final ReturnDefaultValueAction imposterised = new ReturnDefaultValueAction(imposteriser);
      imposterised.addResult(int.class, intResult);
        
      final InterfaceType result = (InterfaceType)imposterised.invoke(invocationReturning(InterfaceType.class));
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

    class UnsupportedReturnType
    {
    }

    @Test public void
    invocationWithAnUnsupportedReturnTypeReturnsNull() throws Throwable {
      assertNull(action.invoke(invocationReturning(UnsupportedReturnType.class)));
    }
    
    public void assertHasRegisteredValue(ReturnDefaultValueAction action,
                                         Class<?> resultType,
                                         Object resultValue ) throws Throwable {
        assertEquals("expected " + resultValue + " to be returned",
                     resultValue, action.invoke(invocationReturning(resultType)));
    }

    public void assertHasNotRegisteredReturnType( ReturnDefaultValueAction action,
                                                  Class<?> resultType )
            throws Throwable
    {
        try {
            action.invoke(invocationReturning(resultType));
            fail("action should not support return type " + resultType);
        }
        catch (AssertionFailedError expected) {
            return;
        }
    }

    private Invocation invocationReturning(Class<?> resultType) {
        return new Invocation("INVOKED-OBJECT",
                              METHOD_FACTORY.newMethodReturning(resultType),
                              NO_ARG_VALUES);
    }
}
