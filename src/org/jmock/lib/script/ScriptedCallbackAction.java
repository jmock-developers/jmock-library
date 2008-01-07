package org.jmock.lib.script;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;


public class ScriptedCallbackAction implements Action {
    private final Interpreter interpreter = new Interpreter();
    private final String script;

    public ScriptedCallbackAction(String expression) {
        this.script = expression;
        this.interpreter.setStrictJava(true);
    }

    public Object invoke(Invocation invocation) throws Throwable {
        try {
            defineParameters(interpreter, invocation);
            return interpreter.eval(script);
        }
        catch (TargetError e) {
            throw e.getTarget();
        }
        catch (EvalError e) {
            throw new IllegalArgumentException("could not interpret script", e);
        }
    }
    
    private void defineParameters(Interpreter interpreter, Invocation invocation) 
        throws EvalError 
    {
        for (int i = 0; i < invocation.getParameterCount(); i++) {
            interpreter.set("$" + i, invocation.getParameter(i));
        }
    }

    public void describeTo(Description description) {
        description.appendText("perform ").appendText(script);
    }
    
    public static ScriptedCallbackAction perform(String script) {
        return new ScriptedCallbackAction(script);
    }
    
    public ScriptedCallbackAction where(String name, Object value) {
        try {
            interpreter.set(name, value);
        }
        catch (EvalError e) {
            throw new IllegalArgumentException("invalid name binding", e);
        }
        return this;
    }
}
