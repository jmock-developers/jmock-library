/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.TestCase;
import org.jmock.expectation.MapEntry;


public class MapEntryTest extends TestCase
{

	public void testEquals() {
		assertEquals("Should be expected value",
		             new MapEntry("A", "2"),
		             new MapEntry("A", "2"));

		assertTrue("Should not be equal",
		           !new MapEntry("A", "2").equals(new MapEntry("A", "1")));

		assertTrue("Should not be equal",
		           !new MapEntry("A", "2").equals(new MapEntry("B", "2")));

		assertEquals("Should be equal with null value",
		             new MapEntry("A", null),
		             new MapEntry("A", null));

		assertEquals("Should be equal with null key",
		             new MapEntry(null, "A"),
		             new MapEntry(null, "A"));

		assertEquals("Should be equal byte arrays",
		             new MapEntry("A", "A".getBytes()),
		             new MapEntry("A", "A".getBytes()));

		assertTrue("Should not be equal byte arrays",
		           !new MapEntry("A", "AB".getBytes()).equals(new MapEntry("A", "A".getBytes())));

		assertTrue("Should not be equal byte arrays",
		           !new MapEntry("A", "A".getBytes()).equals(new MapEntry("A", "AB".getBytes())));

		assertTrue("Should not be equal byte arrays",
		           !new MapEntry("A", null).equals(new MapEntry("A", "AB".getBytes())));
	}

	public void testHashCode() {
		assertEquals("Should be equal hashcodes",
		             new MapEntry("A", "A".getBytes()).hashCode(),
		             new MapEntry("A", "A".getBytes()).hashCode());

	}
}
