/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

/**
 * A class that represents the <code>null</code> value.
 * The {@link org.jmock.expectation.Null Null} class is used when an
 * {@link org.jmock.expectation.Expectation Expectation} is set to expect nothing.
 * <p/>
 * <b>Example usage:</b>
 * <pre>
 * public class MockX {
 *    private Expectation... anExpectation = new Expectation...(...);
 * <p/>
 *    public MockX() {
 *       anExpectation.setExpectNothing();
 *    }
 * <p/>
 *    public void setAnExpectation(Object value) {
 *       anExpectation.setExpected(value);
 *    }
 * <p/>
 *    public void setActual(Object value) {
 *       anExpectation.setActual(value);
 *    }
 * }
 * </pre>
 * The act of calling {@link org.jmock.expectation.Expectation#setExpectNothing() Expectation.setExpectNothing()}
 * tells the expectation that it should expect no values to change.  Since
 * all {@link org.jmock.expectation.Null Null} objects are equal to themselves,
 * most expectations set their expected value to an instance of
 * {@link org.jmock.expectation.Null Null}, and at the same time, set their actual
 * value to another instance of {@link org.jmock.expectation.Null Null}.
 * This way, when {@link org.jmock.expectation.Verifiable#verify() verify()} checks
 * expectations, they will compare two {@link org.jmock.expectation.Null Null}
 * objects together, which is guaranteed to succeed.
 * 
 * @author <a href="mailto:fbos@users.sourceforge.net">Francois Beausoleil (fbos@users.sourceforge.net)</a>
 * @version $Id$
 */
public class Null
{
    /**
     * The default description for all {@link org.jmock.expectation.Null Null}
     * objects.
     * This String is equal to "<code>Null</code>".
     */
    public static final String DEFAULT_DESCRIPTION = "Null";

    /**
     * A default {@link org.jmock.expectation.Null Null} object.
     * Instead of always instantiating new {@link org.jmock.expectation.Null Null}
     * objects, consider using a reference to this object instead. This way,
     * the virtual machine will not be taking the time required to instantiate
     * an object everytime it is required.
     */
    public static final Null NULL = new Null();

    /**
     * The description of this {@link org.jmock.expectation.Null Null} object.
     */
    final private String myDescription;

    /**
     * Instantiates a new {@link org.jmock.expectation.Null Null} object with
     * the default description.
     *
     * @see org.jmock.expectation.Null#DEFAULT_DESCRIPTION
     */
    public Null() {
        this(DEFAULT_DESCRIPTION);
    }

    /**
     * Instantiates a new {@link org.jmock.expectation.Null Null} object and
     * sets it's description.
     *
     * @param description
     */
    public Null( String description ) {
        super();
        myDescription = description;
    }

    /**
     * Determines equality between two objects.
     * {@link org.jmock.expectation.Null Null} objects are only equal to
     * another instance of themselves.
     *
     * @param other
     */
    public boolean equals( Object other ) {
        return other instanceof Null;
    }

    /**
     * Returns this {@link org.jmock.expectation.Null Null} object's hashCode.
     * All  {@link org.jmock.expectation.Null Null} return the same
     * hashCode value.
     */
    public int hashCode() {
        return 0;
    }

    /**
     * Returns a string representation of this {@link org.jmock.expectation.Null Null}
     * object.
     * This merely returns the string passed to the constructor initially.
     */
    public String toString() {
        return myDescription;
    }
}
