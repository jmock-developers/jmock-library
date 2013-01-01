/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.internal;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.lib.JavaReflectionImposteriser;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Returns a value of the invoked method's result type. Returns nothing from
 * void methods. Zero or false results are returned for primitive types. Arrays
 * and strings are returned with a length of zero. Types that can be
 * imposterised by the action's {@link Imposteriser} are returned as <a
 * href="http://www.c2.com/cgi/wiki?NullObject">Null Objects</a>. Otherwise
 * <code>null</code> is returned. The default value can be overridden for
 * specific types.
 * 
 * @author nat
 */
public class ReturnDefaultValueAction implements Action {
    private final Map<Class<?>, Object> resultValuesByType = new HashMap<Class<?>, Object>();
    private Imposteriser imposteriser;

    public ReturnDefaultValueAction(Imposteriser imposteriser) {
        this.imposteriser = imposteriser;
        createDefaultResults();
    }

    public ReturnDefaultValueAction() {
        this(new JavaReflectionImposteriser());
    }
    
    public void setImposteriser(Imposteriser newImposteriser) {
        this.imposteriser = newImposteriser;
    }
    
    public void describeTo(Description description) {
        description.appendText("returns a default value");
    }

    public void addResult(Class<?> resultType, Object resultValue) {
        resultValuesByType.put(resultType, resultValue);
    }

    public Object invoke(Invocation invocation) throws Throwable {
      final Class<?> returnType = invocation.getInvokedMethod().getReturnType();

      if (resultValuesByType.containsKey(returnType)) {
          return resultValuesByType.get(returnType);
      }
      if (returnType.isArray()) {
          return Array.newInstance(returnType.getComponentType(), 0);
      }
      if (isIterableOrMap(returnType)) {
        if (! returnType.isInterface()) {
          return returnType.newInstance();
        } else {
          return instanceForIterableType(returnType);
        }
      }
      if (imposteriser.canImposterise(returnType)) {
          return imposteriser.imposterise(this, returnType);
      }
      return null;
    }

  private Object instanceForIterableType(Class<?> type) {
    if (type.isAssignableFrom(TreeSet.class)) {
      return new TreeSet();
    }
    if (type.isAssignableFrom(LinkedList.class)) {
      return new LinkedList();
    }
    if (type.isAssignableFrom(TreeMap.class)) {
      return new TreeMap();
    }
    return null;
  }

  private boolean isIterableOrMap(Class<?> type) {
    return Iterable.class.isAssignableFrom(type)
        || Map.class.isAssignableFrom(type);
  }

  protected void createDefaultResults() {
        addResult(boolean.class, Boolean.FALSE);
        addResult(void.class, null);
        addResult(byte.class, (byte) 0);
        addResult(short.class, (short) 0);
        addResult(int.class, 0);
        addResult(long.class, 0L);
        addResult(char.class, '\0');
        addResult(float.class, 0.0F);
        addResult(double.class, 0.0);
        addResult(Boolean.class, Boolean.FALSE);
        addResult(Byte.class, (byte) 0);
        addResult(Short.class, (short) 0);
        addResult(Integer.class, 0);
        addResult(Long.class, 0L);
        addResult(Character.class, '\0');
        addResult(Float.class, 0.0F);
        addResult(Double.class, 0.0);
        addResult(String.class, "");
        addResult(Object.class, new Object());
    }
}
