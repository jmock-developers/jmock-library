package org.jmock.dynamic.matcher;

import org.jmock.dynamic.Invocation;


public class NoArgumentsMatcher extends StatelessInvocationMatcher {
    public static final NoArgumentsMatcher INSTANCE = new NoArgumentsMatcher();
    
    public boolean matches(Invocation invocation) {
        return invocation.getParameterValues().isEmpty();
    }
    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("(no arguments)");
    }
}