package org.jmock.lib.script;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

/** 
 * <p>An {@link Action} that executes a <a href="http://www.beanshell.org">BeanShell</a> script.
 * This makes it easy to implement custom actions, especially those that call back to objects 
 * passed to the mocked method as parameters.</p>
 * 
 * <p>To use a scripted action in an expectation, statically import the {@link #perform(String) perform}
 * method and call it within the <code>will(...)</code> clause of the expectation.</p>
 * 
 * <p>The script can refer to the parameters of the mocked method by the names $0 (the first parameter), $1, $2, etc,
 * and to the mock object that has been invoked by the name $this.
 * You can define other script variables by calling the action's {@link #where(String, Object) where} method.</p>
 * 
 * <p>For example:</p>
 * <pre>
 * allowing (sheep).accept(with(a(Visitor.class))); 
 *     will(perform("$0.visitSheep($this)");
 * </pre>
 * 
 * <p>is equivalent to:</p>
 * 
 * <pre>
 * allowing (sheep).accept(with(a(Visitor.class))); 
 *     will(perform("$0.visitSheep(s)").where("s", sheep);
 * </pre>
 * 
 * 
 * @author nat
 *
 */
public class ScriptedAction implements Action {
    private final Interpreter interpreter = new Interpreter();
    private final String script;

    public ScriptedAction(String expression) {
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
        interpreter.set("$this", invocation.getInvokedObject());
        for (int i = 0; i < invocation.getParameterCount(); i++) {
            interpreter.set("$" + i, invocation.getParameter(i));
        }
    }

    public void describeTo(Description description) {
        description.appendText("perform ").appendText(script);
    }
    
    /**
     * Creates an action that performs the given script.
     * 
     * @param script
     *     a BeanShell script.
     * @return
     *     the new action.
     */
    public static ScriptedAction perform(String script) {
        return new ScriptedAction(script);
    }
    
    /**
     * Defines a variable that can be referred to by the script.
     * 
     * @param name
     *     the name of the variable
     * @param value
     *     the value of the variable
     * @return
     *     the action, so that more variables can be defined if needed
     */
    public ScriptedAction where(String name, Object value) {
        try {
            interpreter.set(name, value);
        }
        catch (EvalError e) {
            throw new IllegalArgumentException("invalid name binding", e);
        }
        return this;
    }
}
