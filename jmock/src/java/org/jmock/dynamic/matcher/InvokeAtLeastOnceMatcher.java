package org.jmock.dynamic.matcher;



public class InvokeAtLeastOnceMatcher
	extends InvokedRecorder
{
    public void verify() {
    	verifyHasBeenInvoked();
    }
    
    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("called at least once");
    }
}
