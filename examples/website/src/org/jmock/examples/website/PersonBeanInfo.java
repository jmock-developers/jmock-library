/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.website;

import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;


public class PersonBeanInfo extends SimpleBeanInfo
{
    ArrayList properties = new ArrayList();
    ArrayList methods = new ArrayList();

    public PersonBeanInfo() throws IntrospectionException {
        Class beanClass = Person.class;

        properties.add(new PropertyDescriptor("name", beanClass, "name", null));
        properties.add(new PropertyDescriptor("age", beanClass, "age", null));
        properties.add(new PropertyDescriptor("spouse", beanClass, "spouse", null));
        properties.add(new PropertyDescriptor("children", beanClass, "children", null));

        try {
            methods.add(new MethodDescriptor(beanClass.getMethod("birth", new Class[0])));
            methods.add(new MethodDescriptor(beanClass.getMethod("school", new Class[0])));
            methods.add(new MethodDescriptor(beanClass.getMethod("work", new Class[0])));
            methods.add(new MethodDescriptor(beanClass.getMethod("death", new Class[0])));
        }
        catch (NoSuchMethodException ex) {
            throw new IntrospectionException(ex.getMessage());
        }
    }

    public MethodDescriptor[] getMethodDescriptors() {
        return (MethodDescriptor[])methods.toArray(new MethodDescriptor[methods.size()]);
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return (PropertyDescriptor[])properties.toArray(new PropertyDescriptor[properties.size()]);
    }
}
