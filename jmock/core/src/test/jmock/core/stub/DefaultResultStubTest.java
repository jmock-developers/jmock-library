/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.stub.DefaultResultStub;
import org.jmock.expectation.AssertMo;
import test.jmock.core.testsupport.MethodFactory;


public class DefaultResultStubTest
        extends TestCase
{
	static final Object[] NO_ARG_VALUES = new Object[0];

	private static MethodFactory METHOD_FACTORY = new MethodFactory();

	private DefaultResultStub stub;

	public DefaultResultStubTest( String name ) {
		super(name);
	}

	public void setUp() {
		stub = new DefaultResultStub();
	}

	public void testWritesDescritionToStringBuffer() {
		AssertMo.assertIncludes("contains expected description",
		                        "returns a default value",
		                        stub.describeTo(new StringBuffer()).toString());
	}

	public void testReturnsUsefulDefaultResultsForBasicTypes()
	        throws Throwable {
		assertHasRegisteredValue(stub, boolean.class, Boolean.FALSE);
		assertHasRegisteredValue(stub, void.class, null);
		assertHasRegisteredValue(stub, byte.class, new Byte((byte)0));
		assertHasRegisteredValue(stub, short.class, new Short((short)0));
		assertHasRegisteredValue(stub, int.class, new Integer(0));
		assertHasRegisteredValue(stub, long.class, new Long(0L));
		assertHasRegisteredValue(stub, char.class, new Character('\0'));
		assertHasRegisteredValue(stub, float.class, new Float(0.0F));
		assertHasRegisteredValue(stub, double.class, new Double(0.0));
		assertHasRegisteredValue(stub, Boolean.class, Boolean.FALSE);
		assertHasRegisteredValue(stub, Byte.class, new Byte((byte)0));
		assertHasRegisteredValue(stub, Short.class, new Short((short)0));
		assertHasRegisteredValue(stub, Integer.class, new Integer(0));
		assertHasRegisteredValue(stub, Long.class, new Long(0L));
		assertHasRegisteredValue(stub, Character.class, new Character('\0'));
		assertHasRegisteredValue(stub, Float.class, new Float(0.0F));
		assertHasRegisteredValue(stub, Double.class, new Double(0.0));
		assertHasRegisteredValue(stub, String.class, "");
	}

	private static class AnyType
	{
	}

	public void testReturnsEmptyArrayForAllArrayTypes()
	        throws Throwable {
		int[] defaultArrayForPrimitiveType =
		        (int[])stub.invoke(invocationReturning(int[].class));
		assertEquals("should be empty array", 0, defaultArrayForPrimitiveType.length);

		AnyType[] defaultArrayForAnyType =
		        (AnyType[])stub.invoke(invocationReturning(AnyType[].class));
		assertEquals("should be empty array", 0, defaultArrayForAnyType.length);
	}

	public interface InterfaceType
	{
		int returnInt();
	}

	// Inspired by http://www.c2.com/cgi/wiki?JavaNullProxy
	public void testReturnsProxyOfNewMockObjectWithSameDefaultResultStubForInterfaceTypes()
	        throws Throwable {
		int intResult = -1;

		stub.addResult(int.class, new Integer(intResult));

		InterfaceType result = (InterfaceType)stub.invoke(invocationReturning(InterfaceType.class));

		assertEquals("int result from 'null' interface implementation",
		             intResult, result.returnInt());
	}

	public void testDefaultResultsCanBeExplicitlyOverriddenByType()
	        throws Throwable {
		int newDefaultIntResult = 20;
		String newDefaultStringResult = "hello";

		stub.addResult(String.class, newDefaultStringResult);
		stub.addResult(int.class, new Integer(newDefaultIntResult));

		assertEquals("expected registered value for string result type",
		             newDefaultStringResult, stub.invoke(invocationReturning(String.class)));

		assertEquals("expected registered value for int result type",
		             new Integer(newDefaultIntResult), stub.invoke(invocationReturning(int.class)));
	}

	public void testAnExplicitlyRegisteredResultOverridesThePreviousResultForTheSameType()
	        throws Throwable {
		stub.addResult(String.class, "result1");
		stub.addResult(String.class, "result2");

		assertEquals("expected second result",
		             "result2", stub.invoke(invocationReturning(String.class)));
	}

	class UnsupportedReturnType
	{
	}

	public void testInvocationWithAnUnregisteredReturnTypeCausesAnAssertionFailedError()
	        throws Throwable {
		Class unsupportedReturnType = UnsupportedReturnType.class;
		Class[] supportedReturnTypes = {
			boolean.class, byte.class, char.class, short.class, int.class, long.class,
			float.class, double.class,
			Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class,
			Float.class, Double.class,
			String.class
		};

		try {
			stub.invoke(invocationReturning(unsupportedReturnType));
		}
		catch (AssertionFailedError ex) {
			String message = ex.getMessage();

			AssertMo.assertIncludes("message should include name of unsupported type",
			                        unsupportedReturnType.getName(), message);

			for (int i = 0; i < supportedReturnTypes.length; i++) {
				AssertMo.assertIncludes("message should include names of expected types",
				                        supportedReturnTypes[i].getName(), message);
			}
			return;
		}

		fail("should have failed");
	}

	public void assertHasRegisteredValue( DefaultResultStub defaultResultStub,
	                                      Class resultType,
	                                      Object resultValue )
	        throws Throwable {
		assertEquals("expected " + resultValue + " to be returned",
		             resultValue, defaultResultStub.invoke(invocationReturning(resultType)));
	}

	public void assertHasNotRegisteredReturnType( DefaultResultStub defaultResultStub,
	                                              Class resultType )
	        throws Throwable {
		try {
			defaultResultStub.invoke(invocationReturning(resultType));
			fail("stub should not support return type " + resultType);
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
