package org.jmock.examples.timedcache;

public interface ReloadPolicy {

    boolean shouldReload(Timestamp loadTime, Timestamp fetchTime);
}
