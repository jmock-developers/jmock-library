/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

import java.util.Iterator;
import java.util.List;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.InvocationMocker.Describer;
import org.jmock.core.Stub;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.matcher.ArgumentsMatcher;


/* This encapsulates knowledge about how the InvocationMockerBuilder class
 * adds matchers to an InvocationMocker.  Therefore it should be considered
 * to be part of the implementation of the InvocationMockerBuilder class.
 */

/**
 * @since 1.0
 */
public class InvocationMockerDescriber implements Describer
{
    private static final String SEP = ", ";

    public boolean hasDescription() {
        return true;
    }

    public void describeTo( StringBuffer buffer,
                            List matchers, Stub stub, String name ) {
        Iterator i = matchers.iterator();
        boolean needSeparator = false;
        boolean lastWasMethodName = false;

        while (i.hasNext()) {
            InvocationMatcher matcher = (InvocationMatcher)i.next();

            if (!matcher.hasDescription()) continue;

            if (matcher instanceof MethodNameMatcher) {
                if (!needSeparator) buffer.append("stub"); // first matcher
                buffer.append(": ");
                lastWasMethodName = true;
            } else if( matcher instanceof ArgumentsMatcher ) {
                if (needSeparator && !lastWasMethodName) buffer.append(SEP);
                lastWasMethodName = false;

            } else {
                if (needSeparator) buffer.append(SEP);
                lastWasMethodName = false;
            }
            
            matcher.describeTo(buffer);
            needSeparator = true;
        }

        if (needSeparator) buffer.append(SEP);
        stub.describeTo(buffer);

        if (name != null) buffer.append(" [").append(name).append("]");
    }
}
