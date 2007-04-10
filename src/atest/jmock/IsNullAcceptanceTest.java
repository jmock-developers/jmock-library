package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;

/*
 * See Jira issue JMOCK-101
 */
public class IsNullAcceptanceTest extends MockObjectTestCase {
    public interface MockedType {
        public void doSomethingWith(String s1, String s2);
    }
    
    Mock mock = mock(MockedType.class, "mock");
    
    public void testCanUseTheNullKeywordInPlaceOfNULLConstraint() {
        mock.expects(once()).method("doSomethingWith").with(null, null);
        
        MockedType proxy = (MockedType)mock.proxy();
        
        proxy.doSomethingWith(null, null);
        
        try {
            proxy.doSomethingWith("not", "null");
            fail("should have failed");
        }
        catch (DynamicMockError expected) {}
    }
}
