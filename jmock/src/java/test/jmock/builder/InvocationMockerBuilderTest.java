/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import org.jmock.Constraint;
import org.jmock.builder.InvocationMockerBuilder;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;
import org.jmock.dynamock.C;

public class InvocationMockerBuilderTest extends MockObjectTestCase {
    private MockStubMatchersCollection mocker = new MockStubMatchersCollection();
    private InvocationMockerBuilder builder = new InvocationMockerBuilder(mocker);

    public void testWhenPassedAddsArgumentsMatcher() {
    	mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);
    	
    	assertNotNull("Should be Stub Builder", builder.args(new Constraint[0]));
    	
    	mocker.verifyExpectations();
    }
    
    public void testWhenPassedWithOneObjectArgumentAddsArgumentsMatcher() {
    	mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);
    	
    	assertNotNull("Should be Stub Builder", builder.args(eq(new Object())));
    	
    	mocker.verifyExpectations();
    }
    
    public void testWhenPassedWithTwoObjectArgumentsAddsArgumentsMatcher() {
    	mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);
    	
    	assertNotNull("Should be Stub Builder", 
    				  builder.args(eq(new Object()), eq(new Object())));
    	
    	mocker.verifyExpectations();
    }
    
    public void testNoParamsAddsNoArgumentMatcher() {
    	mocker.addedMatcher.setExpected(C.NO_ARGS);
    	
    	assertNotNull("Should be Stub Builder", builder.noParams());
    	
    	mocker.verifyExpectations();
    }
    
    public void testIsVoidSetsVoidStub() {
        mocker.setStubType.setExpected(VoidStub.class);

        assertNotNull("Should be expectation builder", builder.isVoid());

        mocker.verifyExpectations();
    }

    public void testWillReturnSetsReturnStub() {
        String returnValue = "return value";
        
        mocker.setStubType.setExpected(ReturnStub.class);
        mocker.setStubReturnValue.setExpected(returnValue);

		assertNotNull("Should be expectation builder", builder.willReturn(returnValue));

        mocker.verifyExpectations();
    }
    
    public void testWillThrowSetsThrowStub() {
        mocker.setStubType.setExpected(ThrowStub.class);

        assertNotNull("Should be expectation builder", builder.willThrow(new Exception("thrown value")));
        
        mocker.verifyExpectations();
    }
    
    public void testExpectOnceAddsCallOnceMatcher() {
    	mocker.addedMatcherType.setExpected(InvokeOnceMatcher.class);
    	
    	assertNotNull("Should be ExpectationBuilder", builder.expectOnce() );
    	
    	mocker.verifyExpectations();
    }
}
