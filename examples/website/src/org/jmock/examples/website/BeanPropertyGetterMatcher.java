/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.website;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import junit.framework.AssertionFailedError;
import org.jmock.core.Invocation;
import org.jmock.core.matcher.StatelessInvocationMatcher;


public class BeanPropertyGetterMatcher extends StatelessInvocationMatcher
{

    public boolean matches( Invocation invocation ) {
        Class beanClass = invocation.invokedMethod.getDeclaringClass();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            for (int i = 0; i < properties.length; i++) {
                if (invocation.invokedMethod.equals(properties[i].getReadMethod())) return true;
            }
            return false;
        }
        catch (IntrospectionException ex) {
            throw new AssertionFailedError("could not introspect bean class" + beanClass + ": " + ex.getMessage());
        }
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("any bean property getter");
    }
}
