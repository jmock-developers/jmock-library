package org.jmock.dynamic.matcher;

import org.jmock.dynamic.Invocation;


public class AnyArgumentsMatcher extends StatelessInvocationMatcher {
    
    public static final AnyArgumentsMatcher INSTANCE = new AnyArgumentsMatcher();
    
    public boolean matches(Invocation invocation) {
        return true;
    }
    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("(any arguments)");
    }
}