package org.jmock.builder;

import org.jmock.Constraint;
import org.jmock.constraint.And;
import org.jmock.constraint.IsAnything;
import org.jmock.constraint.IsEqual;
import org.jmock.constraint.IsInstanceOf;
import org.jmock.constraint.IsNot;
import org.jmock.constraint.IsNull;
import org.jmock.constraint.IsSame;
import org.jmock.constraint.Or;
import org.jmock.constraint.StringContains;
import org.jmock.util.VerifyingTestCase;


public class MockObjectTestCase extends VerifyingTestCase 
{
	public static final Constraint ANYTHING = new IsAnything();
    public static final Constraint NULL = new IsNull();
    
	public IsEqual eq( Object operand ) {
		return new IsEqual(operand);
	}
	
    public IsEqual eq( boolean operand ) {
    	return eq( new Boolean(operand) );
    }
    
    public IsEqual eq( byte operand ) {
        return eq( new Byte(operand) );
    }
    
    public IsEqual eq( short operand ) {
        return eq( new Short(operand) );
    }
    
    public IsEqual eq( char operand ) {
        return eq( new Character(operand) );
    }
    
    public IsEqual eq( int operand ) {
        return eq( new Integer(operand) );
    }
    
    public IsEqual eq( long operand ) {
        return eq( new Long(operand) );
    }
    
    public IsEqual eq( float operand ) {
        return eq( new Float(operand) );
    }
    
    public IsEqual eq( double operand ) {
        return eq( new Double(operand) );
    }

	public IsSame same( Object operand ) {
		return new IsSame(operand);
	}
    
    public IsInstanceOf isA( Class operandClass ) {
    	return new IsInstanceOf(operandClass);
    }
    
    public StringContains stringContains( String substring ) {
    	return new StringContains(substring);
    }
    
    public IsNot not( Constraint c ) {
    	return new IsNot(c);
    }
    
    public And and( Constraint left, Constraint right ) {
        return new And(left,right);
    }
    
    public Or or( Constraint left, Constraint right ) {
    	return new Or(left,right);
    }
}

