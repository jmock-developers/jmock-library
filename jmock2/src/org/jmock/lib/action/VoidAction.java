/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.lib.action;

import org.hamcrest.Description;
import org.jmock.core.Action;
import org.jmock.core.Invocation;

public class VoidAction implements Action {
    public static final VoidAction INSTANCE = new VoidAction();

    public Object invoke(Invocation invocation) throws Throwable {
        return null;
    }

    public void describeTo(Description description) {
        description.appendText("is void");
    }
}
