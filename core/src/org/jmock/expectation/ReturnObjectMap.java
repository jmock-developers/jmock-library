/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import java.util.Enumeration;
import java.util.Hashtable;
import org.jmock.core.Verifiable;


/**
 * @version $Revision$
 */
public class ReturnObjectMap extends MockObject
{
	private final Hashtable returnValues = new Hashtable();
	private final String name;

	public ReturnObjectMap( String name ) {
		this.name = name;
	}

	public Object getValue( Object key ) {
		if (key == null) {
			key = Null.NULL;
		}
		AssertMo.assertTrue(name + " does not contain " + key.toString(), returnValues.containsKey(key));
		return ((CallCounter)returnValues.get(key)).getValue();
	}

	public Object getValue( short key ) {
		return getValue(new Short(key));
	}

	public void putReturnValue( Object key, Object value ) {
		returnValues.put(key != null ? key : Null.NULL, new CallCounter(key, value));
	}

	public void putReturnValue( Object key, int value ) {
		putReturnValue(key, new Integer(value));
	}

	public void putReturnValue( short key, Object value ) {
		putReturnValue(new Short(key), value);
	}

	public void putReturnValue( Object key, boolean value ) {
		putReturnValue(key, new Boolean(value));
	}

	public int getIntValue( Object key ) {
		return ((Integer)getValue(key)).intValue();
	}

	public boolean getBooleanValue( String key ) {
		return ((Boolean)getValue(key)).booleanValue();
	}

	private class CallCounter implements Verifiable
	{
		private int count = 0;
		private final Object value;
		private final Object key;

		public CallCounter( Object key, Object value ) {
			this.key = key;
			this.value = value;
		}

		public Object getValue() {
			count++;
			return value;
		}

		public void verify() {
			AssertMo.assertTrue("Object never called for key: " + key, count > 0);
		}
	}

	public void verify() {
		super.verify();

		for (Enumeration enumeration = returnValues.elements(); enumeration.hasMoreElements();) {
			((Verifiable)enumeration.nextElement()).verify();
		}
	}
}
