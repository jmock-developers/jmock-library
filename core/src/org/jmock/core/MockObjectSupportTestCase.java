/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.Collection;

import org.jmock.core.constraint.And;
import org.jmock.core.constraint.HasProperty;
import org.jmock.core.constraint.HasPropertyWithValue;
import org.jmock.core.constraint.HasToString;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.constraint.IsArrayContaining;
import org.jmock.core.constraint.IsCloseTo;
import org.jmock.core.constraint.IsCollectionContaining;
import org.jmock.core.constraint.IsCompatibleType;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.constraint.IsIn;
import org.jmock.core.constraint.IsInstanceOf;
import org.jmock.core.constraint.IsMapContaining;
import org.jmock.core.constraint.IsNot;
import org.jmock.core.constraint.IsNull;
import org.jmock.core.constraint.IsSame;
import org.jmock.core.constraint.Or;
import org.jmock.core.constraint.StringContains;
import org.jmock.core.constraint.StringEndsWith;
import org.jmock.core.constraint.StringStartsWith;
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

    /**
     * @since 1.1.0
     */
    public StringContains contains( String substring ) {
        return stringContains(substring);
    }
    
    /**
     * @since 1.1.0
     */
    public StringStartsWith startsWith( String substring ) {
        return new StringStartsWith(substring);
    }
    
    /**
     * @since 1.1.0
     */
    public StringEndsWith endsWith( String substring ) {
        return new StringEndsWith(substring);
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
    
    /**
      * @since 1.1.0
      */
    public HasPropertyWithValue hasProperty(String propertyName, Constraint expectation) {
        return new HasPropertyWithValue(propertyName, expectation);
    }
    
    /**
     * @since 1.1.0
     */
    public HasProperty hasProperty(String propertyName) {
       return new HasProperty(propertyName);
    }
    
    /**
     * @since 1.1.0
     */
    public HasToString toString(Constraint toStringConstraint) {
        return new HasToString(toStringConstraint);
    }
    
    /**
     * @since 1.1.0
     */
    public IsCompatibleType compatibleType(Class baseType) {
        return new IsCompatibleType(baseType);
    }
    
    /**
     * @since 1.1.0
     */
    public IsIn isIn(Collection collection) {
        return new IsIn(collection);
    }
    
    /**
     * @since 1.1.0
     */
    public IsIn isIn(Object[] array) {
        return new IsIn(array);
    }
    
    /**
     * @since 1.1.0
     */
    public IsCollectionContaining collectionContaining(Constraint elementConstraint) {
        return new IsCollectionContaining(elementConstraint);
    }
    
    /**
     * @since 1.1.0
     */
    public IsCollectionContaining collectionContaining(Object element) {
        return collectionContaining(eq(element));
    }
    
    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(Constraint elementConstraint) {
        return new IsArrayContaining(elementConstraint);
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(Object element) {
        return arrayContaining(eq(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(boolean element) {
        return arrayContaining(new Boolean(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(byte element) {
        return arrayContaining(new Byte(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(short element) {
        return arrayContaining(new Short(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(char element) {
        return arrayContaining(new Character(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(int element) {
        return arrayContaining(new Integer(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(long element) {
        return arrayContaining(new Long(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(float element) {
        return arrayContaining(new Float(element));
    }

    /**
     * @since 1.1.0
     */
    public IsArrayContaining arrayContaining(double element) {
        return arrayContaining(new Double(element));
    }
    
    /**
     * @since 1.1.0
     */
    public IsMapContaining mapContaining(Constraint keyConstraint, Constraint valueConstraint) {
        return new IsMapContaining(keyConstraint, valueConstraint);
    }
    
    /**
     * @since 1.1.0
     */
    public IsMapContaining mapContaining(Object key, Object value) {
        return mapContaining(eq(key), eq(value));
    }

    /**
     * @since 1.1.0
     */
    public IsMapContaining mapWithKey(Object key) {
        return mapWithKey(eq(key));
    }

    /**
     * @since 1.1.0
     */
    public IsMapContaining mapWithKey(Constraint keyConstraint) {
        return new IsMapContaining(keyConstraint, ANYTHING);
    }

    /**
     * @since 1.1.0
     */
    public IsMapContaining mapWithValue(Object value) {
        return mapWithValue(eq(value));
    }

    /**
     * @since 1.1.0
     */
    public IsMapContaining mapWithValue(Constraint valueConstraint) {
        return new IsMapContaining(ANYTHING, valueConstraint);
    }
}
