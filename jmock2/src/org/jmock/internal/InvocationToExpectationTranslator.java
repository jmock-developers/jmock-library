package org.jmock.internal;

import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;

public class InvocationToExpectationTranslator implements Invokable {
    private final ExpectationCapture capture;
    private Action defaultAction;
    
    public InvocationToExpectationTranslator(ExpectationCapture capture,
                                             Action defaultAction) 
    {
        this.capture = capture;
        this.defaultAction = defaultAction;
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        capture.createExpectationFrom(invocation);
        return defaultAction.invoke(invocation);
    }
}
