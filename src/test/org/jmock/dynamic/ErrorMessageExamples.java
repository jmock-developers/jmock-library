/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.Assert;
import junit.textui.TestRunner;


public class ErrorMessageExamples {

    public static void main(String[] args) {
        TestRunner.main(new String[]{ErrorMessageExamples.class.getName()});
        //TODO work out how to build up test methods to be run just by this app
    }

    public void testUncalledMethodError() {
        Assert.assertEquals("should not be equa1", "a", "b");
    }
}
