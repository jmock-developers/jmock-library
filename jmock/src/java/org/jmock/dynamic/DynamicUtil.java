/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.lang.reflect.Array;
import java.lang.reflect.Proxy;

public class DynamicUtil {
    public static String toReadableString(Object element) {
        if (element == null) {
            return "null";
        }
        
        if (Proxy.isProxyClass(element.getClass())) {
        	Object invocationHandler = Proxy.getInvocationHandler(element);
        	if( invocationHandler instanceof DynamicMock ) {
        		return invocationHandler.toString();
        	} else {
        		return element.toString();
        	}
        }

        if (element.getClass().isArray()) {
            return join(element, new StringBuffer()).toString();
        } else {
            return element.toString();
        }
    }

    public static StringBuffer join(Object array, StringBuffer buf) {
        buf.append("[");
        for (int i = 0; i < Array.getLength(array); i++) {
            if (i > 0) buf.append(", ");

            Object element = Array.get(array, i);

            if (null == element) {
                buf.append("<null>");
            } else if (element.getClass().isArray()) {
                join(element, buf);
            } else {
                buf.append("<");
                buf.append(toReadableString(element));
                buf.append(">");
            }
        }
        buf.append("]");
        return buf;
   }

	public static String classShortName(Class c) {
	    return c.getName().substring(c.getPackage().getName().length() + 1);
	}
}