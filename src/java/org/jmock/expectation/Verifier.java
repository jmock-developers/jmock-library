/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import junit.framework.Assert;

import java.lang.reflect.Field;
import java.util.Vector;

/**
 * Helper class to verify all {@link org.jmock.expectation.Expectation Expectation}s
 * of an object.
 * The {@link org.jmock.expectation.Verifier Verifier} class provides two static
 * methods to verify objects:
 * <ul>
 * <li>{@link org.jmock.expectation.Verifier#verifyObject(java.lang.Object) verifyObject(Object)}</li>
 * <li>{@link Verifier#verifyField(Field,Object,Vector) verifyField(Field, Object)}</li>
 * </ul>
 * These two methods can be used to verify any expectation to assert that
 * they still hold.<p>
 * <b>Example usage:</b><p>
 * Verifying all expectations on one object at a time:<p>
 * <pre>
 * public class MockX implements Verifiable {
 *    private Expectation... anExpectation = new Expectation...(...);
 *    private Expectation... aSecondExpectation = new Expectation...(...);
 * <p/>
 *    public void verify() {
 *       Verifier.verifyObject(this);
 *    }
 * }
 * </pre>
 * This example shows how most mocks implement
 * {@link org.jmock.expectation.Verifiable Verifiable}, i.e.:  by delegation.
 * 
 * @version $Id$
 * @see org.jmock.expectation.Expectation
 * @see org.jmock.expectation.Verifiable
 */
public class Verifier {

    private static Vector myProcessingObjects = new Vector();

    /**
     * Verifies all the fields of type Verifiable in the given object, including
     * those inherited from superclasses.
     * 
     * @param anObject The object to be verified.
     */
    static synchronized public void verifyObject(Object anObject) {
        verifyFieldsForClass(anObject, anObject.getClass(), myProcessingObjects);
    }

    static private void verifyFieldsForClass(Object anObject, Class aClass, Vector alreadyProcessed) {
        if (alreadyProcessed.contains(anObject) || isBaseObjectClass(aClass)) {
            return;
        }

        verifyFieldsForClass(anObject, aClass.getSuperclass(), alreadyProcessed);
        try {
            alreadyProcessed.addElement(anObject);

            Field[] fields = aClass.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                verifyField(fields[i], anObject, alreadyProcessed);
            }
        } finally {
            alreadyProcessed.removeElement(anObject);
        }
    }

    static private void verifyField(Field aField, Object anObject, Vector alreadyProcessed) {
        try {
            aField.setAccessible(true);
            Object fieldObject = aField.get(anObject);

            if (isVerifiable(fieldObject) && !alreadyProcessed.contains(fieldObject)) {
                ((Verifiable) fieldObject).verify();
            }
        } catch (IllegalAccessException e) {
            Assert.fail("Could not access field " + aField.getName());
        }
    }

    private static boolean isVerifiable(Object anObject) {
        return anObject instanceof Verifiable;
    }

    private static boolean isBaseObjectClass(Class aClass) {
        return aClass.equals(Object.class);
    }
}
