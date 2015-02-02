/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;


public class ConsecutiveCallsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    
    public void testCanEasilySpecifySequenceOfStubsForSameMethod() {
        context.checking(new Expectations() {{
            atLeast(1).of (mock).returnString();
                will(onConsecutiveCalls(returnValue("hello"),
                                        returnValue("bonjour"),
                                        returnValue("guten Tag")));
        
        }});

        assertEquals("hello", mock.returnString());
        assertEquals("bonjour", mock.returnString());
        assertEquals("guten Tag", mock.returnString());
    }
}
