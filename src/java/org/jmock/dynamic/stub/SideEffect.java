package org.jmock.dynamic.stub;

import org.jmock.dynamic.Stub;

/** 
 * A partial implementation of the Stub interface that makes it convenient to mock
 * side effects with inline anonymous subclasses:
 * <pre>
 * final String name = "NAME";
 * final StringBuffer buffer = new StringBuffer();
 * 
 * mock.expect("writeTo", C.args(C.same(buffer))), new SideEffect("appends name to buffer") {
 *     public Object invoke( Invocation invocation ) throws Throwable {
 *         return buffer.append(name);
 *     }
 * } );
 * </pre>
 */ 
public abstract class SideEffect implements Stub {
	private String description;
    
	public SideEffect( String description ) {
        this.description = description;
	}
	
    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append(description);
    }
}
