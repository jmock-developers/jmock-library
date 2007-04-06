/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.lang.reflect.Array;
import java.util.Map;


/**
 * A public MapEntry data type that can be used where the Map.Entry interface is required
 * (needed because the Sun implementation is package protected)
 */

public class MapEntry implements Map.Entry
{
    private Object myKey;
    private Object myValue;

    public MapEntry( Object aKey, Object aValue ) {
        myKey = (aKey == null ? new Null() : aKey);
        myValue = (aValue == null ? new Null() : aValue);
    }

    public boolean equals( Object o ) {
        if (!(o instanceof MapEntry)) {
            return false;
        }
        MapEntry other = (MapEntry)o;

        if (myValue.getClass().isArray() && other.getValue().getClass().isArray()) {
            return arrayEquals(other.getValue());
        } 
        return myKey.equals(other.getKey()) && myValue.equals(other.getValue());
    }

    private final boolean arrayEquals( Object anArray ) {
        int i = 0;
        boolean endOfThisArray = false;
        boolean endOfAnotherArray = false;

        while (true) {
            Object valueOfThis = null;
            Object valueOfAnother = null;

            try {
                valueOfThis = Array.get(myValue, i);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                endOfThisArray = true;
            }

            try {
                valueOfAnother = Array.get(anArray, i);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                endOfAnotherArray = true;
            }

            if (endOfThisArray && endOfAnotherArray) {
                return true;
            }

            if (valueOfThis != null || valueOfAnother != null) {
                if (valueOfThis == null || !valueOfThis.equals(valueOfAnother)) {
                    return false;
                }
            }

            i++;
        }
    }

    public Object getKey() {
        return myKey;
    }

    public Object getValue() {
        return myValue;
    }

    public int hashCode() {
        int hash = myKey.hashCode();

        if (myValue.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(myValue); i++) {
                hash = hash ^ Array.get(myValue, i).hashCode();
            }
        } else {
            hash = hash ^ myValue.hashCode();
        }

        return hash;
    }

    public Object setValue( Object aValue ) {
        Object oldValue = myValue;
        myValue = (null == aValue ? new Null() : aValue);
        return oldValue;
    }

    public String toString() {
        return myKey.toString() + "=" + myValue.toString();
    }
}
