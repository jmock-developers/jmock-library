package org.jmock.builder;

import java.util.Iterator;
import java.util.List;

import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;
import org.jmock.dynamic.InvocationMocker.Describer;
import org.jmock.dynamic.matcher.MethodNameMatcher;


// This encapsulates knowledge about how the InvocationMockerBuilder class
// adds matchers to an InvocationMocker.  Therefore it should be considered
// to be part of the implementation of the InvocationMockerBuilder class.
public class InvocationMockerDescriber implements Describer {
    private static final String SEP = ", ";
    
    public boolean hasDescription() {
        return true;
    }
    
    public void describeTo( StringBuffer buffer, 
                            List matchers, Stub stub, String name) 
    {
        Iterator i = matchers.iterator();
        boolean needSeparator = false;
        boolean lastWasMethodName = false;
        
        while( i.hasNext() ) {
            InvocationMatcher matcher = (InvocationMatcher)i.next();
            
            if( !matcher.hasDescription() ) continue;
            
            if( matcher instanceof MethodNameMatcher ) {
                if( !needSeparator ) buffer.append("stub"); // first matcher
                buffer.append(": ");
                lastWasMethodName = true;
            } else {
                if( needSeparator && !lastWasMethodName ) buffer.append(SEP); 
                lastWasMethodName = false;
            }
            
            matcher.describeTo(buffer);
            needSeparator = true;
        }
        
        if( needSeparator ) buffer.append(SEP);
        stub.describeTo(buffer);
        
        if( name != null ) buffer.append(" [").append(name).append("]");
    }
}
