/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.TestCase;
import org.jmock.expectation.Null;


public class NullTest extends TestCase
{

	public void testEquals() {
		assertEquals("Should be same value", new Null(), new Null());
		assertEquals("Should be same hashCode", new Null().hashCode(), new Null().hashCode());

		assertEquals("Should be same value", new Null("one"), new Null("two"));
		assertEquals("Should be same hashCode", new Null("one").hashCode(), new Null("two").hashCode());

		// Compare with other objects to assert that they are not equal
		assertEquals("Not equal to something else", false, new Null("one").equals("one"));
		assertEquals("Not equal to something else", false, new Null().equals(new Integer(2)));
	}

	public void testDescription() {
		assertEquals("Description", "what it is", new Null("what it is").toString());
	}
}
