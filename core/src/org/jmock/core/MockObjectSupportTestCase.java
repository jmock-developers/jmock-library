/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import org.jmock.core.constraint.*;
import org.jmock.util.Dummy;

/**
 * @since 1.0
 */
public abstract class MockObjectSupportTestCase extends VerifyingTestCase
{
    public static final Constraint ANYTHING = new IsAnything();
    public static final Constraint NULL = new IsNull();
    public static final Constraint NOT_NULL = new IsNot(NULL);
    
    public MockObjectSupportTestCase() {
    }

    public MockObjectSupportTestCase( String name ) {
        super(name);
    }

    public IsEqual eq( Object operand ) {
        return new IsEqual(operand);
    }

    public IsEqual eq( boolean operand ) {
        return eq(new Boolean(operand));
    }

    public IsEqual eq( byte operand ) {
        return eq(new Byte(operand));
    }

    public IsEqual eq( short operand ) {
        return eq(new Short(operand));
    }

    public IsEqual eq( char operand ) {
        return eq(new Character(operand));
    }

    public IsEqual eq( int operand ) {
        return eq(new Integer(operand));
    }

    public IsEqual eq( long operand ) {
        return eq(new Long(operand));
    }

    public IsEqual eq( float operand ) {
        return eq(new Float(operand));
    }

    public IsEqual eq( double operand ) {
        return eq(new Double(operand));
    }

    public IsCloseTo eq( double operand, double error ) {
        return new IsCloseTo(operand, error);
    }

    public IsSame same( Object operand ) {
        return new IsSame(operand);
    }

    public IsInstanceOf isA( Class operandClass ) {
        return new IsInstanceOf(operandClass);
    }

    public StringContains stringContains( String substring ) {
        return new StringContains(substring);
    }

    public StringContains contains( String substring ) {
        return stringContains(substring);
    }

    public IsNot not( Constraint c ) {
        return new IsNot(c);
    }

    public And and( Constraint left, Constraint right ) {
        return new And(left, right);
    }

    public Or or( Constraint left, Constraint right ) {
        return new Or(left, right);
    }

    public Object newDummy( Class dummyType ) {
        return Dummy.newDummy(dummyType);
    }

    public Object newDummy( Class dummyType, String name ) {
        return Dummy.newDummy(dummyType, name);
    }

    public Object newDummy( String name ) {
        return Dummy.newDummy(name);
    }
    
    
    /**
     * @since 1.0.1
     */
    public void assertThat(Object actual, Constraint constraint) {
        if (!constraint.eval(actual)) {
            StringBuffer message = new StringBuffer("\nExpected: ");
            constraint.describeTo(message);
            message.append("\n    got : ").append(actual).append('\n');
            fail(message.toString());
          }
    }

    public void assertThat(boolean actual, Constraint constraint) {
        assertThat(new Boolean(actual), constraint);
    }
    public void assertThat(byte actual, Constraint constraint) {
        assertThat(new Byte(actual), constraint);
    }
    public void assertThat(short actual, Constraint constraint) {
        assertThat(new Short(actual), constraint);
    }
    public void assertThat(char actual, Constraint constraint) {
        assertThat(new Character(actual), constraint);
    }
    public void assertThat(int actual, Constraint constraint) {
        assertThat(new Integer(actual), constraint);
    }
    public void assertThat(long actual, Constraint constraint) {
        assertThat(new Long(actual), constraint);
    }
    public void assertThat(float actual, Constraint constraint) {
        assertThat(new Float(actual), constraint);
    }
    public void assertThat(double actual, Constraint constraint) {
        assertThat(new Double(actual), constraint);
    }
}

