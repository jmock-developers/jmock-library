package org.jmock.core.matcher;

import org.jmock.core.Invocation;

<<<<<<< NoArgumentsMatcher.java

public class NoArgumentsMatcher extends StatelessInvocationMatcher
{
	public static final NoArgumentsMatcher INSTANCE = new NoArgumentsMatcher();
=======
>>>>>>> 1.3

	public boolean matches( Invocation invocation ) {
		return invocation.parameterValues.isEmpty();
	}

	public StringBuffer describeTo( StringBuffer buffer ) {
		return buffer.append("(no arguments)");
	}
}