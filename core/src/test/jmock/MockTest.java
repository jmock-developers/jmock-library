/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.Mock;
import org.jmock.core.InvocationMatcher;
import org.jmock.expectation.AssertMo;

import test.jmock.builder.testsupport.MockMatchBuilder;
import test.jmock.core.DummyInterface;
import test.jmock.core.testsupport.MockDynamicMock;
import test.jmock.core.testsupport.MockInvocationMatcher;
import test.jmock.core.testsupport.MockStub;

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
    
    public void testStubAddsInvocationMockerAndReturnsBuilder() {
        mockCoreMock.addCalls.setExpected(1);

        assertNotNull("Should be method expectation", mock.stub());
        mockCoreMock.verifyExpectations();
    }
    
    public void testExpectAddsInvocationMockerAndAddsExpectationAndReturnsBuilder() {
        InvocationMatcher expectation = new MockInvocationMatcher(); 
        
        mockCoreMock.addCalls.setExpected(1);
        
        assertNotNull("Should be method expectation", mock.expect(expectation));
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
    	MockMatchBuilder builder1 = new MockMatchBuilder();
    	MockMatchBuilder builder2 = new MockMatchBuilder();
    	
    	mock.registerUniqueID( BUILDER_ID+1, builder1 );
    	mock.registerUniqueID( BUILDER_ID+2, builder2 );
    	
    	assertSame( "should be builder1", 
    				builder1, mock.lookupIDForSameMock(BUILDER_ID+1) );
    	assertSame( "should be builder2", 
    				builder2, mock.lookupIDForSameMock(BUILDER_ID+2) );
    }
    
    public void testFailsOnLookingUpUnregisteredID() {
    	try {
    		mock.lookupIDForSameMock(BUILDER_ID);
    	}
    	catch( AssertionFailedError ex ) {
    		assertTrue( "error message should contain invalid id",
    					ex.getMessage().indexOf(BUILDER_ID) >= 0 );
    		return;
    	}
    	fail("expected AssertionFailedError");
    }
    
    public void testLookingUpMethodNameForSameMockReturnsLastButOneRegistration() {
    	MockMatchBuilder builder1 = new MockMatchBuilder();
    	MockMatchBuilder builder2 = new MockMatchBuilder();
    	
    	mock.registerMethodName( BUILDER_ID, builder1 );
    	mock.registerMethodName( BUILDER_ID, builder2 );
        
        assertSame( "builder2", builder2, mock.lookupIDForOtherMock(BUILDER_ID) );
    }
    
    public void testLookingUpMethodNameForOtherMockReturnsLastRegistration() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();
        
        mock.registerMethodName( BUILDER_ID, builder1 );
        mock.registerMethodName( BUILDER_ID, builder2 );
        
        assertSame( "builder2", builder2, mock.lookupIDForOtherMock(BUILDER_ID) );
    }
    
    public void testDuplicateUniqueIDCausesTestFailure() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();
        
        mock.registerUniqueID( BUILDER_ID, builder1 );
        try {
            mock.registerUniqueID( BUILDER_ID, builder2 );
        }
        catch( AssertionFailedError ex ) {
            AssertMo.assertIncludes( "should contain invalid ID",
                                     BUILDER_ID, ex.getMessage() );
            return;
        }
        
        fail("expected failure");
    }
}

