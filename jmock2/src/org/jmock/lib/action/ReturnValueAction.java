/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.lib.action;

import org.hamcrest.Description;
import org.jmock.core.Action;
import org.jmock.core.Invocation;

public class ReturnValueAction implements Action {
    private Object result;

    public ReturnValueAction(Object result) {
        this.result = result;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        return result;
    }

    public void describeTo(Description description) {
        description.appendText("returns ");
        description.appendValue(result);
    }
}
