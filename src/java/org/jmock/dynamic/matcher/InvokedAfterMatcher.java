package org.jmock.dynamic.matcher;

import org.jmock.dynamic.Invocation;


public class InvokedAfterMatcher 
	extends StatelessInvocationMatcher 
{
	private InvokedRecorder priorCallRecorder;
	private String priorCallID;
	
	public InvokedAfterMatcher( InvokedRecorder priorCallRecorder, String priorCallID ) {
		this.priorCallRecorder = priorCallRecorder;
		this.priorCallID = priorCallID;
	}
	
	public boolean matches(Invocation invocation) {
		return priorCallRecorder.hasBeenInvoked();
	}
	
	public StringBuffer writeTo(StringBuffer buffer) {
		return buffer.append("after ").append(priorCallID);
	}
}
