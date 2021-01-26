package uk.jamesdal.perfmock.internal;

import uk.jamesdal.perfmock.api.Action;
import uk.jamesdal.perfmock.api.Invocation;
import uk.jamesdal.perfmock.api.Invokable;

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
        Invocation buildingInvocation = new Invocation(Invocation.ExpectationMode.BUILDING,invocation);
        capture.createExpectationFrom(buildingInvocation);
        return defaultAction.invoke(buildingInvocation);
    }
}
