/*
 * Created on Dec 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jmock.dynamic;

import org.jmock.dynamic.stub.CustomStub;


class NoMatchFoundStub extends CustomStub {
    private InvocationDispatcher dispatcher;
    
    public NoMatchFoundStub(InvocationDispatcher dispatcher) {
        super("report no matching method");
        this.dispatcher = dispatcher;
    }
    public Object invoke( Invocation invocation ) throws Throwable {
		throw new DynamicMockError( invocation, dispatcher, "No match found" );
    }
}