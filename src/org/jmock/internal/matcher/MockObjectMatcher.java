package org.jmock.internal.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class MockObjectMatcher extends BaseMatcher<Object> {
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
