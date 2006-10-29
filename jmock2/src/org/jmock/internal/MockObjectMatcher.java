package org.jmock.internal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class MockObjectMatcher implements Matcher<Object> {
    private Object mockObject;
    
    public MockObjectMatcher(Object mockObject) {
        this.mockObject = mockObject;
    }
    
    public boolean matches(Object o) {
        return o == mockObject;
    }

    public void describeTo(Description description) {
        description.appendText(mockObject.toString());
    }
}
