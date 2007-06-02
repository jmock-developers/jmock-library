package org.jmock.core.stub;

import org.jmock.core.Invocation;
import org.jmock.core.Stub;

public class DoAllStub implements Stub {
    private final Stub[] stubs;
    
    public DoAllStub(Stub[] stubs) {
        this.stubs = (Stub[]) stubs.clone();
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        Object result = null;
        
        for (int i = 0; i < stubs.length; i++) {
            result = stubs[i].invoke(invocation);
        }
        
        return result;
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("do all of ");
        for (int i = 0; i < stubs.length; i++) {
            if (i > 0) buffer.append(", ");
            stubs[i].describeTo(buffer);
        }
        return buffer;
    }
}
