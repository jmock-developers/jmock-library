/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.util.Vector;
import org.jmock.core.Verifiable;


/**
 * <p>This class allows a list of objects to be setup which can be used whilst.The
 * list is check to make sure that all the object in it are used and that none
 * are left over at the end of a test.</p>
 * <p/>
 * <p>For ever sucessive call to nextReturnObject the next object in the list will
 * returned.</p>
 * <p/>
 * <p>If the nextReturnObject invokedMethod is called and there are no objects in the list
 * an assertion error will be thrown. If the verify invokedMethod is called and there
 * are objects still in the list an assertion error will also be thrown.</p>
 */
public class ReturnObjectList implements Verifiable
{

    private final Vector myObjects = new Vector();
    private final String myName;

    /**
     * Construct a new empty list
     *
     * @param aName Label used to identify list
     */
    public ReturnObjectList( String aName ) {
        this.myName = aName;
    }

    /**
     * Add a next object to the end of the list.
     *
     * @param anObjectToReturn object to be added to the list
     */
    public void addObjectToReturn( Object anObjectToReturn ) {
        myObjects.add(anObjectToReturn);
    }

    /**
     * Add a next boolean to the end of the list.
     *
     * @param aBooleanToReturn boolean to be added to the list
     */
    public void addObjectToReturn( boolean aBooleanToReturn ) {
        myObjects.add(new Boolean(aBooleanToReturn));
    }

    /**
     * Add a next integer to the end of the list.
     *
     * @param anIntegerToReturn integer to be added to the list
     */
    public void addObjectToReturn( int anIntegerToReturn ) {
        myObjects.add(new Integer(anIntegerToReturn));
    }

    /**
     * Returns the next object from the list. Each object it returned in the
     * order in which they where added.
     */
    public Object nextReturnObject() {
        AssertMo.assertTrue(myName + " has run out of objects.",
                            myObjects.size() > 0);
        return myObjects.remove(0);
    }

    /**
     * Verify that there are no objects left within the list.
     */
    public void verify() {
        AssertMo.assertEquals(myName + " has un-used objects.", 0,
                              myObjects.size());
    }
}
