package org.jmock.dynamock;

import java.util.Arrays;

import org.jmock.Constraint;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.matcher.AnyArgumentsMatcher;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;
import org.jmock.util.MockObjectSupportTestCase;


public class MockObjectTestCase 
	extends MockObjectSupportTestCase
{
	public static final InvocationMatcher NO_ARGS = NoArgumentsMatcher.INSTANCE;
	public static final InvocationMatcher ANY_ARGS = AnyArgumentsMatcher.INSTANCE;
	
	public InvocationMatcher args() {
		return NO_ARGS;
	}
	
	public InvocationMatcher args( Constraint[] constraints ) {
		return new ArgumentsMatcher(constraints);
	}
	
	public InvocationMatcher args( Constraint constraint ) {
		return args( new Constraint[]{constraint} );
	}
	
	public InvocationMatcher args( Constraint arg1, Constraint arg2 ) {
		return args( new Constraint[] {arg1,arg2} );
	}
	
	public InvocationMatcher args( Constraint arg1, Constraint arg2, Constraint arg3 ) {
		return args( new Constraint[] {arg1,arg2,arg3} );
	}
	
	public InvocationMatcher args( Constraint arg1, Constraint arg2,
								   Constraint arg3, Constraint arg4 ) 
	{
		return args( new Constraint[] {arg1,arg2,arg3,arg4} );
	}
	
	public InvocationMatcher anyArgs(int argCount) {
		Constraint[] constraints = new Constraint[argCount];
		Arrays.fill( constraints, ANYTHING );
		return args(constraints);
	}
}
