package org.jmock.dynamic.matcher;

import org.jmock.dynamic.Invocation;


public class InvokedAfterMatcher 
	extends StatelessInvocationMatcher 
{
	private InvokedRecorder priorCallRecorder;
	private String priorCallDescription;
	
	public InvokedAfterMatcher( InvokedRecorder priorCallRecorder, 
								String priorCallDescription ) 
	{
		this.priorCallRecorder = priorCallRecorder;
		this.priorCallDescription = priorCallDescription;
	}
	
	public boolean matches(Invocation invocation) {
		return priorCallRecorder.hasBeenInvoked();
	}
	
	public StringBuffer writeTo(StringBuffer buffer) {
		return buffer.append("after ").append(priorCallDescription);
	}
}
