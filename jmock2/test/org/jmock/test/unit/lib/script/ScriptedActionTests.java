package org.jmock.test.unit.lib.script;

import static org.hamcrest.Matchers.sameInstance;
import static org.jmock.lib.script.ScriptedAction.perform;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;


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
            oneOf (callout).doSomethingWith(callback); will(perform("$0.callback()"));
            oneOf (callback).callback();
        }});
        
        callout.doSomethingWith(callback);
        
        context.assertIsSatisfied();
    }

    public void testScriptCanReferToParametersByIndex() {
        context.checking(new Expectations() {{
            oneOf (callout).doSomethingWithBoth(callback, callback2); will(perform("$1.callback()"));
            oneOf (callback2).callback();
        }});
        
        callout.doSomethingWithBoth(callback, callback2);
        
        context.assertIsSatisfied();
    }
    
    public void testScriptCanReferToInvokedObjectAs$This() throws Exception {
        context.checking(new Expectations() {{
            oneOf (callout).doSomethingWith(callback); will(perform("$0.callbackWith($this)"));
            oneOf (callback).callbackWith(callout);
        }});
        
        callout.doSomethingWith(callback);
        
        context.assertIsSatisfied();
    }
    
    public void testCanBindValuesFromTestIntoScript() throws Exception {
        final Object v1 = new Object();
        final Object v2 = new Object();
        
        context.checking(new Expectations() {{
            oneOf (callout).doSomethingWith(callback); will(perform("$0.callbackWith(x, y)").where("x", v1).where("y", v2));
            oneOf (callback).callbackWith(v1, v2);
        }});
        
        callout.doSomethingWith(callback);
        
        context.assertIsSatisfied();
    }

    public void testExceptionThrownByCallbackIsPassedBackToCaller() throws Exception {
        final Exception exception = new Exception("exception");
        
        context.checking(new Expectations() {{
            oneOf (callout).doSomethingWith(callback); will(perform("$0.throwException()"));
            oneOf (callback).throwException(); will(throwException(exception));
        }});
        
        try {
            callout.doSomethingWith(callback);
        }
        catch (Exception e) {
            Assert.assertThat(e, sameInstance(exception));
        }
        
        context.assertIsSatisfied();
    }
}
