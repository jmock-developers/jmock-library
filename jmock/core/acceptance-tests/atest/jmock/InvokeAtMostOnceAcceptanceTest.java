package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;

public class InvokeAtMostOnceAcceptanceTest extends MockObjectTestCase {
	public interface MockedType {
		public void m();
	}

	public void testPassesIfMethodExpectedAtMostOnceIsNotCalled() {
		Mock mock = mock(MockedType.class, "mock");
		
		mock.expects(atMostOnce()).method("m");
	}
	
	public void testPassesIfMethodExpectedAtMostOnceIsCalledOnceOnly() {
		Mock mock = mock(MockedType.class, "mock");
		
		mock.expects(atMostOnce()).method("m");
		
		MockedType proxy = (MockedType)mock.proxy();
		proxy.m();
	}
	
	public void testFailsIfMethodExpectedAtMostOnceIsCalledMoreThanOnce() {
		Mock mock = mock(MockedType.class, "mock");
		
		mock.expects(atMostOnce()).method("m");
		
		MockedType proxy = (MockedType)mock.proxy();
		proxy.m();
		
		try {
			proxy.m();
			fail("should have thrown DynamicMockError");
		}
		catch (DynamicMockError error) {
			// expected
		}
	}
	
}
