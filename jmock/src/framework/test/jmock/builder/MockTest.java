/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.builder.Mock;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.expectation.AssertMo;

import test.jmock.builder.testsupport.MockExpectationBuilder;
import test.jmock.dynamic.DummyInterface;
import test.jmock.dynamic.testsupport.MockDynamicMock;
import test.jmock.dynamic.testsupport.MockInvocationMatcher;
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
    
    public void testDuplicateIDOverridesExistingID() {
    	MockExpectationBuilder builder1 = new MockExpectationBuilder();
    	MockExpectationBuilder builder2 = new MockExpectationBuilder();
    	
    	mock.registerID( BUILDER_ID, builder1 );
    	mock.registerID( BUILDER_ID, builder2 );
        
        assertSame( "builder2", builder2, mock.lookupID(BUILDER_ID) );
    }
    
    public void testDuplicateUniqueIDCausesTestFailure() {
        MockExpectationBuilder builder1 = new MockExpectationBuilder();
        MockExpectationBuilder builder2 = new MockExpectationBuilder();
        
        mock.registerID( BUILDER_ID, builder1 );
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

