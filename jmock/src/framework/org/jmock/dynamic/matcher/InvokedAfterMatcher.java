package org.jmock.dynamic.matcher;

import junit.framework.AssertionFailedError;

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
	
	public void invoked( Invocation invocation ) {
		if( !matches(invocation) ) {
			throw new AssertionFailedError( 
				"called out of order; should be called after " + priorCallDescription );
		}
	}
	
	public StringBuffer writeTo(StringBuffer buffer) {
		return buffer.append("after ").append(priorCallDescription);
	}
}
