/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.util.Enumeration;
import java.util.Iterator;


/**
 * An <EM>ExpectationCollection</EM> is an expectation that supports multiple values, such as lists
 * and sets.
 * <p/>
 * The addition methods distinguish between adding a single value and unpacking the contents of
 * a collection. We have to make this distinction so that it is possible to add an array, enumeration,
 * or iterator as a single expected object, rather than adding its contents.
 * @since 1.0
 */
public interface ExpectationCollection extends Expectation
{

    void addActual( Object actual );

    void addActual( long actual );

    void addActualMany( Object[] actuals );

    void addActualMany( Enumeration actuals );

    void addActualMany( Iterator actuals );


    void addExpected( Object expected );

    void addExpected( long expected );

    void addExpectedMany( Object[] expectedItems );

    void addExpectedMany( Enumeration expectedItems );

    void addExpectedMany( Iterator expectedItems );
}
