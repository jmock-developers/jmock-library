/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import java.util.Hashtable;
import java.util.Iterator;
import org.jmock.core.Verifiable;


/**
 * The ReturnObjectBag is a map containing instances  of ReturnObjectList.
 * A single instance is held for each mapkey. Every time a call to putObjectToReturn or
 * getNextReturnObject is made an object is added or removed from the ReturnObjectList for
 * the given key.
 * This allows the ReturnObjectBag to be used to return an ordered list of objects for each key
 * regardless of the order in which the key requests are made.
 * 
 * @author Jeff Martin
 * @version $Revision$
 * @see ReturnObjectList
 */
public class ReturnObjectBag implements Verifiable
{
    private final Hashtable returnObjectLists = new Hashtable();
    private final String name;

    /**
     * @param name Name used to describe an instance of ReturnObjectBag in error messages
     */
    public ReturnObjectBag( String name ) {
        this.name = name;
    }

    /**
     * Places an object into the list of return objects for a particular key
     *
     * @param key   the key against which the object will be stored
     * @param value the value to be added to the list for that key
     * @see ReturnObjectList#addObjectToReturn
     */
    public void putObjectToReturn( Object key, Object value ) {
        if (key == null) {
            key = Null.NULL;
        }
        ReturnObjectList returnObjectList = (ReturnObjectList)returnObjectLists.get(key);
        if (returnObjectList == null) {
            returnObjectList = new ReturnObjectList(name + "." + key.toString());
            returnObjectLists.put(key, returnObjectList);
        }

        returnObjectList.addObjectToReturn(value);
    }

    /**
     * Places an object into the list of return objects for a particular int key
     *
     * @param key   the key against which the object will be stored
     * @param value the value to be added to the list for that key
     * @see ReturnObjectList#addObjectToReturn
     */
    public void putObjectToReturn( int key, Object value ) {
        putObjectToReturn(new Integer(key), value);
    }

    /**
     * Places an int into the list of return objects for a particular key. The value can be retrieved
     * using the getNextReturnInt invokedMethod
     *
     * @param key   the key against which the object will be stored
     * @param value the value to be added to the list for that key
     * @see ReturnObjectList#addObjectToReturn
     * @see #getNextReturnInt
     */
    public void putObjectToReturn( Object key, int value ) {
        putObjectToReturn(key, new Integer(value));
    }

    /**
     * Places an boolean into the list of return objects for a particular key. The value can be retrieved
     * using the getNextReturnBoolean invokedMethod
     *
     * @param key   the key against which the object will be stored
     * @param value the value to be added to the list for that key
     * @see ReturnObjectList#addObjectToReturn
     * @see #getNextReturnBoolean
     */
    public void putObjectToReturn( Object key, boolean value ) {
        putObjectToReturn(key, new Boolean(value));
    }

    /**
     * Checks each the list for each key to verify that all no objects remain
     * in the list for that key.
     *
     * @see ReturnObjectList#verify
     */
    public void verify() {
        for (Iterator it = returnObjectLists.values().iterator(); it.hasNext();) {
            ((ReturnObjectList)it.next()).verify();
        }
    }

    /**
     * Returns the next object in the ReturnObjectList for a given key.
     * The call will throw an AssertFailError if the requested key is
     * not present within this ReturnObjectBag.
     *
     * @param key The key for which the next object should be returned.
     * @return The next object from the ReturnObjectList stored against the given key.
     * @see ReturnObjectList#nextReturnObject
     */
    public Object getNextReturnObject( Object key ) {
        if (key == null) {
            key = Null.NULL;
        }
        ReturnObjectList returnObjectList = (ReturnObjectList)returnObjectLists.get(key);
        AssertMo.assertNotNull(name + " does not contain " + key.toString(), returnObjectList);
        return returnObjectList.nextReturnObject();
    }

    /**
     * Returns the next object in the ReturnObjectList for a given int key.
     * The call will throw an AssertFailError if the requested key is
     * not present within this ReturnObjectBag.
     *
     * @param key The key for which the next object should be returned.
     * @return The next object from the ReturnObjectList stored against the given key.
     * @see ReturnObjectList#nextReturnObject
     */
    public Object getNextReturnObject( int key ) {
        return getNextReturnObject(new Integer(key));
    }

    public Hashtable getHashTable() {
        return returnObjectLists;
    }

    public int getNextReturnInt( Object key ) {
        return ((Integer)getNextReturnObject(key)).intValue();
    }

    public boolean getNextReturnBoolean( Object key ) {
        return ((Boolean)getNextReturnObject(key)).booleanValue();
    }
}
