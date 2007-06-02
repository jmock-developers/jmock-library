/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.timedcache;

public interface ReloadPolicy
{

    boolean shouldReload( Timestamp loadTime, Timestamp fetchTime );
}
