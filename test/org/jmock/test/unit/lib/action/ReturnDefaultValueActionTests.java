/*  Copyright (c) 2000-2006 jMock.org
 */
package org.jmock.test.unit.lib.action;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.api.Invocation;
import org.jmock.lib.action.ReturnDefaultValueAction;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.GetDescription;
import org.jmock.test.unit.support.MethodFactory;


public class ReturnDefaultValueActionTests extends TestCase {
    static final Object[] NO_ARG_VALUES = new Object[0];
    static final MethodFactory METHOD_FACTORY = new MethodFactory();

    private ReturnDefaultValueAction action;

    @Override public void setUp() {
        action = new ReturnDefaultValueAction();
    }

    public void testWritesDescritionToStringBuffer() {
        AssertThat.stringIncludes("contains expected description",
            "returns a default value",
            GetDescription.of(action));
    }

    public void testReturnsUsefulDefaultResultsForBasicTypes()
        throws Throwable
    {
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
        assertNotNull( "should return an object for Object return type",
                       action.invoke(invocationReturning(Object.class)) );
    }

    public void testReturnsEmptyArrayForAllArrayTypes()
            throws Throwable
    {
        int[] defaultArrayForPrimitiveType =
                (int[])action.invoke(invocationReturning(int[].class));
        assertEquals("should be empty array", 0, defaultArrayForPrimitiveType.length);

        Object[] defaultArrayForReferenceType =
                (Object[])action.invoke(invocationReturning(Object[].class));
        assertEquals("should be empty array", 0, defaultArrayForReferenceType.length);
    }

    public interface InterfaceType {
        int returnInt();
    }

    // Inspired by http://www.c2.com/cgi/wiki?JavaNullProxy
    public void testReturnsProxyOfNewMockObjectWithSameReturnDefaultValueActionForInterfaceTypes() throws Throwable {
        int intResult = -1;
        
        action.addResult(int.class, new Integer(intResult));

        InterfaceType result = (InterfaceType)action.invoke(invocationReturning(InterfaceType.class));

        assertEquals("int result from 'null' interface implementation",
                     intResult, result.returnInt());
    }

    public void testDefaultResultsCanBeExplicitlyOverriddenByType() throws Throwable {
        int newDefaultIntResult = 20;
        String newDefaultStringResult = "hello";

        action.addResult(String.class, newDefaultStringResult);
        action.addResult(int.class, new Integer(newDefaultIntResult));

        assertEquals("expected registered value for string result type",
                     newDefaultStringResult, action.invoke(invocationReturning(String.class)));

        assertEquals("expected registered value for int result type",
                     new Integer(newDefaultIntResult), action.invoke(invocationReturning(int.class)));
    }

    public void testAnExplicitlyRegisteredResultOverridesThePreviousResultForTheSameType() throws Throwable {
        action.addResult(String.class, "result1");
        action.addResult(String.class, "result2");

        assertEquals("expected second result",
                     "result2", action.invoke(invocationReturning(String.class)));
    }

    class UnsupportedReturnType
    {
    }

    public void testInvocationWithAnUnregisteredReturnTypeReturnsNull()
        throws Throwable
    {
        Class unsupportedReturnType = UnsupportedReturnType.class;

        Object result = action.invoke(invocationReturning(unsupportedReturnType));
        
        assertNull("should have returned null", result);
    }

    public void assertHasRegisteredValue( ReturnDefaultValueAction action,
                                          Class resultType,
                                          Object resultValue )
            throws Throwable
    {
        assertEquals("expected " + resultValue + " to be returned",
                     resultValue, action.invoke(invocationReturning(resultType)));
    }

    public void assertHasNotRegisteredReturnType( ReturnDefaultValueAction action,
                                                  Class resultType )
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

    private Invocation invocationReturning( Class resultType ) {
        return new Invocation("INVOKED-OBJECT",
                              METHOD_FACTORY.newMethodReturning(resultType),
                              NO_ARG_VALUES);
    }
}
