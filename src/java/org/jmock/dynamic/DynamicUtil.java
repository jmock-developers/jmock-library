/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DynamicUtil {
    public static Object[] asObjectArray(Object primitiveArray) {

        if (primitiveArray instanceof Object[]) {
            return (Object[]) primitiveArray;
        }

        List result = new ArrayList();

        if (primitiveArray instanceof boolean[]) {
            boolean[] booleanArray = (boolean[]) primitiveArray;

            for (int i = 0; i < booleanArray.length; i++) {
                result.add(new Boolean(booleanArray[i]));
            }
        } else if (primitiveArray instanceof char[]) {
            char[] charArray = (char[]) primitiveArray;

            for (int i = 0; i < charArray.length; i++) {
                result.add(new Character(charArray[i]));
            }
        } else if (primitiveArray instanceof byte[]) {
            byte[] byteArray = (byte[]) primitiveArray;

            for (int i = 0; i < byteArray.length; i++) {
                result.add(new Byte(byteArray[i]));
            }

        } else if (primitiveArray instanceof short[]) {
            short[] shortArray = (short[]) primitiveArray;

            for (int i = 0; i < shortArray.length; i++) {
                result.add(new Short(shortArray[i]));
            }
        } else if (primitiveArray instanceof int[]) {
            int[] intArray = (int[]) primitiveArray;

            for (int i = 0; i < intArray.length; i++) {
                result.add(new Integer(intArray[i]));
            }
        } else if (primitiveArray instanceof long[]) {
            long[] longArray = (long[]) primitiveArray;

            for (int i = 0; i < longArray.length; i++) {
                result.add(new Long(longArray[i]));
            }
        } else if (primitiveArray instanceof float[]) {
            float[] floatArray = (float[]) primitiveArray;

            for (int i = 0; i < floatArray.length; i++) {
                result.add(new Float(floatArray[i]));
            }
        } else if (primitiveArray instanceof double[]) {
            double[] doulbeArray = (double[]) primitiveArray;

            for (int i = 0; i < doulbeArray.length; i++) {
                result.add(new Float(doulbeArray[i]));
            }
        } else {
            throw new RuntimeException("Unknown primitive data type for Object[] conversion " + primitiveArray.toString());
        }

        return result.toArray();
    }

    public static String proxyToString(Object element) {

        if (element == null) {
            return "null";
        }
        if (Proxy.isProxyClass(element.getClass())) {
            try {
                Method mockNameMethod = CoreMock.class.getDeclaredMethod("getMockName", new Class[0]);
                Object debugableResult = Proxy.getInvocationHandler(element).invoke(element, mockNameMethod, new Object[0]);
                return debugableResult.toString();
            } catch (Throwable e) {
                return element.getClass().getName();
            }
        }

        if (element.getClass().isArray()) {
            StringBuffer buf = new StringBuffer();
            buf.append("[");
            join(asObjectArray(element), buf);
            buf.append("]");
            return buf.toString();
        } else {
            return element.toString();
        }
    }

    public static String methodToString(String name, Object[] args) {
        StringBuffer buf = new StringBuffer();

        buf.append(name);
        buf.append("(");
        join(args, buf);
        buf.append(")");

        return buf.toString();
    }

    public static void join(Object[] elements, StringBuffer buf) {
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }

            Object element = elements[i];

            if (null == element) {
                buf.append("<null>");
            } else if (element.getClass().isArray()) {
                buf.append("[");
                join(asObjectArray(element), buf);
                buf.append("]");
            } else {
                buf.append("<");
                buf.append(proxyToString(element));
                buf.append(">");
            }
        }
    }
}
