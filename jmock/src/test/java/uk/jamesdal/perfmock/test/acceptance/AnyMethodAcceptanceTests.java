package uk.jamesdal.perfmock.test.acceptance;

import junit.framework.TestCase;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.Mockery;

public class AnyMethodAcceptanceTests extends TestCase {
    public interface AnotherType {
        void anotherMethod();
    }
    
    Mockery context = new Mockery();
    
    MockedType mock = context.mock(MockedType.class, "mock");
    AnotherType anotherMock = context.mock(AnotherType.class, "anotherMock");
    
    public void testElidingTheMethodMeansAnyMethodWithAnyArguments() {
        context.checking(new Expectations() {{
            allowing (mock);
        }});
        
        mock.method1();
        mock.method2();
        mock.method3();
        mock.method4();
    }
    
    public void testCanElideMethodsOfMoreThanOneMockObject() {
        context.checking(new Expectations() {{
            ignoring (mock);
            ignoring (anotherMock);
        }});
        
        mock.method1();
        anotherMock.anotherMethod();
    }
}
