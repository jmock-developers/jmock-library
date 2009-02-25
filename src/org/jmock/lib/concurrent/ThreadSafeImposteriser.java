package org.jmock.lib.concurrent;

import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;

/**
 * A Decorator that wraps an Imposteriser and makes the Mockery thread-safe.
 *  
 * @author Nat Pryce
 */
public class ThreadSafeImposteriser implements Imposteriser {
  private final Object sync = new Object();
  private final Imposteriser imposteriser;

  public ThreadSafeImposteriser(Imposteriser imposteriser) {
    this.imposteriser = imposteriser;
  }

  public boolean canImposterise(Class<?> type) {
    return imposteriser.canImposterise(type);
  }

  public <T> T imposterise(Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes) {
      return imposteriser.imposterise(wrapSynchronizationAround(mockObject), mockedType, ancilliaryTypes);
  }
  
  private Invokable wrapSynchronizationAround(final Invokable mockObject) {
    return new Invokable() {
      public Object invoke(Invocation invocation) throws Throwable {
        synchronized(sync) {
          return mockObject.invoke(invocation);
        }
      }
    };
  }
}