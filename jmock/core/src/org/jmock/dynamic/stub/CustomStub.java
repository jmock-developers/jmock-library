package org.jmock.dynamic.stub;

import org.jmock.dynamic.Stub;

/** 
 * A partial implementation of the Stub interface that makes it convenient to implement
 * application-specific stubs with inline anonymous classes:
 * <pre>
 * final String name = "NAME";
 * final StringBuffer buffer = new StringBuffer();
 * 
 * mock.expect("writeTo", C.args(C.same(buffer))), new CustomStub("appends name to buffer") {
 *     public Object invoke( Invocation invocation ) throws Throwable {
 *         return buffer.append(name);
 *     }
 * } );
 * </pre>
 */ 
public abstract class CustomStub implements Stub {
	private String description;
    
	public CustomStub( String description ) {
        this.description = description;
	}
	
    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append(description);
    }
}
