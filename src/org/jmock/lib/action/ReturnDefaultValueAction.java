/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.lib.action;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.jmock.core.Action;
import org.jmock.core.Imposteriser;
import org.jmock.core.Invocation;
import org.jmock.lib.JavaReflectionImposteriser;


public class ReturnDefaultValueAction implements Action {
    private final Map<Class<?>,Object> resultValuesByType = new HashMap<Class<?>,Object>();
    private final Imposteriser imposteriser;

    public ReturnDefaultValueAction(Imposteriser imposteriser) {
        this.imposteriser = imposteriser;
        createDefaultResults();
    }
    
    public ReturnDefaultValueAction() {
        this(new JavaReflectionImposteriser());
    }
    
    public void describeTo(Description description) {
        description.appendText("returns a default value");
    }

    public void addResult( Class resultType, Object resultValue ) {
        resultValuesByType.put(resultType, resultValue);
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        Class<?> returnType = invocation.getInvokedMethod().getReturnType();

        if (resultValuesByType.containsKey(returnType)) {
            return resultValuesByType.get(returnType);
        } else if (returnType.isArray()) {
            return Array.newInstance(returnType.getComponentType(), 0);
        } else if (returnType.isInterface()) {
            return imposteriser.imposterise(this, returnType);
        } else {
        	throw new AssertionFailedError(createErrorMessage(invocation));
        }
    }
    
    public String createErrorMessage(Invocation invocation) {
        StringBuffer buf = new StringBuffer();

        buf.append("unexpected result type: ");
        buf.append(invocation.getInvokedMethod().getReturnType().toString());
        buf.append("\n");


        if (resultValuesByType.isEmpty()) {
            buf.append("no result types are registered!");
        } 
        else {
            buf.append("expected one of: ");

            Iterator i = resultValuesByType.keySet().iterator();
            boolean separatorRequired = false;

            while (i.hasNext()) {
                if (separatorRequired) buf.append(", ");
                buf.append(((Class)i.next()).getName());

                separatorRequired = true;
            }
        }

        return buf.toString();
    }

    protected void createDefaultResults() {
        addResult(boolean.class, Boolean.FALSE);
        addResult(void.class, null);
        addResult(byte.class, new Byte((byte)0));
        addResult(short.class, new Short((short)0));
        addResult(int.class, new Integer(0));
        addResult(long.class, new Long(0L));
        addResult(char.class, new Character('\0'));
        addResult(float.class, new Float(0.0F));
        addResult(double.class, new Double(0.0));
        addResult(Boolean.class, Boolean.FALSE);
        addResult(Byte.class, new Byte((byte)0));
        addResult(Short.class, new Short((short)0));
        addResult(Integer.class, new Integer(0));
        addResult(Long.class, new Long(0L));
        addResult(Character.class, new Character('\0'));
        addResult(Float.class, new Float(0.0F));
        addResult(Double.class, new Double(0.0));
        addResult(String.class, "");
        addResult(Object.class, new Object());
    }
}
