/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core;

import java.util.List;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.jmock.core.*;
import org.jmock.core.matcher.StatelessInvocationMatcher;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationList;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;
import test.jmock.core.testsupport.MethodFactory;


public class InvocationMockerTest extends TestCase {
    private InvocationMatcher matchAll = new StatelessInvocationMatcher() {
        public boolean matches(Invocation invocation) {
            return true;
        }

        public StringBuffer describeTo(StringBuffer buffer) {
            return buffer.append("match all");
        }
    };
    private InvocationMatcher matchNone = new StatelessInvocationMatcher() {
        public boolean matches(Invocation invocation) {
            return false;
        }

        public StringBuffer describeTo(StringBuffer buffer) {
            return buffer.append("match none");
        }
    };

    public class MockInvocationMatcher implements InvocationMatcher {
        public ExpectationValue invocation = new ExpectationValue("MockInvocationMatcher.invoked");
        public ExpectationValue match = new ExpectationValue("MockInvocationMatcher.matches");
        public ExpectationCounter verifyCalls = new ExpectationCounter("Verify calls");

        public boolean matches(Invocation actualMatch) {
            match.setActual(actualMatch);
            return true;
        }

        public void invoked(Invocation actualInvocation) {
            this.invocation.setActual(actualInvocation);
        }
        
        public boolean hasDescription() {
            return true;
        }
        
        public StringBuffer describeTo(StringBuffer buffer) {
            return buffer.append("Mock matcher");
        }

        public void verify() {
            verifyCalls.inc();
        }
    }

    public class MockStub implements Stub {
        public ExpectationValue stubInvocation = new ExpectationValue("Stub invocation");

        public Object invoke(Invocation invocation) throws Throwable {
            stubInvocation.setActual(invocation);
            return "stub result";
        }

        public StringBuffer describeTo(StringBuffer buffer) {
            return buffer.append("Mock stub");
        }
    }

    private static final String ARG2 = "arg2";
    private static final String ARG1 = "arg1";

    private Invocation exampleInvocation;
    private InvocationMocker invocationMocker;

    public void setUp() {
	    MethodFactory methodFactory = new MethodFactory();
	    Method method = methodFactory.newMethod( "example", new Class[]{String.class, String.class}, Void.class,
	                                                   MethodFactory.NO_EXCEPTIONS );

	    exampleInvocation = new Invocation( new Object(), method, new Object[]{ARG1,ARG2} );
        invocationMocker = new InvocationMocker();
    }
    
    public void testCanAddMatchers() throws Throwable {
        MockInvocationMatcher mockInvocationMatcher = new MockInvocationMatcher();
        invocationMocker.addMatcher(mockInvocationMatcher);
        
        mockInvocationMatcher.match.setExpected(exampleInvocation);
        mockInvocationMatcher.invocation.setExpected(exampleInvocation);
        
        invocationMocker.matches(exampleInvocation);
        invocationMocker.invoke(exampleInvocation);

        Verifier.verifyObject(mockInvocationMatcher);
    }
    
    public void testMatchesIfAllMatchersMatch() {
        invocationMocker.addMatcher(matchAll);
        invocationMocker.addMatcher(matchAll);
        
        assertTrue("Should have matched", invocationMocker.matches(exampleInvocation));
    }

    public void testDoesNotMatchIfAnyMatcherDoesNotMatch() {
        invocationMocker.addMatcher(matchAll);
        invocationMocker.addMatcher(matchNone);

        assertFalse("Should not have matched", invocationMocker.matches(exampleInvocation));
    }
    
    public void testMatchesInvocationBeforeCallingStub() throws Throwable {
        MockInvocationMatcher mockInvocationMatcher = new MockInvocationMatcher();
        invocationMocker.addMatcher(mockInvocationMatcher);
        
        mockInvocationMatcher.invocation.setExpected(exampleInvocation);

        invocationMocker.invoke(exampleInvocation);
        
        Verifier.verifyObject(mockInvocationMatcher);
    }

    public void testDelegatesVerifyToInvocationMatchers() throws Throwable {
        MockInvocationMatcher mockInvocationMatcher = new MockInvocationMatcher();
        invocationMocker.addMatcher(mockInvocationMatcher);

        mockInvocationMatcher.verifyCalls.setExpected(1);

        invocationMocker.verify();
        
        Verifier.verifyObject(mockInvocationMatcher);
    }
    
    public void testDelegatesInvocationToStubObject() throws Throwable {
        MockStub mockStub = new MockStub();
        invocationMocker.setStub(mockStub);
        
        mockStub.stubInvocation.setExpected(exampleInvocation);
        
        assertEquals( "Should be invoke result", "stub result", 
                      invocationMocker.invoke(exampleInvocation));
        
        Verifier.verifyObject(mockStub);
    }
    
    public void testCanBeNamed() {
        String name = "~{MOCKER-NAME}~";
        InvocationMocker mocker = new InvocationMocker();
        
        mocker.setName( name );
        
        String description = mocker.describeTo(new StringBuffer()).toString();
        assertTrue( "name should be in description",
                    description.indexOf(name) >= 0 );
    }
    
    
    class MockDescriber implements InvocationMocker.Describer, Verifiable {
        public ExpectationCounter hasDescriptionCalls = 
            new ExpectationCounter("hasDescription #calls");
        public boolean hasDescriptionResult = false;
        
        public boolean hasDescription() {
            hasDescriptionCalls.inc();
            return hasDescriptionResult;
        }
        
        public ExpectationValue describeToBuf = 
            new ExpectationValue("describeTo buf");
        public ExpectationList describeToMatchers = 
            new ExpectationList("describeTo matchers");
        public ExpectationValue describeToStub = 
            new ExpectationValue("describeTo stub");
        public ExpectationValue describeToName = 
            new ExpectationValue("describeTo name");
        
        public void describeTo(StringBuffer buf, List matchers, Stub stub, String name) {
            describeToBuf.setActual(buf);
            describeToMatchers.addActualMany(matchers.iterator());
            describeToStub.setActual(stub);
            describeToName.setActual(name);
        }
        
        public void verify() {
            Verifier.verifyObject(this);
        }
    }
    
    public void testDelegatesRequestForDescriptionDescriberObject() {
        MockDescriber mockDescriber = new MockDescriber();
        MockInvocationMatcher mockMatcher1 = new MockInvocationMatcher();
        MockInvocationMatcher mockMatcher2 = new MockInvocationMatcher();
        MockStub mockStub = new MockStub();
        String invocationMockerName = "INVOCATION-MOCKER-NAME";
        StringBuffer buf = new StringBuffer();
        
        
        invocationMocker = new InvocationMocker(mockDescriber);
        invocationMocker.addMatcher(mockMatcher1);
        invocationMocker.addMatcher(mockMatcher2);
        invocationMocker.setStub(mockStub);
        invocationMocker.setName(invocationMockerName);
        
        mockDescriber.hasDescriptionCalls.setExpected(1);
        mockDescriber.hasDescriptionResult = true;
        
        mockDescriber.describeToBuf.setExpected(buf);
        mockDescriber.describeToMatchers.addActual(mockMatcher1);
        mockDescriber.describeToMatchers.addActual(mockMatcher2);
        mockDescriber.describeToStub.setExpected(mockStub);
        mockDescriber.describeToName.setExpected(invocationMockerName);
        
        assertTrue( "should have description", invocationMocker.hasDescription() );
        invocationMocker.describeTo(buf);
        
        mockDescriber.verify();
    }
}
