/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.util;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.MockObject;


public class VerifierTest extends TestCase
{

	class OneVerifiable extends MockObject
	{
		private ExpectationValue myValue = new ExpectationValue("should fail");
		private int unusedField = 0;

		public OneVerifiable() {
			myValue.setFailOnVerify();
			myValue.setExpected("good");
			unusedField = unusedField + 1; // to avoid compiler warning
		}

		public void setValue( String aValue ) {
			myValue.setActual(aValue);
		}
	}

	class InheritVerifiable extends OneVerifiable
	{
	}

	class LoopingVerifiable extends MockObject
	{
		LoopingVerifiable myRef = this;
		boolean inVerify = false;

		LoopingVerifiable() {
			super();
		}

		public void setRef( LoopingVerifiable aRef ) {
			myRef = aRef;
		}

		public void verify() {
			assertTrue("Looping verification detected", !inVerify);
			inVerify = true;
			super.verify();
			inVerify = false;
		}
	}

	public void testInheritVerifiableFails() {
		InheritVerifiable inheritVerifiable = new InheritVerifiable();
		inheritVerifiable.setValue("bad");

		boolean hasThrownException = false;
		try {
			inheritVerifiable.verify();
		}
		catch (AssertionFailedError ex) {
			hasThrownException = true;
		}
		assertTrue("Should have thrown exception", hasThrownException);
	}


	public void testInheritVerifiablePasses() {
		InheritVerifiable inheritVerifiable = new InheritVerifiable();
		inheritVerifiable.setValue("good");

		inheritVerifiable.verify();
	}


	public void testNoVerifiables() {
		class NoVerifiables extends MockObject
		{
		}

		new NoVerifiables().verify();
	}


	public void testOneVerifiableFails() {
		OneVerifiable oneVerifiable = new OneVerifiable();
		oneVerifiable.setValue("bad");

		boolean hasThrownException = false;
		try {
			oneVerifiable.verify();
		}
		catch (AssertionFailedError ex) {
			hasThrownException = true;
		}
		assertTrue("Should have thrown exception", hasThrownException);
	}


	public void testOneVerifiablePasses() {
		OneVerifiable oneVerifiable = new OneVerifiable();
		oneVerifiable.setValue("good");

		oneVerifiable.verify();
	}

	public void testNoLoopVerifySingleLevel() {
		new LoopingVerifiable().verify();
	}

	public void testNoLoopVerifyMultiLevel() {
		LoopingVerifiable a = new LoopingVerifiable();
		LoopingVerifiable b = new LoopingVerifiable();

		a.setRef(b);
		b.setRef(a);
		a.verify();
	}
}
