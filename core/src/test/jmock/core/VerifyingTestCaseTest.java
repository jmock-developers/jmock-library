package test.jmock.core;

import junit.framework.TestCase;

import org.jmock.core.VerifyingTestCase;
import org.jmock.expectation.ExpectationCounter;

import test.jmock.core.testsupport.MockVerifiable;



public abstract class VerifyingTestCaseTest extends TestCase {
    public static class ExampleTestCase extends VerifyingTestCase {
        private MockVerifiable verifiableField = new MockVerifiable();
        
        public ExampleTestCase() {
        	setName("testMethod");
        }
        
        public void setExpectedVerifyCalls( int n ) {
        	verifiableField.verifyCalls.setExpected(n);
        }
        
        public void verifyExpectations() {
        	verifiableField.verifyExpectations();
        }
        
        public void testMethod() {
            // Success!
        }
    }
    
    public void testAutomaticallyVerifiesVerifiableFieldsAfterTheTestRunAndBeforeTearDown()
        throws Throwable
    {
        ExampleTestCase testCase = new ExampleTestCase();
        
        testCase.setExpectedVerifyCalls(1);
        testCase.runBare();
        testCase.verifyExpectations();
    }
    
    public void testVerificationCanBeOverridden()
        throws Throwable
    {
    	final ExpectationCounter overriddenVerifyCalls = 
            new ExpectationCounter("overridden verify #calls");
        
        ExampleTestCase testCase = new ExampleTestCase() {
        	public void verify() {
        		overriddenVerifyCalls.inc();
            }
        };
        
        overriddenVerifyCalls.setExpected(1);
        testCase.runBare();
        overriddenVerifyCalls.verify();
        testCase.verifyExpectations();
    }
    
    public void testOverridingRunTestDoesNotAffectVerification() throws Throwable {
        ExampleTestCase testCase = new ExampleTestCase() {
        	public void runTest() {}
        };
        
        testCase.setExpectedVerifyCalls(1);
        testCase.runBare();
        testCase.verifyExpectations();
    }
    
    public void testOverridingSetUpAndTearDownDoesNotAffectVerification() throws Throwable {
        ExampleTestCase testCase = new ExampleTestCase() {
            public void setUp() {}
            public void tearDown() {}
        };
        
        testCase.setExpectedVerifyCalls(1);
        testCase.runBare();
        testCase.verifyExpectations();
    }
}
