/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package atest.jmock.dynamock;

import junit.framework.TestCase;

import org.jmock.dynamock.C;
import org.jmock.dynamock.Mock;

public class FIFOInvocationOrderFeatureRequest extends TestCase {
	public interface WithReturnValue {
		Object withNoParams();
    }
    
    public void testReturnsMultipleValuesInTheSameOrderAsExpectationsAreDefinedInTheSourceCode() {
    	Mock mock = new Mock(WithReturnValue.class);
        
        mock.expectAndReturn("withNoParams", C.NO_ARGS, "one");
        mock.expectAndReturn("withNoParams", C.NO_ARGS, "two");
        
        WithReturnValue returner = (WithReturnValue)mock.proxy();
        assertEquals("first call", "one", returner.withNoParams());
        assertEquals("second call", "two", returner.withNoParams());
        
        mock.verify();
    }
}
