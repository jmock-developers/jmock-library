package test.jmock.core.stub;

import junit.framework.TestCase;

import org.jmock.core.Invocation;
import org.jmock.core.stub.DoAllStub;

import test.jmock.core.testsupport.MethodFactory;
import test.jmock.core.testsupport.MockStub;

public class DoAllStubTest extends TestCase {
    private Object invokedObject = new Object() {
        public String toString() {
            return "INVOKED_OBJECT";
        }
    };
    private MethodFactory methodFactory = new MethodFactory();
    private Invocation invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(String.class), null);

    
    public void testPerformsAllStubsInOrder() throws Throwable {
        MockStub[] stubs = new MockStub[4];
        for (int i = 0; i < stubs.length; i++) {
            stubs[i] = new MockStub("stubs["+i+"]");
            stubs[i].invokeInvocation.setExpected(invocation);
            if (i > 0) stubs[i].previousStub = stubs[i-1];
        }
        
        DoAllStub doAllStub = new DoAllStub(stubs);
        
        doAllStub.invoke(invocation);
        
        for (int i = 0; i < stubs.length; i++) stubs[i].verify();
    }
    
    public void testReturnsResultOfLastStub() throws Throwable {
        MockStub[] stubs = new MockStub[4];
        for (int i = 0; i < stubs.length; i++) {
            stubs[i] = new MockStub("stubs["+i+"]");
            stubs[i].invokeResult = "stubs["+i+"].invokeResult";
        }
        
        DoAllStub doAllStub = new DoAllStub(stubs);
        
        Object expectedResult = stubs[stubs.length-1].invokeResult;
        Object actualResult = doAllStub.invoke(invocation);
        
        assertEquals("result", expectedResult, actualResult);
    }
    
    public void testDescribesAllStubsInDescription() {
        MockStub[] stubs = new MockStub[4];
        for (int i = 0; i < stubs.length; i++) {
            String name = "stubs["+i+"]";
            stubs[i] = new MockStub(name);
            stubs[i].describeToOutput = name;
        }
        
        DoAllStub doAllStub = new DoAllStub(stubs);
        
        String description = doAllStub.describeTo(new StringBuffer()).toString();
        
        assertTrue("no list of stubs in description",
                   description.indexOf("stubs[0], stubs[1], stubs[2], stubs[3]") >= 0);
    }
}
