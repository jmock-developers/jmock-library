package org.jmock.dynamic.matcher;



public class InvokeAtLeastOnceMatcher
	extends InvokedRecorder
{
    public void verify() {
    	verifyHasBeenInvoked();
    }
    
    public boolean hasDescription() {
        return true;
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("expected at least once");
        if( hasBeenInvoked() ) {
            buffer.append(" and has been invoked");
        }
        return buffer;
    }
}
