/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;


public class ConsecutiveCallsAcceptanceTest extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    
    public void testCanEasilySpecifySequenceOfStubsForSameMethod() {
        context.expects(new InAnyOrder() {{
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
