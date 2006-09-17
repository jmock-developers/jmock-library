package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;

public class AnyMethodAcceptanceTests extends TestCase {
    public interface AnotherType {
        void anotherMethod();
    }
    
    Mockery context = new Mockery();
    
    MockedType mock = context.mock(MockedType.class, "mock");
    AnotherType anotherMock = context.mock(AnotherType.class, "anotherMock");
    
    public void testElidingTheMethodMeansAnyMethodWithAnyArguments() {
        context.expects(new InAnyOrder() {{
            allowing (mock);
        }});
        
        mock.method1();
        mock.method2();
        mock.method3();
        mock.method4();
    }
    
    public void testCanElideMethodsOfMoreThanOneMockObject() {
        context.expects(new InAnyOrder() {{
            allowing (mock);
            allowing (anotherMock);
        }});
        
        mock.method1();
        anotherMock.anotherMethod();
    }
}
