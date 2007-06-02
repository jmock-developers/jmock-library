package org.jmock.examples.website;

import java.util.List;
import org.jmock.core.Stub;
import org.jmock.core.Invocation;


public class AddToListStub implements Stub
{
    Object element;

    public AddToListStub( Object element ) {
        this.element = element;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("adds <"+element+"> to a list");
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        ((List)invocation.parameterValues.get(0)).add(element);
        return null;
    }
}
