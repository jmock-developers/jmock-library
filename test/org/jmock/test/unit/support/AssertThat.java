/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.unit.support;


import junit.framework.AssertionFailedError;
import org.junit.Assert;

public class AssertThat extends Assert
{
    public static void arraysAreEqual( String description,
                                     Object[] expectedArray,
                                     Object[] actualArray ) {
        assertEquals(description + " (different lengths)",
                     expectedArray.length,
                     actualArray.length);
        
        for (int i = 0; i < expectedArray.length; i++) {
            assertEquals(description + " (element " + i + ")",
                         expectedArray[i],
                         actualArray[i]);
        }
    }

    public static void stringExcludes( String description,
                                       String excludeString,
                                       String targetString ) {
        assertTrue(description
                   + "\nExclude String: "
                   + excludeString
                   + "\n Target String: "
                   + targetString,
                   targetString.indexOf(excludeString) == -1);
    }

    public static void stringIncludes( String description,
                                       String includeString,
                                       String targetString ) {
        assertTrue(description
                   + "\nInclude String: "
                   + includeString
                   + "\n Target String: "
                   + targetString,
                   targetString.indexOf(includeString) != -1);
    }

    public static void stringStartsWith( String description,
                                         String startString,
                                         String targetString ) {
        assertTrue(description
                   + "\n Start String: "
                   + startString
                   + "\nTarget String: "
                   + targetString,
                   targetString.startsWith(startString));
    }

//    static protected void failNotEquals( String message,
//                                         Object expected,
//                                         Object actual ) {
//        String formatted = "";
//        if (message != null) {
//            formatted = message + " ";
//        }
//        fail(formatted + "\nExpected:<" + expected + ">\nReceived:<" + actual + ">");
//    }

    public static void fails( String message, Runnable runnable ) {
        try {
            runnable.run();
        }
        catch (AssertionFailedError expected) {
            return;
        }
        fail(message);
    }
}
