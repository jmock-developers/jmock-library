/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import org.jmock.Constraint;
import org.jmock.builder.InvocationMockerBuilder;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.matcher.AnyArgumentsMatcher;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;
import org.jmock.util.Dummy;

import test.jmock.builder.testsupport.*;

public class InvocationMockerBuilder_Test extends MockObjectTestCase {
    private MockStubMatchersCollection mocker;
    private MockBuilderIdentityTable idTable;
    private InvocationMockerBuilder builder;
    
    public void setUp() {
    	mocker = new MockStubMatchersCollection();
    	idTable = new MockBuilderIdentityTable();
    	
    	builder = new InvocationMockerBuilder( mocker, idTable );
    }
    
    public void testCanAddCustomMatcher() {
    	InvocationMatcher matcher = 
    		(InvocationMatcher)Dummy.newDummy(InvocationMatcher.class,"matcher");
    	
    	mocker.addedMatcher.setExpected( matcher );
    	
    	assertNotNull("Should be Stub Builder", builder.match(matcher));
    	
    	mocker.verifyExpectations();
    }
    
    public void testWithMethodAddsArgumentsMatcher() {
    	mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);
    	
    	assertNotNull("Should be Stub Builder", builder.with(new Constraint[0]));
    	
    	mocker.verifyExpectations();
    }
    
    public void testWithMethodWithOneObjectArgumentAddsArgumentsMatcher() {
    	mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);
    	
    	assertNotNull("Should be Stub Builder", builder.with(eq(new Object())));
    	
    	mocker.verifyExpectations();
    }
    
    public void testWithMethodWithTwoObjectArgumentsAddsArgumentsMatcher() {
    	mocker.addedMatcherType.setExpected(ArgumentsMatcher.class);
    	
    	assertNotNull("Should be Stub Builder", 
    				  builder.with(eq(new Object()), eq(new Object())));
    	
    	mocker.verifyExpectations();
    }
    
    public void testNoParamsAddsNoArgumentMatcher() {
    	mocker.addedMatcher.setExpected(NoArgumentsMatcher.INSTANCE);
    	
    	assertNotNull("Should be Stub Builder", builder.noParams());
    	
    	mocker.verifyExpectations();
    }
    
    public void testAnyParamsAddsAnyArgumentMatcher() {
        mocker.addedMatcher.setExpected(AnyArgumentsMatcher.INSTANCE);
        
        assertNotNull("Should be Stub Builder", builder.anyParams());
        
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
    
    public void testExpectOnceAddsInvokeOnceMatcher() {
    	mocker.addedMatcherType.setExpected(InvokeOnceMatcher.class);
    	
    	assertNotNull("Should be ExpectationBuilder", builder.expectOnce() );
    	
    	mocker.verifyExpectations();
    }
    
    public void testExpectAtLeastOnceAddsInvokeAtLeastOnceMatcher() {
    	mocker.addedMatcherType.setExpected(InvokeAtLeastOnceMatcher.class);
    	
    	assertNotNull("Should be ExpectationBuilder", builder.expectAtLeastOnce() );
    	
    	mocker.verifyExpectations();
    }
    
    static final String INVOCATION_ID = "INVOCATION-ID";
    
    public void testRegistersItselfInBuilderIdentityTable() {
    	idTable.registerID.setExpected(INVOCATION_ID);
    	idTable.registerIDInvocation.setExpected(builder);
    	
    	builder.id(INVOCATION_ID);
    	
    	idTable.verify();
    }
}
