package org.jmock.test.unit.lib.script;

import static org.hamcrest.Matchers.sameInstance;
import static org.jmock.lib.script.ScriptedAction.perform;
import static org.junit.Assert.assertThat;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;


public class ScriptedActionTests extends TestCase {
    public interface Callout {
        void doSomethingWith(Callback cb) throws Exception;
        void doSomethingWithBoth(Callback cb1, Callback cb2);
    }
    
    public interface Callback {
        void callback();
        void throwException() throws Exception;
        void callbackWith(Object o);
        void callbackWith(Object o1, Object o2);
    }
    
    Mockery context = new Mockery();
    Callout callout = context.mock(Callout.class);
    Callback callback = context.mock(Callback.class);
    Callback callback2 = context.mock(Callback.class, "callback2");
    
    
    public void testInterpretsCallbackExpression() throws Exception {
        context.checking(new Expectations() {{
            one (callout).doSomethingWith(callback); will(perform("$0.callback()"));
            one (callback).callback();
        }});
        
        callout.doSomethingWith(callback);
        
        context.assertIsSatisfied();
    }

    public void testScriptCanReferToParametersByIndex() {
        context.checking(new Expectations() {{
            one (callout).doSomethingWithBoth(callback, callback2); will(perform("$1.callback()"));
            one (callback2).callback();
        }});
        
        callout.doSomethingWithBoth(callback, callback2);
        
        context.assertIsSatisfied();
    }
    
    public void testScriptCanReferToInvokedObjectAs$This() throws Exception {
        context.checking(new Expectations() {{
            one (callout).doSomethingWith(callback); will(perform("$0.callbackWith($this)"));
            one (callback).callbackWith(callout);
        }});
        
        callout.doSomethingWith(callback);
        
        context.assertIsSatisfied();
    }
    
    public void testCanBindValuesFromTestIntoScript() throws Exception {
        final Object v1 = new Object();
        final Object v2 = new Object();
        
        context.checking(new Expectations() {{
            one (callout).doSomethingWith(callback); will(perform("$0.callbackWith(x, y)").where("x", v1).where("y", v2));
            one (callback).callbackWith(v1, v2);
        }});
        
        callout.doSomethingWith(callback);
        
        context.assertIsSatisfied();
    }

    public void testExceptionThrownByCallbackIsPassedBackToCaller() throws Exception {
        final Exception exception = new Exception("exception");
        
        context.checking(new Expectations() {{
            one (callout).doSomethingWith(callback); will(perform("$0.throwException()"));
            one (callback).throwException(); will(throwException(exception));
        }});
        
        try {
            callout.doSomethingWith(callback);
        }
        catch (Exception e) {
            assertThat(e, sameInstance(exception));
        }
        
        context.assertIsSatisfied();

    }
}
