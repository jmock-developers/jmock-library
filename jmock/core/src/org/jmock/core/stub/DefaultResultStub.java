/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.stub;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.framework.AssertionFailedError;
import org.jmock.core.CoreMock;
import org.jmock.core.Formatting;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;


public class DefaultResultStub
        implements Stub
{
    private Map resultValuesByType = new HashMap();

    public DefaultResultStub() {
        createDefaultResults();
    }

    public StringBuffer describeTo( StringBuffer buf ) {
        return buf.append("returns a default value");
    }

    public void addResult( Class resultType, Object resultValue ) {
        resultValuesByType.put(resultType, resultValue);
    }

    public Object invoke( Invocation invocation )
            throws Throwable
    {
        Class returnType = invocation.invokedMethod.getReturnType();

        if (resultValuesByType.containsKey(returnType)) {
            return resultValuesByType.get(returnType);
        } else if (returnType.isArray()) {
            return Array.newInstance(returnType.getComponentType(), 0);
        } else if (returnType.isInterface()) {
            CoreMock nullMock = new CoreMock(returnType, "null" + Formatting.classShortName(returnType));
            nullMock.setDefaultStub(this);
            return nullMock.proxy();
        } else {
            throw new AssertionFailedError(createErrorMessage(invocation));
        }
    }

    public String createErrorMessage( Invocation call ) {
        StringBuffer buf = new StringBuffer();

        buf.append("unexpected result type: ");
        buf.append(call.invokedMethod.getReturnType().toString());
        buf.append("\n");


        if (resultValuesByType.isEmpty()) {
            buf.append("no result types are registered!");
        } else {
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
