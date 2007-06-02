/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.jmock.core.Constraint;
import org.jmock.core.MockObjectSupportTestCase;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.constraint.IsNothing;
import org.jmock.util.Dummy;


public class MockObjectSupportTestCaseTest extends TestCase
{
    private static final String DUMMY_NAME = "DUMMY NAME";

    Constraint trueConstraint = new IsAnything("always true");
    Constraint falseConstraint = new IsNothing("always false");


    interface ExampleInterface
    {
        void method1();
    }


    MockObjectSupportTestCase testCase;

    public void setUp() {
        testCase = new MockObjectSupportTestCase() {};
    }

    public void testCanBeConstructedWithAName() {
        String name = "NAME";

        testCase = new MockObjectSupportTestCase(name) {};

        assertEquals("name", name, testCase.getName());
    }

    public void testCanCreateNamedDummyObjects() {
        Object dummy = testCase.newDummy(DUMMY_NAME);

        assertEquals("should return name from toString",
                     DUMMY_NAME, dummy.toString());
    }

    public void testCanCreateNamedDummyObjectsThatImplementASpecificInterface() {
        Object dummy = testCase.newDummy(ExampleInterface.class, DUMMY_NAME);

        assertTrue("should be instanceof ExampleInterface",
                   dummy instanceof ExampleInterface);
        assertEquals("should return name from toString",
                     DUMMY_NAME, dummy.toString());
    }

    public void testGeneratesUsefulNamesForDummiesFromTheDummiedInterface() {
        Object dummy = testCase.newDummy(ExampleInterface.class);

        assertEquals("should return name from toString",
                     "dummyExampleInterface", dummy.toString());
    }

    public void testHasConvenienceConstantForIsAnything() {
        assertConstraintTrue(MockObjectSupportTestCase.ANYTHING, new Object());
        assertConstraintTrue(MockObjectSupportTestCase.ANYTHING, new Object());
    }

    public void testHasConvenienceConstantForIsNull() {
        assertConstraintTrue(MockObjectSupportTestCase.NULL, null);
        assertConstraintFalse(MockObjectSupportTestCase.NULL, "not null");
    }

    public void testHasConvenienceConstantForNotNull() {
        assertConstraintTrue(MockObjectSupportTestCase.NOT_NULL, "not null");
        assertConstraintFalse(MockObjectSupportTestCase.NOT_NULL, null );
    }

    public void testHasConvenienceMethodForCreatingIsEqualConstraints() {
        String stringValue = new String("STRING VALUE");

        assertConstraintTrue(testCase.eq(stringValue), stringValue);
    }

    public void testConvenienceMethodForCreatingIsEqualConstraintsIsOverloadedForPrimitiveTypes() {
        assertConstraintTrue(testCase.eq(true), new Boolean(true));
        assertConstraintTrue(testCase.eq(false), new Boolean(false));
        assertConstraintTrue(testCase.eq((byte)1), new Byte((byte)1));
        assertConstraintTrue(testCase.eq((short)1), new Short((short)1));
        assertConstraintTrue(testCase.eq('a'), new Character('a'));
        assertConstraintTrue(testCase.eq(1), new Integer(1));
        assertConstraintTrue(testCase.eq(1L), new Long(1L));
        assertConstraintTrue(testCase.eq(1.0F), new Float(1.0F));
        assertConstraintTrue(testCase.eq(1.0), new Double(1.0));

        assertConstraintFalse(testCase.eq(true), new Boolean(false));
        assertConstraintFalse(testCase.eq(false), new Boolean(true));
        assertConstraintFalse(testCase.eq((byte)1), new Byte((byte)2));
        assertConstraintFalse(testCase.eq((short)1), new Short((short)2));
        assertConstraintFalse(testCase.eq('a'), new Character('b'));
        assertConstraintFalse(testCase.eq(1), new Integer(2));
        assertConstraintFalse(testCase.eq(1L), new Long(2L));
        assertConstraintFalse(testCase.eq(1.0F), new Float(2.0F));
        assertConstraintFalse(testCase.eq(1.0), new Double(2.0));
    }

    public void testConvenienceMethodForCreatingArrayContainingConstraintsIsOverloadedForPrimitiveTypes() {
        assertConstraintTrue(testCase.arrayContaining("a"), new String[]{"a"});
        assertConstraintTrue(testCase.arrayContaining(true), new boolean[]{true});
        assertConstraintTrue(testCase.arrayContaining(false), new boolean[]{false});
        assertConstraintTrue(testCase.arrayContaining((byte)1), new byte[]{(byte)1});
        assertConstraintTrue(testCase.arrayContaining((short)1), new short[]{(short)1});
        assertConstraintTrue(testCase.arrayContaining('a'), new char[]{'a'});
        assertConstraintTrue(testCase.arrayContaining(1), new int[]{1});
        assertConstraintTrue(testCase.arrayContaining(1L), new long[]{1L});
        assertConstraintTrue(testCase.arrayContaining(1.0F), new float[]{1.0F});
        assertConstraintTrue(testCase.arrayContaining(1.0), new double[]{1.0});

        assertConstraintFalse(testCase.arrayContaining("a"), new String[]{"b"});
        assertConstraintFalse(testCase.arrayContaining(true), new boolean[]{false});
        assertConstraintFalse(testCase.arrayContaining(false), new boolean[]{true});
        assertConstraintFalse(testCase.arrayContaining((byte)1), new byte[]{(byte)2});
        assertConstraintFalse(testCase.arrayContaining((short)1), new short[]{(short)2});
        assertConstraintFalse(testCase.arrayContaining('a'), new char[]{'b'});
        assertConstraintFalse(testCase.arrayContaining(1), new int[]{2});
        assertConstraintFalse(testCase.arrayContaining(1L), new long[]{2L});
        assertConstraintFalse(testCase.arrayContaining(1.0F), new float[]{2.0F});
        assertConstraintFalse(testCase.arrayContaining(1.0), new double[]{2.0});
    }

    public void testHasConvenienceMethodForCreatingIsSameConstraints() {
        Object o1 = Dummy.newDummy("o1");
        Object o2 = Dummy.newDummy("o2");

        assertConstraintTrue(testCase.same(o1), o1);
        assertConstraintFalse(testCase.same(o1), o2);
    }

    public void testHasConvenienceMethodForCreatingIsAConstraints() {
        String aString = "a string";

        assertConstraintTrue(testCase.isA(String.class), aString);
        assertConstraintTrue(testCase.isA(Object.class), aString);
        assertConstraintFalse(testCase.isA(Integer.class), aString);
    }

    public void testHasConvenienceMethodsForCreatingStringContainsConstraints() {
        assertConstraintTrue(testCase.stringContains("fruit"), "fruitcake");
        assertConstraintFalse(testCase.stringContains("chocolate chips"), "fruitcake");
        assertConstraintTrue(testCase.contains("fruit"), "fruitcake");
        assertConstraintFalse(testCase.contains("chocolate chips"), "fruitcake");
    }

    public void testHasConvenienceMethodForLogicalNegationOfConstraints() {
        assertConstraintTrue(testCase.not(testCase.eq("hello")), "world");
        assertConstraintFalse(testCase.not(testCase.eq("hello")), "hello");
    }

    public void testHasConvenienceMethodForLogicalConjunctionOfConstraints() {
        Object ignored = new Object();

        assertConstraintTrue(testCase.and(trueConstraint, trueConstraint), ignored);
        assertConstraintFalse(testCase.and(trueConstraint, falseConstraint), ignored);
        assertConstraintFalse(testCase.and(falseConstraint, trueConstraint), ignored);
        assertConstraintFalse(testCase.and(falseConstraint, falseConstraint), ignored);
    }

    public void testHasConvenienceMethodForLogicalDisjunctionOfConstraints() {
        Object ignored = new Object();

        assertConstraintTrue(testCase.or(trueConstraint, trueConstraint), ignored);
        assertConstraintTrue(testCase.or(trueConstraint, falseConstraint), ignored);
        assertConstraintTrue(testCase.or(falseConstraint, trueConstraint), ignored);
        assertConstraintFalse(testCase.or(falseConstraint, falseConstraint), ignored);
    }

    static class OneOffTestResult extends TestResult {
        public AssertionFailedError failure;
        public synchronized void addFailure(Test arg0, AssertionFailedError aFailure) {
            super.addFailure(arg0, aFailure);
            this.failure = aFailure;
        }
        public String message() {
            return failure.getMessage();
        }
    }
    
    public void testHasAssertThatMethodToUseConstraints() {
        OneOffTestResult result = new OneOffTestResult();
        MockObjectSupportTestCase example = new MockObjectSupportTestCase() {
            public void testConstraintBased() {
                assertThat("a string", eq("a different string"));
            }
            public void runTest() { testConstraintBased(); }
        };
        example.run(result);
        assertEquals("without message", 
                "\nExpected: eq(\"a different string\")\n    got : a string\n", 
                result.message());
    }
    
    private void assertConstraintTrue( Constraint constraint, Object argument ) {
        assertTrue("expected <" + constraint + "> to return true when passed <" + argument + ">",
                   constraint.eval(argument));
    }

    private void assertConstraintFalse( Constraint constraint, Object argument ) {
        assertFalse("expected <" + constraint + "> to return false when passed <" + argument + ">",
                    constraint.eval(argument));
    }
}
