/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.builder.ExpectationBuilder;
import org.jmock.builder.Mock;
import org.jmock.expectation.AssertMo;

import test.jmock.builder.testsupport.MockExpectationBuilder;
import test.jmock.dynamic.DummyInterface;
import test.jmock.dynamic.testsupport.MockDynamicMock;
import test.jmock.dynamic.testsupport.MockStub;

public class MockTest extends TestCase {

    private MockDynamicMock mockCoreMock = new MockDynamicMock();
    private Mock mock = new Mock(mockCoreMock);

    public void testToStringComesFromUnderlyingDynamicMock() {
        mockCoreMock.toStringResult = "some string here";
        assertEquals("Should be same string", "some string here", mock.toString());
    }
    
    public void testPassesExplicitNameToUnderlyingCoreMock() {
        String explicitName = "EXPLICIT NAME";
        
        assertEquals( "should be explicit name", explicitName, 
                new Mock( DummyInterface.class, explicitName ).toString() );
    }
    
    public void testMethodAddsInvocationMockerAndReturnsMethodExpectation() {
        mockCoreMock.addCalls.setExpected(1);

        assertNotNull("Should be method expectation", mock.method("methodname"));
        mockCoreMock.verifyExpectations();
    }
    
    public void testVerifyCallsUnderlyingMock() {
        mockCoreMock.verifyCalls.setExpected(1);

        mock.verify();

        mockCoreMock.verifyExpectations();
    }
    
    private interface MockedType {}
    
    public void testReportsTypesMockedByUnderlyingMock(){
        mockCoreMock.getMockedTypeCalls.setExpected(1);
        mockCoreMock.getMockedTypeResult = MockedType.class;
        
        AssertMo.assertSame( "mocked types", 
                               MockedType.class, mock.getMockedType() );
    }
    
    public void testPassesDefaultStubToCoreMock() {
        MockStub mockDefaultStub = new MockStub();
        
        mockCoreMock.setDefaultStub.setExpected(mockDefaultStub);
        
        mock.setDefaultStub( mockDefaultStub );
        
        mockCoreMock.verifyExpectations();
    }
    
    
    static final String BUILDER_ID = "BUILDER-ID";
    
    public void testStoresExpectationBuildersByID() {
    	MockExpectationBuilder builder1 = new MockExpectationBuilder();
    	MockExpectationBuilder builder2 = new MockExpectationBuilder();
    	
    	mock.registerID( BUILDER_ID+1, builder1 );
    	mock.registerID( BUILDER_ID+2, builder2 );
    	
    	assertSame( "should be builder1", 
    				builder1, mock.lookupID(BUILDER_ID+1) );
    	assertSame( "should be builder2", 
    				builder2, mock.lookupID(BUILDER_ID+2) );
    }
    
    public void testFailsOnLookingUpUnregisteredID() {
    	try {
    		mock.lookupID(BUILDER_ID);
    	}
    	catch( AssertionFailedError ex ) {
    		assertTrue( "error message should contain invalid id",
    					ex.getMessage().indexOf(BUILDER_ID) >= 0 );
    		return;
    	}
    	fail("expected AssertionFailedError");
    }
    
    public void testFailsOnRegisteringDuplicateID() {
    	MockExpectationBuilder builder1 = new MockExpectationBuilder();
    	MockExpectationBuilder builder2 = new MockExpectationBuilder();
    	
    	mock.registerID( BUILDER_ID, builder1 );
    	
    	try {
    		mock.registerID( BUILDER_ID, builder2 );
    	}
    	catch( AssertionFailedError ex ) {
    		assertTrue( "error message should contain invalid id",
    			ex.getMessage().indexOf(BUILDER_ID) >= 0 );
    		return;
    	}
    	fail("expected AssertionFailedError");
    }
    
    public void testUsesMethodNameAsDefaultBuilderID() {
    	String methodName = "METHOD-NAME";
    	
    	ExpectationBuilder builder = mock.method(methodName);
    	assertSame( "should be builder", builder, mock.lookupID(methodName) );
    }

    
    public void testOverridesDefaultBuilderIDForSameMethodName() {
    	String methodName = "METHOD-NAME";
    	
    	mock.method(methodName);
    	ExpectationBuilder builder2 = mock.method(methodName);
    	
    	assertSame( "should be builder2", builder2, mock.lookupID(methodName) );
    }
}

