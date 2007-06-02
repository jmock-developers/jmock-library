/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.util.HashMap;
import org.jmock.core.Verifiable;


public class ExpectationMap implements Expectation, Verifiable
{
    private HashMap myEntries;
    private ExpectationSet myKeys;

    public ExpectationMap( String name ) {
        myEntries = new HashMap();
        myKeys = new ExpectationSet(name + " keys");
    }

    public void addExpected( Object key, Object value ) {
        myKeys.addExpected(key);
        myEntries.put(key, value);
    }

    public void addExpectedMissing( Object key ) {
        myKeys.addExpected(key);

    }

    public Object get( Object key ) {
        myKeys.addActual(key);
        return myEntries.get(key);
    }

    public boolean hasExpectations() {
        return myKeys.hasExpectations();
    }

    public void setExpectNothing() {
        myKeys.setExpectNothing();
    }

    public void setFailOnVerify() {
        myKeys.setFailOnVerify();
    }

    public void verify() {
        myKeys.verify();
    }
}
