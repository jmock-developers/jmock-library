/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmock.core.Constraint;
import org.jmock.util.PropertyUtil;

/**
 * Constraint that asserts that a JavaBean property on an argument passed to the
 * mock object meets the provided constraint. This is useful for when objects
 * are created within code under test and passed to a mock object, and you wish
 * to assert that the created object has certain properties.
 * 
 * <h2>Example Usage</h2>
 * Consider the situation where we have a class representing a person, which
 * follows the basic JavaBean convention of having get() and possibly set()
 * methods for it's properties: <code>
 * public class Person {
 *     private String name;
 *     
 *     public Person(String person) {
 *         this.person = person;
 *     }
 *     
 *     public String getName() {
 *         return name;
 *     }
 * }
 * </code> And that these person
 * objects are generated within a piece of code under test (a class named
 * PersonGenerator). This object is sent to one of our mock objects which
 * overrides the PersonGenerationListener interface: <code>
 * public interface PersonGenerationListener {
 *     public void personGenerated(Person person);
 * }
 * </code>
 * In order to check that the code under test generates a person with name
 * "Iain" we would do the following:
 * 
 * <code>
 * Mock personGenListenerMock = mock(PersonGenerationListener.class);
 * personGenListenerMock.expects(once()).method("personGenerated").with(and(isA(Person.class), hasProperty("Name", eq("Iain")));
 * PersonGenerationListener listener = (PersonGenerationListener)personGenListenerMock.proxy();
 * </code>
 * 
 * If an exception is thrown by the getter method for a property, the property
 * does not exist, is not readable, or a reflection related exception is thrown
 * when trying to invoke it then this is treated as an evaluation failure and
 * the eval method will return false.
 * 
 * This constraint class will also work with JavaBean objects that have explicit
 * bean descriptions via an associated BeanInfo description class. See the
 * JavaBeans specification for more information:
 * 
 * http://java.sun.com/products/javabeans/docs/index.html
 * 
 * @author Iain McGinniss
 * @author Nat Pryce
 * @author Steve Freeman
 * @since 1.1.0
 */
public class HasPropertyWithValue implements Constraint {

    private static final Object[] NO_ARGUMENTS = new Object[0];

    private String propertyName;

    private Constraint expectation;

    public HasPropertyWithValue(String propertyName, Constraint expectation) {
        this.propertyName = propertyName;
        this.expectation = expectation;
    }

    public boolean eval(Object argument) {
        try {
            Method readMethod = getReadMethod(argument);
            return readMethod != null
                && expectation.eval(readMethod.invoke(argument, NO_ARGUMENTS));

        } catch (IntrospectionException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return false;
    }

    private Method getReadMethod(Object argument) throws IntrospectionException {
        PropertyDescriptor property = PropertyUtil.getPropertyDescriptor(propertyName, argument);
        return property == null ? null : property.getReadMethod();
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("hasProperty(\"");
        buffer.append(propertyName);
        buffer.append("\", ");
        expectation.describeTo(buffer);
        buffer.append(")");

        return buffer;
    }
}
