package test.jmock.builder;

import junit.framework.TestCase;

import org.jmock.Constraint;
import org.jmock.util.Dummy;
import org.jmock.util.MockObjectSupportTestCase;

import test.jmock.dynamic.testsupport.*;



public class MockObjectTestCase_Test extends TestCase {
    Constraint trueConstraint = AlwaysTrue.INSTANCE;
    Constraint falseConstraint = AlwaysFalse.INSTANCE;
    
    private MockObjectSupportTestCase testCase;
    
    public void setUp() {
    	testCase = new MockObjectSupportTestCase();
    }
    
    public void testHasConvenienceConstantForIsAnything() {
        assertConstraintTrue( MockObjectSupportTestCase.ANYTHING, new Object() );
        assertConstraintTrue( MockObjectSupportTestCase.ANYTHING, new Object() );
    }
    
    public void testHasConvenienceConstantForIsNull() {
        assertConstraintTrue( MockObjectSupportTestCase.NULL, null );
        assertConstraintFalse( MockObjectSupportTestCase.NULL, "not null" );
    }
    
    public void testHasConvenienceMethodForCreatingIsEqualConstraints() {
        String stringValue = new String("STRING VALUE");
        
		assertConstraintTrue( testCase.eq(stringValue), stringValue );
    }
    
    public void testConvenienceMethodForCreatingIsEqualConstraintsIsOverloadedForPrimitiveTypes() {
        assertConstraintTrue( testCase.eq(true), new Boolean(true) );
        assertConstraintTrue( testCase.eq(false), new Boolean(false) );
        assertConstraintTrue( testCase.eq((byte)1), new Byte((byte)1) );
        assertConstraintTrue( testCase.eq((short)1), new Short((short)1) );
        assertConstraintTrue( testCase.eq('a'), new Character('a') );
        assertConstraintTrue( testCase.eq(1), new Integer(1) );
        assertConstraintTrue( testCase.eq(1L), new Long(1L) );
        assertConstraintTrue( testCase.eq(1.0F), new Float(1.0F) );
        assertConstraintTrue( testCase.eq(1.0), new Double(1.0) );
        
        assertConstraintFalse( testCase.eq(true), new Boolean(false) );
        assertConstraintFalse( testCase.eq(false), new Boolean(true) );
        assertConstraintFalse( testCase.eq((byte)1), new Byte((byte)2) );
        assertConstraintFalse( testCase.eq((short)1), new Short((short)2) );
        assertConstraintFalse( testCase.eq('a'), new Character('b') );
        assertConstraintFalse( testCase.eq(1), new Integer(2) );
        assertConstraintFalse( testCase.eq(1L), new Long(2L) );
        assertConstraintFalse( testCase.eq(1.0F), new Float(2.0F) );
        assertConstraintFalse( testCase.eq(1.0), new Double(2.0) );
    }
    
    public void testHasConvenienceMethodForCreatingIsSameConstraints() {
        Object o1 = Dummy.newDummy("o1");
        Object o2 = Dummy.newDummy("o2");
        
        assertConstraintTrue( testCase.same(o1), o1 );
        assertConstraintFalse( testCase.same(o1), o2 );
    }
    
    public void testHasConvenienceMethodForCreatingIsAConstraints() {
    	String aString = "a string";
        
        assertConstraintTrue( testCase.isA(String.class), aString );
        assertConstraintTrue( testCase.isA(Object.class), aString );
        assertConstraintFalse( testCase.isA(Integer.class), aString );
    }
    
    public void testHasConvenienceMethodForCreatingStringContainsConstraints() {
    	assertConstraintTrue( testCase.stringContains("fruit"), "fruitcake" );
        assertConstraintFalse( testCase.stringContains("chocolate chips"), "fruitcake" );
    }
    
    public void testHasConvenienceMethodForLogicalNegationOfConstraints() {
        assertConstraintTrue( testCase.not(testCase.eq("hello")), "world" );
        assertConstraintFalse( testCase.not(testCase.eq("hello")), "hello" );
    }
    
    public void testHasConvenienceMethodForLogicalConjunctionOfConstraints() {
        Object ignored = new Object();
        
        assertConstraintTrue(  testCase.and( trueConstraint,  trueConstraint  ), ignored );
        assertConstraintFalse( testCase.and( trueConstraint,  falseConstraint ), ignored );
        assertConstraintFalse( testCase.and( falseConstraint, trueConstraint  ), ignored );
        assertConstraintFalse( testCase.and( falseConstraint, falseConstraint ), ignored );
    }
    
    public void testHasConvenienceMethodForLogicalDisjunctionOfConstraints() {
        Object ignored = new Object();
        
        assertConstraintTrue(  testCase.or( trueConstraint,  trueConstraint  ), ignored );
        assertConstraintTrue(  testCase.or( trueConstraint,  falseConstraint ), ignored );
        assertConstraintTrue(  testCase.or( falseConstraint, trueConstraint  ), ignored );
        assertConstraintFalse( testCase.or( falseConstraint, falseConstraint ), ignored );
    }
    
    private void assertConstraintTrue( Constraint constraint, Object argument ) {
    	assertTrue( "expected <"+constraint+"> to return true when passed <"+argument+">", 
                    constraint.eval(argument) );
    }
    
    private void assertConstraintFalse( Constraint constraint, Object argument ) {
        assertFalse( "expected <"+constraint+"> to return false when passed <"+argument+">", 
                     constraint.eval(argument) );
    }
}
