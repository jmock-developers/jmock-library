package test.jmock.core;

import junit.framework.TestCase;

import org.jmock.core.VerifyingTestCase;
import org.jmock.expectation.ExpectationCounter;

import test.jmock.core.testsupport.MockVerifiable;



public class VerifyingTestCaseTest extends TestCase {
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

	public void testCanBeConstructedWithAName() {
		String name = "NAME";

		VerifyingTestCase testCase = new VerifyingTestCase(name) {};

		assertEquals( "name", name, testCase.getName() );
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

    public void testAutomaticallyVerifiesAnyObjectsRegisteredAsRequiringVerificatin() throws Throwable {
        // setup
        ExampleTestCase testCase = new ExampleTestCase();
        MockVerifiable aVerifiable = new MockVerifiable();
        MockVerifiable anotherVerifiable = new MockVerifiable();

        // expect
        aVerifiable.setExpectedVerifyCalls(1);
        anotherVerifiable.setExpectedVerifyCalls(1);

        // execute
        testCase.registerToVerify(aVerifiable);
        testCase.registerToVerify(anotherVerifiable);
        testCase.runBare();

        // verify
        aVerifiable.verifyExpectations();
        anotherVerifiable.verifyExpectations();
    }

    public void testAllowsVerifiableObjectsToBeUnregistered() throws Throwable {
        // setup
        ExampleTestCase testCase = new ExampleTestCase();
        MockVerifiable aVerifiable = new MockVerifiable();

        // expect
        aVerifiable.setExpectedVerifyCalls(0);

        // execute
        testCase.registerToVerify(aVerifiable);
        testCase.unregisterToVerify(aVerifiable);
        testCase.runBare();

        // verify
        aVerifiable.verifyExpectations();
    }
}
